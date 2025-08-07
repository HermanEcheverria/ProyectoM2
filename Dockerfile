# Etapa 1: Build
FROM node:20 AS builder

WORKDIR /app

# Copiamos archivos necesarios para instalar y construir
COPY package*.json ./
COPY tsconfig*.json ./
COPY vite.config.* ./

# Copiamos el código fuente
COPY src ./src
COPY public ./public
COPY index.html ./

# Instalamos dependencias y construimos
RUN npm install
RUN npm run build

# Etapa 2: Producción con Nginx
FROM nginx:alpine

# Copiamos archivos de producción desde la etapa de build
COPY --from=builder /app/dist /usr/share/nginx/html

# Exponemos el puerto que usará Nginx
EXPOSE 80

# Comando por defecto
CMD ["nginx", "-g", "daemon off;"]
