package com.example.temp.Models;

public class MemberExtend extends Member{
    private String packageName;
    private String startDate;
    private String endDate;
    private int exp;

    public MemberExtend(int customerID, String name, String phone, String gender, int age,
                          String packageName, String startDate, String endDate, int exp) {
        super(customerID, name, phone, gender, age);
        this.packageName = packageName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.exp = exp;
    }

    public String getPackageName() { return packageName; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public int getExp() { return exp; }

}
