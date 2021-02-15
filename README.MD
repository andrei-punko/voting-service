
# Voting REST service test task
![Java CI with Maven](https://github.com/andrei-punko/voting-service/workflows/Java%20CI%20with%20Maven/badge.svg)

## Implement Voting REST service with next operations:

- Get candidates (should be read from json file)
- Make vote (by passport id) (no double vote allowed)
- Get voting results
---

## How to build:
    mvn clean install

## How to build Docker image with application inside:
    docker build ./ -t voting-service-app

## How to start:
- Use `run.bat` script in project root folder or
- Use Docker compose:  
  `docker-compose up`

## Swagger documentation:
http://localhost:8090/swagger-ui.html

## Useful CURL commands:
- Get candidates:
`curl http://localhost:8090/candidates`

- Get voting results:
`curl http://localhost:8090/votings`

- Make vote:
`curl -X POST http://localhost:8090/votings/3434 -H "Content-Type: application/json" -d "{ \"name\": \"Andrei\", \"passportId\": \"MP345353634543\"}"`

## How to run Spock functional tests:
    cd func-tests
    ./gradlew clean build

Check tests report at `./func-tests/build/spock-reports/index.html`

## How to run k6 performance tests:
Install k6 according to [manual](https://k6.io/docs/getting-started/installation).

Use next commands:
```
cd load-tests
k6 run script.js
```
