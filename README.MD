# Voting REST service (test task)

![Java CI with Maven](https://github.com/andrei-punko/voting-service/workflows/Java%20CI%20with%20Maven/badge.svg)
[![Coverage](.github/badges/jacoco.svg)](https://github.com/andrei-punko/voting-service/actions/workflows/maven.yml)
[![Branches](.github/badges/branches.svg)](https://github.com/andrei-punko/voting-service/actions/workflows/maven.yml)

## Prerequisites

- Maven 3
- JDK 21
- K6 for performance tests (install according to [manual](https://k6.io/docs/getting-started/installation))

## Implement Voting REST service with next operations

- Get candidates (should be read from JSON file during app start)
- Make vote (by passport id) (no double vote allowed)
- Get voting results

## How to build project

    mvn clean install

## How to start application

Use `run.bat` script in project root folder or use `docker-compose`:

```
docker-compose up
```

## Swagger documentation page

http://localhost:8090/swagger-ui/index.html

## Useful CURL commands

### Get candidates

```bash
curl http://localhost:8090/candidates
```

### Get voting results

```bash
curl http://localhost:8090/votings
```

### Get voting results for particular candidate

```bash
curl http://localhost:8090/votings/3
```

### Make vote

```bash
curl -i -X POST http://localhost:8090/votings/3 \
  -H "Content-Type: application/json" \
  -d "{ \"passportId\": \"MP345353634547\"}"
```

### Delete voting results

```bash
curl -i -X DELETE http://localhost:8090/votings
```

## How to run Spock-based functional tests

```bash
cd func-test
./gradlew clean build
```

Check tests report at `./func-test/build/spock-reports/index.html`

## How to run k6-based performance tests

```bash
cd load-test
k6 run script.js
```

Check tests report in console

## YouTube video with description of the project refactoring

[Рефакторинг Spring Boot сервиса из тестового задания](https://youtu.be/esueccOEQZE)
