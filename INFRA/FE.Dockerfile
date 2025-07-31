FROM node:22.17.1-alpine AS builder

WORKDIR /app

# FE 폴더의 package.json 복사
COPY package*.json ./

RUN npm install

# FE 폴더 전체 복사
COPY . ./

RUN npm run build

FROM nginx:alpine

COPY --from=builder /app/dist /usr/share/nginx/html

RUN rm /etc/nginx/conf.d/default.conf

# nginx.conf 경로도 수정
COPY INFRA/nginx.conf /etc/nginx/conf.d/

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]