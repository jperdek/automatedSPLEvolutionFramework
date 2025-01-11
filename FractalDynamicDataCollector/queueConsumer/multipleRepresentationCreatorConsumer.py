import json
import logging
import os
import sys

import pika

from global_worker_configuration import DataRepresentationsClient

logger = logging.getLogger(__name__)
logger.setLevel(logging.DEBUG)
logger.addHandler(logging.StreamHandler(sys.stdout))

processed_spls = {}


def callback_func(channel, method, properties, body):
    try:
        task_configuration = json.loads(body)
        project_id = str(task_configuration["projectId"])
        logger.debug(
            ">------| Processing data creation request after SPL: "
            + project_id
            + " has been evolved."
        )
        evolved_spl_path = task_configuration["targetPath"]
        evolution_iteration = str(task_configuration["evolutionIteration"])
        if evolved_spl_path and evolved_spl_path not in processed_spls.keys():
            processed_spls[evolved_spl_path] = evolved_spl_path
            evolution_id = str(task_configuration["evolutionId"])
            evolved_script_path = str(task_configuration["evolvedScriptPath"])
            evolved_product_line_id = project_id
            previous_product_line_id = str(task_configuration["previousProductLineId"])
            variation_points_data_location = task_configuration[
                "variationPointsDataLocation"
            ]

            destination_spl_path = DataRepresentationsClient.create_all_representations(
                evolution_id,
                evolution_iteration,
                evolved_product_line_id,
                evolved_script_path,
                evolved_spl_path,
                project_id,
                variation_points_data_location,
                previous_product_line_id,
                logger,
            )
            logger.debug(
                ">------| Finished all processing tasks. Final location: "
                + str(destination_spl_path)
            )
        else:
            logger.debug("SPL has been already processed. Skipping...")
        if channel.is_open:
            logger.debug("Final OK ack is sent")
            channel.basic_ack("OK")

    except Exception as e:
        if channel.is_open:
            logger.debug("Error is thrown. Skipping..." + str(e))
            channel.basic_reject(method.delivery_tag, requeue=False)


if __name__ == "__main__":
    credentials = pika.PlainCredentials(
        username=os.getenv("CONSUMER_USER_NAME", "guest"),
        password=os.getenv("CONSUMER_USER_PASSWORD", "guest"),
    )
    connection_url = (
        "amqp://"
        + os.getenv("CONSUMER_USER_NAME", "guest")
        + ":"
        + os.getenv("CONSUMER_USER_PASSWORD", "guest")
        + "@"
        + os.getenv("RABBIT_MQ_HOST", "localhost")
        + ":"
        + "5672/rabbitmq?heartbeat="
        + str(os.getenv("RABBIT_MQ_HEARTBEAT", 0))
    )
    logger.debug("Connection url: " + connection_url)
    connection_params = pika.URLParameters(connection_url)

    connection = pika.BlockingConnection(connection_params)

    channel = connection.channel()
    queue_name = os.getenv("QUEUE_EVOLVED_SPL", "EVOLVED_SPL")
    result = channel.queue_declare(queue=queue_name)
    channel.queue_bind(
        result.method.queue, exchange=queue_name, routing_key="*.*.*.*.*"
    )

    if os.getenv("PURGE_QUEUE_ON_START", False):
        logger.debug("Purging queue: " + queue_name)
        channel.queue_purge(queue_name)
    channel.basic_consume(queue=result.method.queue, on_message_callback=callback_func)
    logger.debug(">------| Consuming started |--------<")
    channel.start_consuming()
