package com.example.test.HelperClasses;


public class UserHelperClass
{
    String fullName, address, username, profileImg, date, gender, phoneNo;
    public UserHelperClass() {}

    public UserHelperClass(String fullName, String address, String username, String profileImg, String date, String gender, String phoneNo) {
        this.fullName = fullName;
        this.address = address;
        this.username = username;
        this.profileImg = profileImg;
        this.date = date;
        this.gender = gender;
        this.phoneNo = phoneNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
