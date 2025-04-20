// Member.java - Đã sửa từ 'status' sang 'age'
package com.example.temp.Models;


import java.time.LocalDate;

public class Member {
    private String customerID;
    private String name;
    private String phone;
    private String gender;
    private String schedule;
    private LocalDate startDate;
    private LocalDate endDate;
    private int age;

    public Member(String customerID, String name, String phone, String gender,
                  String schedule, LocalDate startDate, LocalDate endDate, int age) {
        this.customerID = customerID;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.schedule = schedule;
        this.startDate = startDate;
        this.endDate = endDate;
        this.age = age;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
