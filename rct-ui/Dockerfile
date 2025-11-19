FROM node:20.19.3-alpine3.21

ARG NODE_ENV=development
ENV NODE_ENV=${NODE_ENV}

RUN mkdir /workspace && \
   mkdir /workspace/node_modules &&  \
   mkdir /workspace/node_modules/.cache && \
   chown -R node:node /workspace/node_modules/.cache

WORKDIR /workspace

COPY . /workspace

EXPOSE 5173

RUN npm install

# CMD ["ng", "serve", "--host", "0.0.0.0"]

# ENTRYPOINT ["tail", "-f", "/dev/null"] ## use while setting up project 