# DECISION ENGINE
Thales Reis

#### Info:

    SpringBoot 3
    JUnit5
    Java21

#### To run tests - unit and integration, run:

    mvn test

#### To run the app, run:

    mvn spring-boot:run

By default, the server will listen to the port 8080 so to start using just make a POST to:

    http://localhost:8080/decision-engine/simulate
    {
        "personalCode": "49002010987",
        "loanAmount": 5000.0,
        "loanPeriod": 12
    }

Response:

    {
        "success": true,
        "decisionAmount": 3600.0,
        "loanPeriod": 12
    }

cURL:


    curl --location 'http://localhost:8080/decision-engine/simulate' \
    --header 'Content-Type: application/json' \
    --data '{
    "personalCode": "49002010987",
    "loanAmount": 5000.0,
    "loanPeriod": 12
    }'

*You can change de app default port in the application.yaml config file*