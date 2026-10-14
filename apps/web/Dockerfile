FROM node:24.11.1-alpine3.22 AS build
WORKDIR /work
COPY package.json package-lock.json ./
COPY apps/web/package.json apps/web/package.json
RUN npm ci
COPY apps/web apps/web
RUN npm run build --workspace apps/web

FROM nginx:1.28.0-alpine
COPY --from=build /work/apps/web/dist /usr/share/nginx/html
COPY apps/web/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 8080
