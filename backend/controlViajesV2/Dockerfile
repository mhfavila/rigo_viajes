# --- ETAPA 1: CONSTRUCCIÓN (BUILD) ---
# Usamos una imagen que ya tiene Maven instalado para evitar errores
FROM maven:3.8.5-openjdk-17 AS build

# Creamos la carpeta de trabajo
WORKDIR /app

# Copiamos todo tu código al contenedor
COPY . .

# Compilamos el proyecto (saltando los tests para ir rápido y no gastar memoria)
RUN mvn clean package -DskipTests

# --- ETAPA 2: EJECUCIÓN (RUN) ---
# Usamos una imagen ligera de Java para arrancar
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiamos el archivo .jar que cocinamos en la etapa 1
COPY --from=build /app/target/*.jar app.jar

# Informamos del puerto (Render usa el 8080 internamente)
EXPOSE 8080

# COMANDO DE ARRANQUE (Optimizada para plan GRATIS)
# -Xmx350m asegura que Java no use más de 350MB de RAM (Render te da 512MB)
ENTRYPOINT ["java", "-Xmx350m", "-jar", "app.jar"]