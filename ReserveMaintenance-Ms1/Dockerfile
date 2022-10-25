FROM node:alpine
WORKDIR    /usr/src/app1
COPY ./package.json .
RUN npm install
COPY . .
CMD ["npm", "start"]