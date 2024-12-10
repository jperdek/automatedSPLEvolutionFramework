import json
import logging
import os

import pika

from queueConsumer.global_worker_configuration import DataRepresentationsClient

logger = logging.getLogger(__name__)

def callback_func(channel, method, properties, body):
    task_configuration = json.loads(body)
    evolved_spl_path = task_configuration["targetPath"]
    evolved_spl_script_path = task_configuration["evolvedScriptPath"]
    project_id = task_configuration["projectId"]
    evolution_iteration = task_configuration["evolutionIteration"]

    DataRepresentationsClient.create_all_representations(evolved_spl_path, evolved_spl_script_path, evolution_iteration, project_id, logger)


if __name__ == "__main__":
    credentials = pika.PlainCredentials(os.getenv("PROCESSING_QUEUE_USERNAME", "splManager"),
                                        os.getenv("PROCESSING_QUEUE_PASSWORD", "splManager"))
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(host=os.getenv("RABBIT_MQ_HOST", "localhost"),
                                  credentials=credentials))

    channel = connection.channel()
    result = channel.queue_declare(exclusive=True)
    channel.queue_bind(result.method.queue,
                       exchange="EVOLVED_SPL",
                       routing_key="*.*.*.*.*")

    channel.basic_consume(callback_func, result.method.queue, no_ack=True)
    channel.start_consuming()
