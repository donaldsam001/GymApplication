package com.example.temp.Models;

public class TrainingTime {
    private int trainingTimeID;
    private int memberID; // Mã hội viên
    private String memberName;
    private String checkInTime; // Thời gian check-in
    private String checkOutTime; // Thời gian check-out
    private String note; // Ghi chú

    // Constructor
    public TrainingTime(int id, int memberID, String memberName, String checkInTime, String checkOutTime, String note) {
        this.trainingTimeID = id;
        this.memberID = memberID;
        this.memberName = memberName;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.note = note;
    }
    public TrainingTime( int memberID, String memberName, String checkInTime, String checkOutTime, String note) {
        this.memberID = memberID;
        this.memberName = memberName;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.note = note;
    }

    public int getTrainingTimeID() {
        return trainingTimeID;
    }

    public void setTrainingTimeID(int id) {
        this.trainingTimeID = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    // Getter and Setter methods
    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
