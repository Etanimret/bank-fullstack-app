FROM node:18-alpine
WORKDIR /app
COPY . .
RUN npm install
RUN npm install -g @angular/cli
EXPOSE 4200
CMD ["npm", "start"]