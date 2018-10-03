package com.degree.college.Pojo;

/**
 * Created by USER on 29-03-2018.
 */

public class Tutor {
    private String name,imageurl,mobile,email,location,subject,description;


    public Tutor(String name, String imageurl, String mobile, String email, String location, String subject, String description) {
        this.name = name;
        this.imageurl = imageurl;
        this.mobile = mobile;
        this.email = email;
        this.location = location;
        this.subject = subject;
        this.description = description;
    }

    public Tutor(String profileImg, String tutorname, String major, String address) {
        this.imageurl=profileImg;
        this.name=tutorname;
        this.subject=major;
        this.location=address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
