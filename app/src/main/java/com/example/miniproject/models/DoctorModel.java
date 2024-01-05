package com.example.miniproject.models;

public class DoctorModel {
    private String uid;
    private String doctorName;
    private String doctorEmail;
    private String password;
    private String mobileNo;
    private String dateOfBirth;
    private String gender;
    private String userImage;
    private String experience;
    private String category;

    public DoctorModel() {
    }

    public DoctorModel(String uid, String doctorName, String doctorEmail, String password, String mobileNo, String dateOfBirth, String gender, String userImage, String experience, String category) {
        this.uid = uid;
        this.doctorName = doctorName;
        this.doctorEmail = doctorEmail;
        this.password = password;
        this.mobileNo = mobileNo;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.userImage = userImage;
        this.experience = experience;
        this.category = category;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
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

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
