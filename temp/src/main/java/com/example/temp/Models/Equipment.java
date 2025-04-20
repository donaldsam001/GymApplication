package com.example.temp.Models;

import java.time.LocalDate;

public class Equipment {
    private int id; 
    private String name;
    private String description;
    private LocalDate repairDate;
    private String status;

    public Equipment() {}

    public Equipment(int id, String name, String description, LocalDate repairDate, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.repairDate = repairDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(LocalDate repairDate) {
        this.repairDate = repairDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    



}
