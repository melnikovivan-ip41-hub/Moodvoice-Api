# Етап 1: Збірка проєкту за допомогою Maven та Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Копіюємо всі файли проєкту в контейнер
COPY . .
# Збираємо .jar файл, пропускаючи тести для швидкості
RUN mvn clean package -DskipTests

# Етап 2: Запуск готового додатка на легкій версії Java
FROM eclipse-temurin:21-jre
WORKDIR /app
# Копіюємо тільки зібраний файл з першого етапу
COPY --from=build /app/target/*.jar app.jar
# Відкриваємо порт 8080 для інтернету
EXPOSE 8080
# Команда для запуску нашого бекенду
ENTRYPOINT ["java", "-jar", "app.jar"]