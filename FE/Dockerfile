FROM node:22-alpine as builder

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

FROM nginx:stable-alpine

RUN rm -rf /etc/nginx/conf.d/default.conf

COPY --from=builder /app/dist /usr/share/nginx/html

COPY --from=builder /app/nginx.conf /etc/nginx/conf.d/default.conf 

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]