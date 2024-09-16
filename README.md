<div align="center">
<h1 align="center">
<img src="https://raw.githubusercontent.com/PKief/vscode-material-icon-theme/ec559a9f6bfd399b82bb44393651661b08aaf7ba/icons/folder-markdown-open.svg" width="100" />
<br>CRM-API</h1>
<h3>◦ Simplified customer interactions. Close more deals.</h3>
<h3>◦ Developed with the software and tools below.</h3>
    
<img src="https://github.com/arthurraposodev/crm-api/actions/workflows/maven.yml/badge.svg" alt="buildBadge" />
<p align="center">
<img src="https://img.shields.io/badge/YAML-CB171E.svg?style=flat-square&logo=YAML&logoColor=white" alt="YAML" />
<img src="https://img.shields.io/badge/GitHub%20Actions-2088FF.svg?style=flat-square&logo=GitHub-Actions&logoColor=white" alt="GitHub%20Actions" />
<img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=flat-square&logo=openjdk&logoColor=white" alt="java" />
</p>
<img src="https://img.shields.io/github/license/arthurraposodev/crm-api?style=flat-square&color=5D6D7E" alt="GitHub license" />
<img src="https://img.shields.io/github/last-commit/arthurraposodev/crm-api?style=flat-square&color=5D6D7E" alt="git-last-commit" />
<img src="https://img.shields.io/github/commit-activity/m/arthurraposodev/crm-api?style=flat-square&color=5D6D7E" alt="GitHub commit activity" />
<img src="https://img.shields.io/github/languages/top/arthurraposodev/crm-api?style=flat-square&color=5D6D7E" alt="GitHub top language" />
</div>

---

## 📖 Table of Contents
- [📖 Table of Contents](#-table-of-contents)
- [📍 Overview](#-overview)
- [📦 Features](#-features)
- [📂 Repository Structure](#-repository-structure)
- [⚙️ Modules](#-modules)
- [🚀 Getting Started](#-getting-started)
    - [🔧 Installation](#-installation)
    - [🤖 Running the Application](#-running-the-application)
    - [🧪 Tests](#-tests)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)

---

## 📍 Overview

The CRM API is a production-ready Spring Boot 3 project using Spring WebMVC.
It's designed for scalable CRUD operations on customers and OAuth 2.0 users.

## 📦 Features

- Java 21 LTS
- Maven
- Spring Boot 3 with Spring 6
- Spring Data JPA
- Docker Compose With PostgreSQL for Local Development
- Automated Unit Tests
- VCS on Github
- Spring Actuator
- Spring Cloud Gateway
- Spring WebMVC
- Contract-First Swagger CodeGen with OpenAPI 3 Standard
- Github Actions CI with "Build Pass" Badge
- Readme
---
## 🚀 Getting Started

## 🔧 Installation

### Requirements

- Java JDK 21 or higher
- Maven 3.6.3 or higher

### Clone the Repository

```bash
git clone https://github.com/arthurraposodev/crm-api
```

### Build the Project
```bash
mvn clean install
```

## 🤖 Running the Application

To run the application locally (non-volatile docker compose PostgreSQL database automatically created):
```bash
mvn spring-boot:run
```

After application has started up, the REST endpoints are exposed on port 8080 according to the OpenAPI 3 standard.

### Example Postman Collection

A Postman collection with example requests is also available in the project, inside example/. Link:
[Postman Collection](example/postman_collection.json)

## Authentication

The API uses OAuth 2.0 for user authentication. To access the endpoints you must have a user account created by an admin
and client credentials to access the API.

Currently the only client approved for authentication is Postman.

### How to use Postman for OAuth 2.0

1. Open the Postman Collection and choose an endpoint.
2. Navigate to the Authorization tab.
3. Choose OAuth 2.0 from the dropdown.
4. Scroll down to Configure New Token.
5. Fill in the following fields (ask the development team for these details):
    - Token Name: Any
    - Grant Type: Authorization Code
    - Authorize Using Browser
    - Auth URL
    - Access Token URL
    - Client ID
    - Client Secret
6. Click Get New Access Token.
7. A new window will open in your browser. Log in with your user credentials.
8. After logging in, you will be redirected to postman (approve pop up and redirect) with a code.
9. Postman should autoexchange the authorization code for a usable access token.
10. Select the token from the dropdown whenever you want to make a request.

## Screenshots

### Creating a new User

### Creating a new Customer

## 📂 Repository Structure

```sh
└── com.am.crm.gateway/
    ├── security/: Security-related configurations.
```
```sh
└── com.am.crm.customer/
    ├── config/: Configuration files for the application.
    ├── domain/: Domain models and entities.
    ├── exception/: Custom exceptions and error handling logic.
    ├── features/: Application features (CQRS command/query handlers).
    ├── infrastructure/: Infrastructure related files (repository, external services).
    ├── log/: Logging configuration or aspect logic.
    ├── security/: Security-related configurations.
    ├── util/: Utility classes for general-purpose functionality.
    ├── web/: API layer (api delegate, dto mappers).
```
```sh
└── com.am.crm.user/
    ├── config/: Configuration files for the application.
    ├── exception/: Custom exceptions and error handling logic.
    ├── features/: Application features (CQRS command/query handlers).
    ├── infrastructure/: Infrastructure related files (repository, external services).
    ├── log/: Logging configuration or aspect logic.
    ├── security/: Security-related configurations.
    ├── web/: API layer (api delegate, dto mappers).
```

## ⚙️ Modules

<details closed><summary>Customer Service</summary>

| File                                                                                                                                        | Summary                                                                                                                                                                                                                                                                    |
|---------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [CustomerApiApplication.java](https://github.com/arthurraposodev/crm-api/blob/main/src/main/java/com/am/crm/customer/CustomerApiApplication.java) | The code represents the main class of a CRM customers API. It is a Spring Boot application that startups up the embedded web server and uses WebMVC. The code also scans for configuration properties and starts the application using the SpringApplication.run() method. |

</details>

---
## 🧪 Tests
Test folders follow the same structure as source folders. This project respects an 85% code coverage target.
```bash
mvn test
```

## 🤝 Contributing
Contributions are welcome! Please follow these steps:

- **[Submit Pull Requests](https://github.com/arthurraposodev/crm-api/blob/main/CONTRIBUTING.md)**: Review open PRs, and submit your own PRs.
- **[Join the Discussions](https://github.com/arthurraposodev/crm-api/discussions)**: Share your insights, provide feedback, or ask questions.
- **[Report Issues](https://github.com/arthurraposodev/crm-api/issues)**: Submit bugs found or log feature requests for arthurraposodev.

## 📄 License
```bash
This project is licensed under the MIT License - see the LICENSE.md file for details.
```
