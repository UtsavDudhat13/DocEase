# **Doctor Appointment App**

<img alt="Logo" src="app/src/main/res/drawable/logo.jpeg" width="120" />

## **Overview**

Doctor Appointment App is an Android application developed using Java. It allows patients to book appointments with doctors, and doctors to manage their appointments. The app features separate panels for doctors and patients, providing a seamless experience for both users.

This project was developed as a minor project during the 5th semester of college.

## **Features**

### **Patient Panel:**

* User authentication (Sign up/Login using Firebase Authentication)  
* Browse available doctors  
* Book appointments  
* View appointment history  
* Upload and manage medical records using Firebase Storage

### **Doctor Panel:**

* User authentication (Sign up/Login using Firebase Authentication)  
* Manage availability and appointments  
* View patient details  
* Access patient medical records

## **Technologies Used**

* **Java** (Primary programming language)  
* **Firebase Realtime Database** (Store and manage appointments, users, and doctor availability)  
* **Firebase Authentication** (User authentication and authorization)  
* **Firebase Storage** (Upload and retrieve medical records)  
* **Shared Preferences** (Store small amounts of user data locally)

## **Setup Instructions**

1. Clone the repository:  
   git clone https://github.com/UtsavDudhat13/DocEase.git  
2. Open the project in **Android Studio**.  
3. Connect the app to Firebase:  
   * Go to Firebase Console.  
   * Create a new project and add an Android app.  
   * Download the `google-services.json` file and place it in the `app/` directory.  
4. Enable Firebase services:  
   * Firebase Authentication (Email & Password Sign-in)  
   * Firebase Realtime Database (Set up rules to allow authenticated access)  
   * Firebase Storage (Set up rules for secure file storage)  
5. Run the application on an emulator or a physical device.

