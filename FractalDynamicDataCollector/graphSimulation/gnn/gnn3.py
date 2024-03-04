import os
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import tensorflow as tf
from keras.utils import to_categorical
from tensorflow import keras
from sklearn.feature_extraction.text import TfidfVectorizer
from tensorflow.keras import layers


class GraphConvLayer(layers.Layer):
    def __init__(
        self,
        hidden_units,
        dropout_rate=0.2,
        aggregation_type="mean",
        combination_type="concat",
        normalize=False,
        *args,
        **kwargs,
    ):
        super(GraphConvLayer, self).__init__(*args, **kwargs)

        self.aggregation_type = aggregation_type
        self.combination_type = combination_type
        self.normalize = normalize

        self.ffn_prepare = self.create_ffn(hidden_units, dropout_rate)
        if self.combination_type == "gated":
            self.update_fn = layers.GRU(
                units=hidden_units,
                activation="tanh",
                recurrent_activation="sigmoid",
                dropout=dropout_rate,
                return_state=True,
                recurrent_dropout=dropout_rate,
            )
        else:
            self.update_fn = self.create_ffn(hidden_units, dropout_rate)

    def create_ffn(self, hidden_units, dropout_rate, name=None):
        fnn_layers = []

        for units in hidden_units:
            fnn_layers.append(layers.BatchNormalization())
            fnn_layers.append(layers.Dropout(dropout_rate))
            fnn_layers.append(layers.Dense(units, activation=tf.nn.gelu))

        return keras.Sequential(fnn_layers, name=name)

    def prepare(self, node_repesentations, weights=None):
        # node_repesentations shape is [num_edges, embedding_dim].
        messages = self.ffn_prepare(node_repesentations)
        if weights is not None:
            messages = messages * tf.expand_dims(weights, -1)
        return messages

    def aggregate(self, node_indices, neighbour_messages, node_repesentations):
        # node_indices shape is [num_edges].
        # neighbour_messages shape: [num_edges, representation_dim].
        # node_repesentations shape is [num_nodes, representation_dim]
        num_nodes = node_repesentations.shape[0]
        if self.aggregation_type == "sum":
            aggregated_message = tf.math.unsorted_segment_sum(
                neighbour_messages, node_indices, num_segments=num_nodes
            )
        elif self.aggregation_type == "mean":
            aggregated_message = tf.math.unsorted_segment_mean(
                neighbour_messages, node_indices, num_segments=num_nodes
            )
        elif self.aggregation_type == "max":
            aggregated_message = tf.math.unsorted_segment_max(
                neighbour_messages, node_indices, num_segments=num_nodes
            )
        else:
            raise ValueError(f"Invalid aggregation type: {self.aggregation_type}.")

        return aggregated_message

    def update(self, node_repesentations, aggregated_messages):
        # node_repesentations shape is [num_nodes, representation_dim].
        # aggregated_messages shape is [num_nodes, representation_dim].
        if self.combination_type == "gru":
            # Create a sequence of two elements for the GRU layer.
            h = tf.stack([node_repesentations, aggregated_messages], axis=1)
        elif self.combination_type == "concat":
            # Concatenate the node_repesentations and aggregated_messages.
            h = tf.concat([node_repesentations, aggregated_messages], axis=1)
        elif self.combination_type == "add":
            # Add node_repesentations and aggregated_messages.
            h = node_repesentations + aggregated_messages
        else:
            raise ValueError(f"Invalid combination type: {self.combination_type}.")

        # Apply the processing function.
        node_embeddings = self.update_fn(h)
        if self.combination_type == "gru":
            node_embeddings = tf.unstack(node_embeddings, axis=1)[-1]

        if self.normalize:
            node_embeddings = tf.nn.l2_normalize(node_embeddings, axis=-1)
        return node_embeddings

    def call(self, inputs):
        """Process the inputs to produce the node_embeddings.

        inputs: a tuple of three elements: node_repesentations, edges, edge_weights.
        Returns: node_embeddings of shape [num_nodes, representation_dim].
        """

        node_repesentations, edges, edge_weights = inputs
        # Get node_indices (source) and neighbour_indices (target) from edges.
        node_indices, neighbour_indices = edges[0], edges[1]
        # neighbour_repesentations shape is [num_edges, representation_dim].
        neighbour_repesentations = tf.gather(node_repesentations, neighbour_indices)

        # Prepare the messages of the neighbours.
        neighbour_messages = self.prepare(neighbour_repesentations, edge_weights)
        # Aggregate the neighbour messages.
        aggregated_messages = self.aggregate(
            node_indices, neighbour_messages, node_repesentations
        )
        # Update the node embedding with the neighbour messages.
        return self.update(node_repesentations, aggregated_messages)


class GNNNodeClassifier(tf.keras.Model):
    def __init__(
        self,
        graph_info,
        num_classes,
        hidden_units,
        aggregation_type="sum",
        combination_type="concat",
        dropout_rate=0.2,
        normalize=True,
        *args,
        **kwargs,
    ):
        super(GNNNodeClassifier, self).__init__(*args, **kwargs)

        # Unpack graph_info to three elements: node_features, edges, and edge_weight.
        node_features, edges, edge_weights = graph_info
        self.node_features = node_features
        self.edges = edges
        self.edge_weights = edge_weights
        # Set edge_weights to ones if not provided.
        print(edges.shape[1])
        if self.edge_weights is None:
            self.edge_weights = tf.ones(shape=edges.shape[1])
        # Scale edge_weights to sum to 1.
        self.edge_weights = self.edge_weights / tf.math.reduce_sum(self.edge_weights)

        # Create a process layer.
        self.preprocess = self.create_ffn(hidden_units, dropout_rate, name="preprocess")
        # Create the first GraphConv layer.
        self.conv1 = GraphConvLayer(
            hidden_units,
            dropout_rate,
            aggregation_type,
            combination_type,
            normalize,
            name="graph_conv1",
        )
        # Create the second GraphConv layer.
        self.conv2 = GraphConvLayer(
            hidden_units,
            dropout_rate,
            aggregation_type,
            combination_type,
            normalize,
            name="graph_conv2",
        )
        # Create a postprocess layer.
        self.postprocess = self.create_ffn(hidden_units, dropout_rate, name="postprocess")
        # Create a compute logits layer.
        self.compute_logits = layers.Dense(units=num_classes, name="logits")

    def create_ffn(self, hidden_units, dropout_rate, name=None):
        fnn_layers = []

        for units in hidden_units:
            fnn_layers.append(layers.BatchNormalization())
            fnn_layers.append(layers.Dropout(dropout_rate))
            fnn_layers.append(layers.Dense(units, activation=tf.nn.gelu))

        return keras.Sequential(fnn_layers, name=name)

    def call(self, input_node_indices):
        # Preprocess the node_features to produce node representations.
        x = self.preprocess(self.node_features)
        # Apply the first graph conv layer.
        x1 = self.conv1((x, self.edges, self.edge_weights))
        # Skip connection.
        x = x1 + x
        # Apply the second graph conv layer.
        x2 = self.conv2((x, self.edges, self.edge_weights))
        # Skip connection.
        x = x2 + x
        # Postprocess node embedding.
        x = self.postprocess(x)
        # Fetch node embeddings for the input node_indices.
        node_embeddings = tf.gather(x, input_node_indices)
        # Compute logits
        return self.compute_logits(node_embeddings)

    def display_learning_curves(self, history):
        fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(15, 5))

        ax1.plot(history.history["loss"])
        ax1.plot(history.history["val_loss"])
        ax1.legend(["train", "test"], loc="upper right")
        ax1.set_xlabel("Epochs")
        ax1.set_ylabel("Loss")

        ax2.plot(history.history["acc"])
        ax2.plot(history.history["val_acc"])
        ax2.legend(["train", "test"], loc="upper right")
        ax2.set_xlabel("Epochs")
        ax2.set_ylabel("Accuracy")
        plt.show()

    def run_experiment(self, model, x_train, y_train, num_epochs, batch_size, learning_rate):
        # Compile the model.
        model.compile(
            optimizer=keras.optimizers.Adam(learning_rate),
            loss=keras.losses.SparseCategoricalCrossentropy(from_logits=True),
            metrics=[keras.metrics.SparseCategoricalAccuracy(name="acc")],
        )
        # Create an early stopping callback.
        early_stopping = keras.callbacks.EarlyStopping(
            monitor="val_acc", patience=50, restore_best_weights=True
        )
        # Fit the model.
        history = model.fit(
            x=x_train,
            y=y_train,
            epochs=num_epochs,
            batch_size=batch_size,
            validation_split=0.15,
            callbacks=[early_stopping],
        )

        return history


node_edges = pd.read_csv("data/connections.csv", sep="$", header=0, names=["from", "to"])

column_names = ["node_id", "fname", "distanceWidthRadius", "lineLength", "size", "thickness", "__counter__",
                "Name", "NotAesthetic", "Aesthetic", "perceivedAesthetics", "perceivedChaos", "same_counter",
                "centerX", "centerY", "direction", "inheritedOperation", "iteration", "moveRatioIteration",
                "wcurve.diagonalLength", "wcurve.distanceWidthRadius", "wcurve.lineLength", "wcurve.lineLengthHalf",
                "wcurve.moveRatio", "wcurve.size", "wcurve.thickness", "image_base64_url", "perceivedAesthetics_0",
                "perceivedAesthetics_1", "perceivedAesthetics_2", "perceivedAesthetics_3", "perceivedAesthetics_4",
                "perceivedAesthetics_5", "perceivedAesthetics_6", "perceivedAesthetics_7", "perceivedAesthetics_8",
                "perceivedAesthetics_9", "perceivedChaos_0", "perceivedChaos_1", "perceivedChaos_2",
                "perceivedChaos_3", "perceivedChaos_4", "perceivedChaos_5", "perceivedChaos_6", "perceivedChaos_7",
                "perceivedChaos_8", "perceivedChaos_9"]
feature_names = ["node_id", "Aesthetic"]
feature_names_used = ["centerX", "centerY"]
graph_nodes_all = pd.read_csv("data/drawWCurveProcessed.csv", sep="$", header=0, names=column_names)
graph_nodes = graph_nodes_all[feature_names]

class_values = sorted(graph_nodes["Aesthetic"].unique())
class_idx = {name: id for id, name in enumerate(class_values)}
nodes_fb_idx = {name: idx for idx, name in enumerate(sorted(graph_nodes["node_id"].unique()))}

# filter edges
edges_to_omit = list(set(node_edges["to"]).difference(graph_nodes["node_id"]))
edges_from_omit = list(set(node_edges["from"]).difference(graph_nodes["node_id"]))
node_edges = node_edges.drop([node_edges.index[index] for index, edge in enumerate(node_edges["to"]) if edge in edges_to_omit or edge in edges_from_omit])
node_edges = node_edges.drop([node_edges.index[index] for index, edge in enumerate(node_edges["from"]) if edge in edges_from_omit or edge in edges_to_omit])


graph_nodes["node_id"] = graph_nodes["node_id"].apply(lambda name: nodes_fb_idx[name])
node_edges["to"] = node_edges["to"].apply(lambda name: nodes_fb_idx[name])
node_edges["from"] = node_edges["from"].apply(lambda name: nodes_fb_idx[name])
graph_nodes["Aesthetic"] = graph_nodes["Aesthetic"].apply(lambda value: class_idx[value])


hidden_units = [32, 32]
learning_rate = 0.01
dropout_rate = 0.5
num_epochs = 15
batch_size = 256
num_classes = 2
edges = node_edges[["from", "to"]].to_numpy().T
# Create an edge weights array of ones.
edge_weights = tf.ones(shape=edges.shape[1])


graph_nodes = graph_nodes.join(graph_nodes_all[feature_names_used])
node_features = tf.cast(graph_nodes.sort_values("node_id")[
                            feature_names].to_numpy().astype('float32'), dtype=tf.dtypes.float32)

print("Edges shape:", edges.shape)
print("Nodes shape:", node_features.shape)

train_data, test_data = [], []

for _, group_data in graph_nodes.groupby("Aesthetic"):
    # Select around 50% of the dataset for training.
    random_selection = np.random.rand(len(group_data.index)) <= 0.5
    train_data.append(group_data[random_selection])
    test_data.append(group_data[~random_selection])

train_data = pd.concat(train_data).sample(frac=1)
test_data = pd.concat(test_data).sample(frac=1)


# Create graph info tuple with node_features, edges, and edge_weights.
graph_info = (node_features, edges, edge_weights)

print("Train data shape:", train_data.shape)
print("Test data shape:", test_data.shape)
# Create train and test features as a numpy array.
x_train = train_data[feature_names].to_numpy()
x_test = test_data[feature_names].to_numpy()
print(x_train.shape)
print(x_test.shape)
# Create train and test targets as a numpy array.
y_train = np.array([1 if aesthetics > 0.5 else 0 for aesthetics in train_data["Aesthetic"]])
y_test = np.array([1 if aesthetics > 0.5 else 0 for aesthetics in test_data["Aesthetic"]])
train_data = train_data.drop("Aesthetic", axis=1)
test_data = test_data.drop("Aesthetic", axis=1)
#print(y_train.shape)
print(num_classes)
gnn_model = GNNNodeClassifier(
    graph_info=graph_info,
    num_classes=num_classes,
    hidden_units=hidden_units,
    dropout_rate=dropout_rate,
    name="gnn_model",
)

print("GNN output shape:", gnn_model([1, 10, 100]))
gnn_model.summary()

x_train = train_data.node_id.to_numpy()
history = gnn_model.run_experiment(gnn_model, x_train, y_train, num_epochs, batch_size, learning_rate)

gnn_model.display_learning_curves(history)

x_test = test_data.node_id.to_numpy()
_, test_accuracy = gnn_model.evaluate(x=x_test, y=y_test, verbose=0)
print(f"Test accuracy: {round(test_accuracy * 100, 2)}%")
