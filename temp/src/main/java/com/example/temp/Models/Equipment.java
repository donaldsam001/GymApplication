package com.example.temp.Models;

import java.time.LocalDate;

public class Equipment {
    private int equipmentID;
    private String equipmentName;
    private String description;
    private LocalDate repairDate;
    private boolean status;
    private String maintenanceNote;


    public Equipment() {}

    public Equipment(int id, String name, String description, LocalDate repairDate, boolean status, String maintenanceNote) {
        this.equipmentID = id;
        this.equipmentName = name;
        this.description = description;
        this.repairDate = repairDate;
        this.status = status;
        this.maintenanceNote = maintenanceNote;
    }

    public int getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(int id) {
        this.equipmentID = id;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String name) {
        this.equipmentName = name;
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMaintenanceNote() {
        return maintenanceNote;
    }

    public void setMaintenanceNote(String maintenanceNote) {
        this.maintenanceNote = maintenanceNote;
    }
}
