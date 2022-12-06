package com.example.groceree.model;

import java.io.Serializable;

public class UserModel implements Serializable {

    String profilePic, fullName, dob, pinCode, mobileNumber, password, rePassword, address;

    public UserModel(String profilePic, String fullName, String dob, String pinCode, String mobileNumber,  String address) {
        this.profilePic = profilePic;
        this.fullName = fullName;
        this.dob = dob;
        this.pinCode = pinCode;
        this.mobileNumber = mobileNumber;
        this.address = address;
    }

    public UserModel() {
    }

    public UserModel(String profilePic, String fullName, String dob, String pinCode, String mobileNumber, String password, String rePassword) {
        this.profilePic = profilePic;
        this.fullName = fullName;
        this.dob = dob;
        this.pinCode = pinCode;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.rePassword = rePassword;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }
}
