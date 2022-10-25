FROM node:alpine
WORKDIR    /usr/src/app2
COPY ./package.json .
RUN npm install
COPY . .
CMD ["npm", "start"]