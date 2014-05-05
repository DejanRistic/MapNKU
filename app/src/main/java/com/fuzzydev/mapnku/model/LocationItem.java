package com.fuzzydev.mapnku.model;

/**
 * Created by Dejan on 5/4/14.
 */
public class LocationItem {

    private String author;
    private String building;
    private String name;
    private String lat;
    private String lng;
    private String floor;
    private String description;
    private String mondayTime;
    private String tuesdayTime;
    private String wednesdayTime;
    private String thursdayTime;
    private String fridayTime;
    private String saturdayTime;
    private String sundayTime;
    private String date;

    public LocationItem() {
        // no arg
    }

    // Constructor needed for a custom deserializer because i made the server names without thinking.
    public LocationItem(String author, String building, String name, String lat, String lng, String floor, String description, String mondayTime, String tuesdayTime, String wednesdayTime
            , String thursdayTime, String fridayTime, String saturdayTime, String sundayTime, String date) {

        this.author = author;
        this.building = building;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.floor = floor;
        this.description = description;
        this.mondayTime = mondayTime;
        this.tuesdayTime = tuesdayTime;
        this.wednesdayTime = wednesdayTime;
        this.thursdayTime = thursdayTime;
        this.fridayTime = fridayTime;
        this.saturdayTime = saturdayTime;
        this.sundayTime = sundayTime;
        this.date = date;

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMondayTime() {
        return mondayTime;
    }

    public void setMondayTime(String mondayTime) {
        this.mondayTime = mondayTime;
    }

    public String getTuesdayTime() {
        return tuesdayTime;
    }

    public void setTuesdayTime(String tuesdauTime) {
        this.tuesdayTime = tuesdauTime;
    }

    public String getWednesdayTime() {
        return wednesdayTime;
    }

    public void setWednesdayTime(String wednesdayTime) {
        this.wednesdayTime = wednesdayTime;
    }

    public String getThursdayTime() {
        return thursdayTime;
    }

    public void setThursdayTime(String thursdayTime) {
        this.thursdayTime = thursdayTime;
    }

    public String getFridayTime() {
        return fridayTime;
    }

    public void setFridayTime(String fridayTime) {
        this.fridayTime = fridayTime;
    }

    public String getSaturdayTime() {
        return saturdayTime;
    }

    public void setSaturdayTime(String saturdayTime) {
        this.saturdayTime = saturdayTime;
    }

    public String getSundayTime() {
        return sundayTime;
    }

    public void setSundayTime(String sundayTime) {
        this.sundayTime = sundayTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
