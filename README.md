⚙️ Job Organizer Backend

Spring Boot backend for Job Organizer App. Handles authentication, CRUD operations, and file storage.

🌐 Live Backend

Deployed on Fly.io

✨ Features

🔑 JWT Authentication: Secure login and registration.

📅 Tasks & Events API: Full CRUD for tasks, jobs, and calendar events.

📝 Notes API: Manage user notes.

📄 File Uploads: Documents handled via Cloudinary.

✉️ Email Confirmation: Send welcome email on registration.

⚡ Full CRUD: Demonstrates secure client-server architecture.

🛠 Technology Stack
Component	Technology / Service
Backend	Spring Boot, Java
Database	PostgreSQL on Neon
Storage	Cloudinary
Auth	JWT
📌 How It Works

Frontend calls backend REST APIs for all operations.

Backend validates JWT, handles CRUD, manages uploads, and sends emails.

Neon DB stores persistent data.

Cloudinary handles file storage.
