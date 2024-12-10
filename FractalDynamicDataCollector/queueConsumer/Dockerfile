FROM python:3.11-slim

COPY . /python-consumer
WORKDIR /python-consumer

RUN pip install -r requirements.txt

CMD [ "python", "multipleRepresentationCreatorConsumer.py" ]