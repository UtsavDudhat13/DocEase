package com.example.miniproject.models;

public class PatientModel {
    private String uid;
    private String patientName;
    private String patientEmail;
    private String password;
    private String mobileNo;
    private String dateOfBirth;
    private String gender;
    private String userImage;

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public PatientModel(String uid, String patientName, String patientEmail, String password, String mobileNo, String dateOfBirth, String gender, String userImage) {
        this.uid = uid;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.password = password;
        this.mobileNo = mobileNo;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.userImage = userImage;
    }

    public PatientModel(){}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
