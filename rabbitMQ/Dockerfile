FROM rabbitmq

ARG consumer_user_name=consumerUser
ARG consumer_user_password=consumerUser


# Define environment variables.
ENV RABBITMQ_USER=$consumer_user_name
ENV RABBITMQ_PASSWORD=$consumer_user_password
ENV RABBITMQ_PID_FILE=/var/lib/rabbitmq/mnesia/rabbitmq

COPY ./init.sh /rabbitMQ/init.sh
RUN chmod +x /rabbitMQ/init.sh
WORKDIR /rabbitMQ
EXPOSE 15672
EXPOSE 5672


RUN apt-get update
RUN apt-get install -y wget
# Define default command
CMD ["sh", "/rabbitMQ/init.sh"]