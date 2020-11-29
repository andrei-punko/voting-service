
# Voting REST service test task

## Implement Voting REST service with next operations:

- Get candidates (should be read from json file)
- Make vote (by passport id) (no double vote allowed)
- Get voting results

## How to start:
Use `run.bat` script in project root folder

## Swagger documentation:
http://localhost:8090/swagger-ui.html

## Useful CURL commands:
- Get candidates:
`curl http://localhost:8090/candidates`

- Get votings:
`curl http://localhost:8090/votings`

- Make vote:
`curl -X POST http://localhost:8090/votings/3434 -H "Content-Type: application/json" -d "{ \"name\": \"Andrei\", \"passportId\": \"MP345353634543\"}"`
