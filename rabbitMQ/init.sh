#!/bin/sh
# FROM https://stackoverflow.com/questions/30747469/how-to-add-initial-users-when-starting-a-rabbitmq-docker-container
# Ensure the nodename doesn't change, e.g. if docker restarts.
# Important because rabbitmq stores data per node name (or 'IP')
echo 'NODENAME=rabbit@localhost' > /etc/rabbitmq/rabbitmq-env.conf
# Create Rabbitmq user
(rabbitmqctl wait --timeout 60 $RABBITMQ_PID_FILE ; \
rabbitmqctl add_user $RABBITMQ_USER $RABBITMQ_PASSWORD 2>/dev/null ; \
rabbitmqctl set_permissions -p rabbitmq guest  ".*" ".*" ".*" ; \
rabbitmqctl set_user_tags guest administrator ; \
rabbitmqctl set_permissions -p rabbitmq $RABBITMQ_USER  ".*" ".*" ".*" ; \
rabbitmq-plugins enable rabbitmq_management; \
echo "*** User '$RABBITMQ_USER' with password '$RABBITMQ_PASSWORD' completed. ***" ; \
echo "*** Log in the WebUI at port 15672 (example: http:/localhost:15672) ***") &
# $@ is used to pass arguments to the rabbitmq-server command.
# For example if you use it like this: docker run -d rabbitmq arg1 arg2,
# it will be as you run in the container rabbitmq-server arg1 arg2
rabbitmq-server $@