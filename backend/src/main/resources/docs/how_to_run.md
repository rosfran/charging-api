## How to run?

The application can be run in development or production mode by applying the following steps.
<br/>

### Prerequisites

The following apps should be installed before running the application:

- A command line app
- Docker Desktop 

> **Note** <br/>
> For more information regarding the system requirements, etc. refer to the following pages: <br/>
> [Install on Mac](https://docs.docker.com/desktop/install/mac-install/)<br/>
> [Install on Windows](https://docs.docker.com/desktop/install/windows-install/)<br/>
> [Install on Linux](https://docs.docker.com/desktop/install/linux-install/)<br/>

<br/>

### Running the app - Backend and Frontend

In order to run the application in development mode, apply the following steps:

1. Run Docker desktop.

<br/>

2. Open command prompt window and clone the project from GitHub using the following command:

```
git clone https://gitlab.com/fastned-recruitment/backend/19042023-rosfran-lins-borges.git
```
<br/>

3. Change the current directory to the project directory where the `docker-compose.yml` file is in:

```
cd fast-charging
```
<br/>

4. Run the following command:

```
docker compose -f docker-compose.yml up --build
```

<br/>

Then the Frontend application written in React starts on http://localhost:3000/ and will be opened on your default browser. At this step, the following accounts can be used for logging in to the application.
Alternatively, API requests can be sent to the endpoints using Postman, etc. For this purpose, see the details on [How to test?](how_to_test.md) section.
<br/>

### User Account
```
username: johndoe
password: johnd@e
role: admin

```

<br/>

<br/>

By running this command, the app and database containers are built and start up. After this step is completed, the application will be available on http://localhost:3000 and the accounts given in the "User Accounts" section can be used for logging in to the application. 
Alternatively, API requests can be sent to the endpoints using Postman, etc. For this purpose, see the details on [How to test?](how_to_test.md) section. 
<br/>

For connecting to the application database, the following url and credentials given in the `.env` file can be used. 

```
url: jdbc:postgresql://localhost:5432/<${DB_NAME}>
```

<br/>

### Troubleshooting

If there is any process using the same port of the application, _"ports are not available"_ or _"port is already in use"_ errors might be encountered. 
In this situation, terminating that process and restarting the related containers will fix the problem. If the problem continues, 
delete the containers (db, backend and frontend) and re-run the `docker compose` command in the previous step. 

<br/>

### Documentation

[docker compose up](https://docs.docker.com/engine/reference/commandline/compose_up/)<br/>


<br/>
<br/>