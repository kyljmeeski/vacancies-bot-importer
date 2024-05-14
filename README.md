[![Java](https://img.shields.io/badge/Java-8%2B-orange)](https://www.oracle.com/java/)
![Lines of Code](https://img.shields.io/badge/lines_of_code-817-green)
![License](https://img.shields.io/badge/license-MIT-blue)

**Importer for Vacancies Bot** s a component of the Vacancies Bot application built on a microservices architecture. It reads tasks sent by [Scheduler](https://github.com/kyljmeeski/vacancies-bot-scheduler), parses [devkg.com](devkg.com) and sends vacancies to import.
## Features
- Connects to RabbitMQ to consume imports tasks.
- Parses vacancies.
- Publish vacancies to store.

## Requirements
- Java 8 or higher
- RabbitMQ server running locally or accessible via network
- Maven for building the project

## Installation
1. Clone the repository:
```shell
git clone https://github.com/kyljmeeksi/vacancies-bot-importer.git
```
2. Navigate to the project directory:
```shell
cd vacancies-bot-importer 
```
3. Build the project using Maven:
```shell
mvn clean install
```

## Configuration
Modify the following parameters in Main.java to match your RabbitMQ setup:
```java
factory.setHost("localhost");
factory.setPort(5672);
```
Ensure the RabbitMQ server is configured with the appropriate exchanges and queues as specified in the code.

## Usage
1. Run the application:
```shell
java -jar vacancies-bot-importer.jar
```

