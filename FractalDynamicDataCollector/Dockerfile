FROM python:3.11-slim
COPY ./server /python-flask
WORKDIR /python-flask

RUN apt-get -y update
RUN apt-get -y install wget
RUN apt-get -y install libvips-dev 

RUN apt-get -y install libatk1.0-0 && apt-get -y install libatk-bridge2.0-0 && apt-get -y install libdrm2 \
		&& apt-get -y install libxkbcommon0 && apt-get -y install libatspi2.0-0 \
		&& apt-get -y install libxcomposite1 && apt-get -y install libxdamage1 \
		&& apt-get -y install libxfixes3 && apt-get -y install libxrandr2 \
		&& apt-get -y install libgbm1 && apt-get -y install libasound2

RUN pip install -r requirements.txt
RUN python initialScript.py

RUN python -m playwright install

CMD [ "python", "apis/http/api/endpoints.py" ]