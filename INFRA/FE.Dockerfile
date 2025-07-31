FROM node:22.17.1-alpine AS builder

WORKDIR /app

COPY FE/package.json FE/package-lock.json* ./
RUN npm install

COPY FE/ .

RUN npm run build