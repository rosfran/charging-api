## How to test?

### Open API (Swagger) UI

Open API (Swagger) Documentation UI for the endpoints, visit http://localhost:8080/swagger-ui/index.html after running the app.

<br/>

### Postman Collection

<br/>

### API Endpoints

> **Note** <br/>
> All URIs are relative to *http://localhost:8080/api/v1*

<br/>

| Class            | Method                                          | HTTP request   | Description                                           |
|------------------|-------------------------------------------------|----------------|-------------------------------------------------------|
| *AuthController* | [**login**](http://localhost:8080/api/v1/auth)  | **POST** /auth | Authenticates users by their credentials              |
| *AuthController* | [**signup**](http://localhost:8080/api/v1/auth) | **POST** /auth | Registers users using their credentials and user info |

<br/>
<br/>

<br/>
<br/>

| Class            | Method                                                           | HTTP request          | Description                                                         |
|------------------|------------------------------------------------------------------|-----------------------|---------------------------------------------------------------------|
| *UserController* | [**findById**](http://localhost:8080/api/v1/users/{id})          | **GET** /users/{id}   | Retrieves a single user by the given id                             |
| *UserController* | [**findAll**](http://localhost:8080/api/v1/users)         | **GET** /users?page=0&size=10&sort=id,asc | Retrieves all users based on the given parameters     |
| *UserController* | [**create**](http://localhost:8080/api/v1/users)                 | **POST** /users       | Creates a new user using the given request parameters               |
| *UserController* | [**update**](http://localhost:8080/api/v1/users)                 | **PUT** /users        | Updates user using the given request parameters                     |
| *UserController* | [**updateFullName**](http://localhost:8080/api/v1/users/profile) | **PUT** /users/profile | Updates user profile by Full Name (First Name and Last Name fields) |
| *UserController* | [**deleteById**](http://localhost:8080/api/v1/users/{id})        | **DELETE** /users     | Deletes user by id                                                  |

<br/>

### Unit & Integration Tests
Unit and Integration Tests are provided for services and controllers in the corresponding packages.

<br/>
<br/>