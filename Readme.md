# Spring Boot CSRF Example Project

This project is a beginner-friendly Spring Boot application designed to demonstrate **Cross-Site Request Forgery (CSRF)** protection. It uses a cookie-based CSRF token approach, a simple user interface, and clear code comments to help students understand how CSRF attacks work and how to prevent them using Spring Security.

## Table of Contents
- [What is CSRF?](#what-is-csrf)
- [How CSRF Protection Works](#how-csrf-protection-works)
- [Project Overview](#project-overview)
- [Prerequisites](#prerequisites)
- [Project Setup](#project-setup)
- [Running the Application](#running-the-application)
- [Using the Application](#using-the-application)
- [Understanding CSRF Protection in This Project](#understanding-csrf-protection-in-this-project)
- [Code Structure](#code-structure)
- [Security Features](#security-features)
- [Learning Exercises](#learning-exercises)
- [Troubleshooting](#troubleshooting)
- [Additional Resources](#additional-resources)

## What is CSRF?

**Cross-Site Request Forgery (CSRF)** is a type of attack where a malicious website tricks a user's browser into sending unauthorized requests to a trusted website where the user is authenticated. For example:
- Imagine you're logged into your bank's website.
- You visit a malicious site that has a hidden form or script.
- The malicious site sends a request to your bank's server (e.g., to transfer money) using your browser's active session.
- Since your browser is authenticated, the bank's server processes the request, thinking it came from you.

**CSRF protection** prevents this by requiring a unique token for sensitive actions (e.g., POST requests). This token is only known to the legitimate website and the user's browser, making it difficult for attackers to forge valid requests.

## How CSRF Protection Works

CSRF protection typically involves:
1. **Generating a CSRF Token**: The server creates a unique, unpredictable token for each user session and sends it to the client (e.g., in a cookie or form field).
2. **Including the Token in Requests**: The client includes the token in requests for sensitive actions (e.g., in a request header or form parameter).
3. **Server Validation**: The server checks the token in the request against the expected token for the session. If they don't match, the request is rejected.

In this project:
- The CSRF token is stored in a cookie (`XSRF-TOKEN`) that is accessible to JavaScript.
- The token is sent in a custom header (`X-XSRF-TOKEN`) for POST requests.
- Spring Security automatically validates the token for protected endpoints.

## Project Overview

This Spring Boot application includes:
- A **REST API** with protected and public endpoints to demonstrate CSRF handling.
- A **web interface** for users to log in, log out, and perform actions that require or don't require CSRF tokens.
- **Spring Security** configured with a custom cookie-based CSRF token repository.
- Clear **code comments** to explain each component and its role in CSRF protection.

### Key Features
- **Login/Logout**: Users can log in and out, with CSRF protection applied to sensitive actions.
- **Protected Endpoint**: A POST request to update a user profile requires a CSRF token.
- **Public Endpoint**: A GET request for public information does not require a CSRF token.
- **UI Flow**: The interface dynamically updates based on authentication status, making it easy to see the effects of CSRF protection.

## Prerequisites

To run this project, you need:
- **Java 17**: The project is built with Java 17.
- **Maven**: Used for dependency management and building the project.
- **Web Browser**: Any modern browser (e.g., Chrome, Firefox) to access the UI.
- **Optional**: An IDE like IntelliJ IDEA or Visual Studio Code for easier code navigation.

## Project Setup

1. **Clone or Download the Project**:
   - If using Git, clone the repository: `git clone <repository-url>`.
   - Alternatively, copy the project files into a directory named `spring-boot-csrf-enhanced`.

2. **Project Structure**:
   ```
   spring-boot-csrf-enhanced/
   ├── src/
   │   ├── main/
   │   │   ├── java/com/example/csrf/
   │   │   │   ├── CsrfEnhancedApplication.java
   │   │   │   ├── config/
   │   │   │   │   └── SecurityConfig.java
   │   │   │   ├── controller/
   │   │   │   │   ├── UserController.java
   │   │   │   │   └── CsrfController.java
   │   │   │   ├── security/
   │   │   │   │   └── CookieCsrfTokenRepository.java
   │   │   ├── resources/
   │   │   │   ├── static/
   │   │   │   │   ├── css/
   │   │   │   │   │   └── styles.css
   │   │   │   │   ├── js/
   │   │   │   │   │   └── app.js
   │   │   │   │   └── index.html
   │   │   │   └── application.properties
   ├── pom.xml
   ├── README.md
   ```

3. **Install Dependencies**:
   - Open a terminal in the project directory.
   - Run `mvn install` to download dependencies listed in `pom.xml`.

## Running the Application

1. **Start the Application**:
   - In the project directory, run:
     ```bash
     mvn spring-boot:run
     ```
   - The application will start on `http://localhost:8080`.

2. **Access the Application**:
   - Open a web browser and navigate to `http://localhost:8080`.
   - You should see a login page with options to perform public and protected actions (protected actions are visible only after login).

## Using the Application

1. **Login**:
   - Use the credentials: **Username**: `admin`, **Password**: `admin123`.
   - After successful login, the UI shows a welcome message, a logout button, and protected operations.

2. **Protected Operations**:
   - **Get Profile**: Retrieves user profile data (no CSRF token needed for GET requests).
   - **Update Profile**: Sends a POST request to update the profile, requiring a CSRF token.

3. **Public Operations**:
   - **Get Public Info**: Retrieves public information without requiring authentication or a CSRF token.

4. **Logout**:
   - Click the "Logout" button to end the session.
   - The UI reverts to the login state, and protected operations are hidden.

## Understanding CSRF Protection in This Project

### Key Components
1. **CookieCsrfTokenRepository** (`src/main/java/com/example/csrf/security/CookieCsrfTokenRepository.java`):
   - Generates a unique CSRF token for each session using a UUID.
   - Stores the token in a cookie named `XSRF-TOKEN`.
   - Makes the cookie accessible to JavaScript (`HttpOnly=false`) and secure for HTTPS (`Secure=true`).
   - Loads the token from the cookie for validation.

2. **Security Configuration** (`src/main/java/com/example/csrf/config/SecurityConfig.java`):
   - Enables CSRF protection for all endpoints except `/api/public/**`.
   - Configures form-based login and logout.
   - Specifies that logout clears the session and deletes `JSESSIONID` and `XSRF-TOKEN` cookies.

3. **Frontend JavaScript** (`src/main/resources/static/js/app.js`):
   - Reads the CSRF token from the `XSRF-TOKEN` cookie.
   - Includes the token in the `X-XSRF-TOKEN` header for POST requests (e.g., `/user/profile`, `/logout`).
   - Updates the UI based on authentication status.

4. **Controllers**:
   - `UserController` (`src/main/java/com/example/csrf/controller/UserController.java`): Handles protected and public API endpoints.
   - `CsrfController` (`src/main/java/com/example/csrf/controller/CsrfController.java`): Provides the CSRF token for debugging.

### How CSRF Protection is Implemented
- **Token Generation**: When a user accesses the application, Spring Security generates a CSRF token and stores it in the `XSRF-TOKEN` cookie.
- **Token Inclusion**: For POST requests (e.g., updating a profile or logging out), the JavaScript code reads the token from the cookie and includes it in the `X-XSRF-TOKEN` header.
- **Server Validation**: Spring Security checks the token in the request header against the session's token. If they match, the request is allowed; otherwise, a 403 Forbidden error is returned.
- **Logout Handling**: The logout request (`POST /logout`) includes the CSRF token to prevent unauthorized session termination.

### Simulating a CSRF Attack
To understand why CSRF protection is necessary:
1. Comment out the CSRF token header in the `updateProfile()` or `logout()` function in `app.js` (remove the `headers: { 'X-XSRF-TOKEN': token }` line).
2. Try the "Update Profile" or "Logout" action after logging in.
3. You should see a 403 Forbidden error, demonstrating that Spring Security rejects requests without a valid CSRF token.

## Code Structure

- **Java Code** (`src/main/java/com/example/csrf/`):
  - `CsrfEnhancedApplication.java`: Entry point for the Spring Boot application.
  - `config/SecurityConfig.java`: Configures Spring Security and CSRF protection.
  - `controller/UserController.java`: Handles user-related API endpoints.
  - `controller/CsrfController.java`: Provides CSRF token for debugging.
  - `security/CookieCsrfTokenRepository.java`: Custom CSRF token repository for cookie storage.

- **Resources** (`src/main/resources/`):
  - `static/index.html`: Main web page with login, logout, and operation buttons.
  - `static/css/styles.css`: Styles for the UI.
  - `static/js/app.js`: JavaScript for handling authentication and API requests.
  - `application.properties`: Configuration for user credentials and cookie settings.

## Security Features

- **CSRF Protection**:
  - Enabled for all non-safe HTTP methods (POST, PUT, DELETE) except public endpoints.
  - Uses a cookie-based token repository for easy frontend integration.
- **Secure Cookies**:
  - `XSRF-TOKEN` cookie is secure (`Secure=true` for HTTPS) and accessible to JavaScript (`HttpOnly=false`).
  - Session cookie (`JSESSIONID`) and CSRF token are deleted on logout.
- **Authentication**:
  - Form-based login with a default user (`admin/admin123`).
  - Logout invalidates the session and clears cookies.
- **Error Handling**:
  - Displays clear error messages for failed login, logout, or API requests.
  - UI updates dynamically based on authentication status.

## Learning Exercises

To deepen your understanding of CSRF and this project:
1. **Inspect the CSRF Token**:
   - Use your browser's developer tools (F12) to view the `XSRF-TOKEN` cookie under the "Application" tab.
   - Make a GET request to `/api/csrf-token` to see the token details.

2. **Test CSRF Protection**:
   - Modify `app.js` to omit the CSRF token for the `updateProfile()` or `logout()` function and observe the 403 error.
   - Revert the change to confirm the token fixes the issue.

3. **Explore Security Configuration**:
   - In `SecurityConfig.java`, try disabling CSRF protection (`csrf.disable()`) and observe how POST requests behave without token validation.
   - Re-enable CSRF protection to restore security.

4. **Simulate an Attack**:
   - Create a simple HTML file on your local machine with a form that submits a POST request to `http://localhost:8080/api/user/profile`.
   - Try submitting the form while logged in to the application. It should fail due to missing CSRF token.

## Troubleshooting

- **403 Forbidden on Logout or Update Profile**:
  - Ensure the `XSRF-TOKEN` cookie is present in the browser (check developer tools).
  - Verify that `app.js` includes the `X-XSRF-TOKEN` header in POST requests.
  - Check that `server.servlet.session.cookie.http-only=false` in `application.properties`.

- **Login Fails**:
  - Confirm the credentials (`admin/admin123`) are correct.
  - Check the console for errors and ensure the application is running on `http://localhost:8080`.

- **Application Doesn't Start**:
  - Verify Java 17 and Maven are installed (`java -version`, `mvn -version`).
  - Run `mvn clean install` to resolve dependency issues.

## Additional Resources

- **Spring Security CSRF Documentation**: [Spring Security CSRF](https://docs.spring.io/spring-security/reference/features/exploits/csrf.html)
- **OWASP CSRF Guide**: [OWASP CSRF](https://owasp.org/www-community/attacks/csrf)
- **Spring Boot Documentation**: [Spring Boot](https://spring.io/projects/spring-boot)
- **Understanding Cookies and Security**: [MDN Web Docs - Cookies](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies)
