FROM oven/bun:1.1.42
WORKDIR /frontend

COPY package.json .
COPY bun.lockb .
RUN bun install --frozen-lockfile

COPY . .
# [optional] tests & build
ARG VITE_API_BASE_URL
ARG NODE_ENV
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
ENV NODE_ENV=$NODE_ENV
RUN bun run build

EXPOSE 3000/tcp
