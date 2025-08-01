# Builder Stage
FROM node:22.17.1-alpine AS builder

WORKDIR /app

COPY . .

RUN npm install

RUN npm run build

# Final Stage
FROM nginx:alpine

RUN rm /etc/nginx/conf.d/default.conf

COPY conf/conf.d/default.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/build /usr/share/nginx/html

EXPOSE 80
EXPOSE 443

CMD ["nginx", "-g", "daemon off;"]