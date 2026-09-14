# JournalApp Backend 📝

A backend REST API for a personal journal application, built with **Java, Spring Boot, and MongoDB**.

The application allows users to securely manage their journal entries, with user authentication and persistent data storage using MongoDB.

## 🛠️ Tech Stack

* **Java**
* **Spring Boot**
* **Spring Security**
* **JWT**
* **Spring Data MongoDB**
* **MongoDB**
* **Maven**
* **REST API**

## 🚀 Features

* User registration and authentication
* Secure login and logout
* JWT-based authentication
* Create journal entries
* View journal entries
* Update journal entries
* Delete journal entries
* User-specific journal data
* MongoDB database integration
* RESTful API architecture
* Health-check endpoint
* Environment-based configuration
* Exception handling and API validation

### Authentication 🔗 API Endpoints

| Method | Endpoint              | Description         |
| ------ | --------------------- | ------------------- |
| `POST` | `/public/signup`      | Register a new user |
| `POST` | `/public/login`       | Authenticate user   |


## ⚙️ Prerequisites

Before running the project, make sure you have:

* Java 8+ installed
* Maven installed
* MongoDB installed locally or a MongoDB Atlas database
* Git installed

## 🔐 Authentication

Protected endpoints require the user to be authenticated.

After successful login, the client receives an authentication token which is sent with subsequent requests.

The backend validates the token before allowing access to protected resources.

## 🗄️ Database

The application uses **MongoDB** for storing users and journal entries.

User data and journal entries are associated so that users can access only their own journal data.

## 🌐 Deployment

The backend can be deployed to platforms such as:

* Render
* Railway
* AWS
* Azure
* Google Cloud
* Any platform supporting Java/Spring Boot applications

## 🔒 Security

* Passwords should never be stored in plain text.
* Authentication credentials should be stored securely.
* Secrets should not be committed to the repository.
* Use environment variables for production credentials.
* Configure CORS appropriately for the frontend application.
* Protected endpoints should require authentication.
