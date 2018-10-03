package com.degree.college.Pojo;

/**
 * Created by USER on 18-05-2017.
 */

public class Event {
    private int id;
    private byte[] image;
    private String bannerImg;
    private String nam;
    private String tim;
    private String dat;
    private String time_end;
    private String place;
    private String description;
    private  String sportsType;

    public Event(byte[] image, String nam, String tim, String dat, String place) {
        this.id = id;
        this.image = image;
        this.nam = nam;
        this.tim = tim;
        this.dat = dat;
        this.place =place;
        this.description = description;
    }

    public Event(String bannerImg, String eventname, String eventdate, String start_time, String time_end, String eventVenu) {
        this.bannerImg=bannerImg;
        this.nam=eventname;
        this.tim=start_time;
        this.dat=eventdate;
        this.time_end=time_end;
        this.place=eventVenu;
    }

    public Event(String bannerImg, String sportName, String sportDate, String sportType, String address, String sportStime, String sportEtime) {
        this.bannerImg=bannerImg;
        this.nam=sportName;
        this.tim=sportStime;
        this.dat=sportDate;
        this.time_end=sportEtime;
        this.place=address;
        this.sportsType=sportType;
    }

    public String getSportsType() {
        return sportsType;
    }

    public void setSportsType(String sportsType) {
        this.sportsType = sportsType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getNam() {
        return nam;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }

    public String getTim() {
        return tim;
    }

    public void setTim(String tim) {
        this.tim = tim;
    }

    public String getDat() {
        return dat;
    }

    public void setDat(String dat) {
        this.dat = dat;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
