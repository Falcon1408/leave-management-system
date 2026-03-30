# 🏢 Leave Management System

A full-stack Leave Management System built using **Spring Boot** and **React (Vite)** that automates employee leave application, approval workflows, and leave tracking within an organization.

> ⚠️ Note: LMS here refers to *Leave Management System*, not Learning Management System.

---

# 🚀 Features

## 👨‍💼 Employee

* Apply for leave
* View leave balance
* View leave history
* Cancel leave requests (before approval)

## 👨‍💻 Manager

* View team leave requests
* Approve or reject leave requests
* Add remarks for approval/rejection

## 🧑‍💼 Admin (HR)

* Manage employees
* Configure leave types (Casual, Sick, etc.)
* Manage holidays
* View organization-wide reports

---

# 🔐 Authentication & Security

* JWT-based authentication
* Role-based access control (EMPLOYEE, MANAGER, ADMIN)
* Password encryption using BCrypt
* Secure REST APIs

---

# 🏗 Project Structure

```
leave-management-system/
├── frontend/   → React (Vite) client
├── backend/    → Spring Boot REST API
└── README.md
```

---

# 🛠 Tech Stack

## Backend

* Java 17
* Spring Boot
* Spring Security
* Spring Data JPA (Hibernate)
* MySQL / PostgreSQL

## Frontend

* React
* Vite
* Axios
* CSS / Tailwind (if used)

---

# 📊 Key Concepts Implemented

* RESTful API design
* DTO pattern (no direct entity exposure)
* Entity relationships (One-to-Many, Many-to-One)
* Pagination & filtering
* Global exception handling (`@ControllerAdvice`)
* Transaction management
* Role-based authorization

---

# ⚙️ Setup & Installation

## 📌 Prerequisites

Make sure you have installed:

* Java 17+
* Maven
* Node.js (v16+)
* MySQL / PostgreSQL

---

## 🔧 Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend runs at:

```
http://localhost:8080
```

---

## 🎨 Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at:

```
http://localhost:5173
```

---

# 🔗 API Integration

Frontend communicates with backend using:

```
http://localhost:8080/api/
```

Ensure backend is running before using frontend.

---

# 🔑 Demo Credentials

Use these accounts to explore different roles:

### 👨‍💼 Employee

* Email: [employee@test.com](mailto:employee@test.com)
* Password: password123

### 👨‍💻 Manager

* Email: [manager@test.com](mailto:manager@test.com)
* Password: password123

### 🧑‍💼 Admin

* Email: [admin@test.com](mailto:admin@test.com)
* Password: password123

> ⚠️ These are demo credentials for testing purposes only.

---

# 🗄 Database Configuration

Update your database credentials in:

```
backend/src/main/resources/application.properties
```

Example:

```
spring.datasource.url=jdbc:mysql://localhost:3306/lms
spring.datasource.username=root
spring.datasource.password=yourpassword
```

---

# 📄 API Documentation

If Swagger is enabled:

```
http://localhost:8080/swagger-ui/
```

---

# 📸 Screenshots

*Add screenshots here (UI, dashboards, etc.)*

---

# 🎥 Demo

*Add your Loom/YouTube demo link here*

---

# ⚠️ Important Notes

* Ensure backend is running before frontend
* JWT token is required for protected endpoints
* Leave balance updates only after approval
* Holidays are excluded from leave calculation

---

# 💡 Future Improvements

* Email notifications for approvals/rejections
* File attachments for leave requests
* Dashboard analytics
* Docker deployment
* Role-based UI enhancements

---

# 🧠 What This Project Demonstrates

* Real-world enterprise backend design
* Authentication & authorization flows
* Business logic implementation (leave workflow)
* Full-stack integration
* Clean and scalable architecture

---

# 📌 Author

**Mithil Shah**

---

# ⭐ If you like this project

Give it a ⭐ on GitHub!
