# --- ETAPA 1: CONSTRUCCIÓN (BUILD) ---
# Usamos una imagen de Maven que ya tenga Java 21
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Creamos la carpeta de trabajo
WORKDIR /app

# Copiamos todo tu código al contenedor
COPY . .

# Compilamos el proyecto (saltando los tests)
RUN mvn clean package -DskipTests

# --- ETAPA 2: EJECUCIÓN (RUN) ---
# Usamos una imagen ligera de Java 21 para arrancar
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiamos el archivo .jar generado en la etapa 1
COPY --from=build /app/target/*.jar app.jar

# Informamos del puerto
EXPOSE 8080

# COMANDO DE ARRANQUE (Optimizada para plan GRATIS)
ENTRYPOINT ["java", "-Xmx350m", "-jar", "app.jar"]