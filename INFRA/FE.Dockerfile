# Builder Stage
FROM node:22.17.1-alpine AS builder

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

# Final Stage
FROM nginx:alpine

COPY --from=builder /app/build /usr/share/nginx/html

COPY nginx/nginx.conf /etc/nginx/conf.d

RUN chmod +x /docker-entrypoint.sh

EXPOSE 80
EXPOSE 443

CMD ["nginx", "-g", "daemon off;"]