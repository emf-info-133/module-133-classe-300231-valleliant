FROM nginx:alpine

COPY ./build /usr/share/nginx/html

# Copier la configuration nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Créer un fichier de vérification de l'état de santé
RUN echo "<!DOCTYPE html><html><body><h1>Le serveur fonctionne correctement!</h1></body></html>" > /usr/share/nginx/html/health.html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"] 