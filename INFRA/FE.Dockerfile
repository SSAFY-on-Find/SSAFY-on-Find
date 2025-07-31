FROM node:22.17.1-alpine AS builder

WORKDIR /app

COPY FE/package*.json ./

RUN npm install

COPY FE/ ./

RUN npm run build

FROM nginx:alpine

COPY --from=builder /app/dist /usr/share/nginx/html

RUN rm /etc/nginx/conf.d/default.conf

COPY INFRA/nginx.conf /etc/nginx/conf.d/

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]