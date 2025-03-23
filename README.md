# Typeface - File Management System

This is a **full-stack application** that allows users to upload, list, download, and delete files using **Spring Boot (backend)** and **React (frontend)**. The backend integrates with **Supabase S3 buckets** for file storage.

---

## 📁 Project Structure

```
Typeface
│── typeface/          # Spring Boot backend
│── typeface-frontend/ # React frontend
│── README.md          # This file
```

---

## 🚀 Prerequisites

Before running the application, make sure you have:

- **Java 17** or later (for backend)
- **Maven** (to build the backend)
- **Node.js & npm** (for frontend)
- **Supabase account & bucket credentials**

---

# 🖥️ Backend (Spring Boot) - `typeface`

## 🛠️ Setup Backend

### 1️⃣ Navigate to the backend folder

```sh
cd typeface
```

### 2️⃣ Configure Supabase Credentials

Set your Supabase bucket credentials in `application.properties`.

**For `application.properties`:**

```properties
supabase.s3.endpoint=YOUR_SUPABASE_STORAGE_ENDPOINT
supabase.access-key=YOUR_SUPABASE_ACCESS_KEY
supabase.secret-key=YOUR_SUPABASE_SECRET_KEY
```

### 3️⃣ Install Dependencies

```sh
mvn clean install
```

### 4️⃣ Run the Backend Server

```sh
mvn spring-boot:run
```

✅ The backend will start at **`http://localhost:8080`**.

---

# 🎨 Frontend (React) - `typeface-frontend`

## 🛠️ Setup Frontend

### 1️⃣ Navigate to the frontend folder

```sh
cd ../typeface-frontend
```

### 2️⃣ Install Dependencies

```sh
npm install
```

### 3️⃣ Start the Frontend Server

```sh
npm run dev
```

✅ The frontend will start at **`http://localhost:5173`**.

---

## 🔄 How the App Works

1️⃣ **Upload a File** → Select and upload a file using the frontend.  
2️⃣ **List Files** → The frontend fetches the list of uploaded files from the backend.  
3️⃣ **Download a File** → Click on the download button next to a file.  
4️⃣ **Delete a File** → Click on the delete button to remove a file.

---

## 📌 API Endpoints (Backend)

| Method   | Endpoint    | Description                                 |
| -------- | ----------- | ------------------------------------------- |
| `POST`   | `/upload`   | Uploads a file                              |
| `GET`    | `/files`    | Lists all files                             |
| `POST`   | `/download` | Downloads a file (requires `?fileName=xyz`) |
| `DELETE` | `/delete`   | Deletes a file (requires `?fileName=xyz`)   |

---
