FROM node:alpine
					
WORKDIR /usr/app

COPY ./package.json ./
RUN npm install
COPY ./ ./

RUN npm install
COPY ./changedDefaults/BabelParser.js ./node_modules/@typhonjs/babel-parser/dist/BabelParser.js   
COPY ./changedDefaults/eslint-patches.js ./node_modules/eslintcc/source/lib/eslint-patches.js
CMD ["npm","start"]