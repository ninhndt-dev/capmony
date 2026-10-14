FROM node:24.11.1-alpine3.22 AS deps
WORKDIR /work
COPY package.json package-lock.json ./
COPY apps/api/package.json apps/api/package.json
COPY apps/web/package.json apps/web/package.json
RUN npm ci

FROM deps AS build
COPY apps/api apps/api
RUN npm run build --workspace apps/api

FROM deps AS probe
COPY apps/api apps/api
RUN npm run build:probe --workspace apps/api
CMD ["node", "apps/api/dist/test/probe-server.js"]

FROM node:24.11.1-alpine3.22 AS runtime
ENV NODE_ENV=production
WORKDIR /work
COPY package.json package-lock.json ./
COPY apps/api/package.json apps/api/package.json
COPY apps/web/package.json apps/web/package.json
RUN npm ci --omit=dev && npm cache clean --force
COPY --from=build /work/apps/api/dist /work/apps/api/dist
USER node
EXPOSE 3000
CMD ["node", "apps/api/dist/server.js"]
