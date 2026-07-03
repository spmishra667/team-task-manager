🎨 Frontend

The frontend of Team Task Manager is built using Angular 20. It provides a clean and responsive user interface where users can manage projects and tasks. The frontend communicates with the Spring Boot backend through REST APIs and uses JWT authentication for secure login.

🚀 Technologies Used
Angular 20
TypeScript
HTML5
CSS3
Bootstrap 5
Bootstrap Icons
RxJS
Angular Router
HttpClient
✨ Features
👤 User Authentication
User Signup
User Login
JWT Authentication
Secure Logout
Protected Routes
📁 Project Management
Create Project
View All Projects
Update Project
Delete Project
✅ Task Management
Create Task
View Tasks
Update Task
Delete Task
Change Task Status
📱 Responsive UI

The application works on:

Desktop
Laptop
Tablet
Mobile

Bootstrap is used to make the application fully responsive.

🔄 How Frontend Works
User

   │

   ▼

Angular Components

   │

   ▼

Angular Services

   │

   ▼

REST API

   │

   ▼

Spring Boot Backend

   │

   ▼

PostgreSQL Database
🌐 API Configuration
Development
apiUrl = "http://localhost:8080/api"
Production
apiUrl = "https://team-task-manager-production-9bc7.up.railway.app/api"
☁️ Live Website

Frontend is deployed on Vercel.

https://team-task-manager-beta-eight.vercel.app/


▶️ Run Locally

Clone the repository

git clone https://github.com/spmishra667/team-task-manager.git

Go to the frontend folder

cd frontend

Install dependencies

npm install

Run the project

ng serve

Open in browser

http://localhost:4200
📦 Build Project
ng build

Production files are generated in:

dist/frontend/browser
📌 Highlights
Clean User Interface
Responsive Design
JWT Authentication
REST API Integration
Project Management
Task Management
Secure Login System
Production Ready Deployment



⚙️ Backend

The backend of Team Task Manager is built using Java 17 and Spring Boot 3. It provides secure REST APIs for user authentication, project management, and task management. The backend uses JWT authentication to protect APIs and PostgreSQL to store application data.

🚀 Technologies Used
Java 17
Spring Boot 3
Spring Security
Spring Data JPA
Hibernate
PostgreSQL
JWT Authentication
Maven
Lombok
✨ Features
👤 User Authentication
User Signup
User Login
JWT Token Generation
Password Encryption
Secure Authentication
📁 Project Management
Create Project
View Projects
Update Project
Delete Project
✅ Task Management
Create Task
View Tasks
Update Task
Delete Task
Update Task Status
🔐 Security
JWT Authentication
Password Encryption
Protected REST APIs
Request Validation
Stateless Authentication
🏗 Backend Flow
Client

   │

   ▼

REST Controller

   │

   ▼

Service Layer

   │

   ▼

Repository Layer

   │

   ▼

PostgreSQL Database
📡 REST APIs
Authentication
Method	Endpoint
POST	/api/auth/signup
POST	/api/auth/login
Projects
Method	Endpoint
GET	/api/projects
POST	/api/projects
PUT	/api/projects/{id}
DELETE	/api/projects/{id}
Tasks
Method	Endpoint
GET	/api/tasks
POST	/api/tasks
PUT	/api/tasks/{id}
DELETE	/api/tasks/{id}
🌐 Production API
https://team-task-manager-production-9bc7.up.railway.app
API Base URL
https://team-task-manager-production-9bc7.up.railway.app/api
☁️ Deployment

The backend is deployed on Railway.

⚙️ Environment Variables
DATABASE_URL=
PGUSER=
PGPASSWORD=
JWT_SECRET=
▶️ Run Locally

Clone the repository

git clone https://github.com/spmishra667/team-task-manager.git

Go to backend

cd backend

Run the application

mvn spring-boot:run

Backend will start on

http://localhost:8080
📌 Highlights
RESTful API
JWT Authentication
Spring Security
PostgreSQL Database
CRUD Operations
Secure User Authentication
Production Ready Deployment
Clean Layered Architecture
