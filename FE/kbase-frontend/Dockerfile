FROM node:22.13-alpine AS deps
WORKDIR /app
# Khuyên dùng: Copy thêm package-lock.json (nếu có) để lock phiên bản
COPY package.json package-lock.json* ./
RUN npm install

FROM node:22.13-alpine AS build
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .
RUN npm run build

FROM node:22.13-alpine
WORKDIR /app
ENV NODE_ENV=production

COPY --from=build /app/.next/standalone ./
COPY --from=build /app/.next/static ./.next/static
EXPOSE 3000
CMD ["node", "server.js"]