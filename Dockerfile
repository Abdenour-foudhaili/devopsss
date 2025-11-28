# Étape 1 : Utiliser une image Java 17
FROM eclipse-temurin:17-jdk-alpine

# Étape 2 : Créer un dossier dans le conteneur
WORKDIR /app

# Étape 3 : Copier le livrable (JAR)
COPY target/*.jar app.jar

# Étape 4 : Exposer le port si ton app utilise 8080
EXPOSE 8080

# Étape 5 : Lancer l’application
ENTRYPOINT ["java", "-jar", "app.jar"]