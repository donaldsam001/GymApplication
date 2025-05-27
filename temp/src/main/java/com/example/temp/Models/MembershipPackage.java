package com.example.temp.Models;

import javafx.beans.property.*;

public class MembershipPackage {
    private final IntegerProperty packageID = new SimpleIntegerProperty();
    private final StringProperty packageName = new SimpleStringProperty();
    private final IntegerProperty price = new SimpleIntegerProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final IntegerProperty exp = new SimpleIntegerProperty();
    private final BooleanProperty status = new SimpleBooleanProperty();

    public MembershipPackage(int packageID, String packageName, int price, String description, int exp, boolean status) {
        this.packageID.set(packageID);
        this.packageName.set(packageName);
        this.price.set(price);
        this.description.set(description);
        this.exp.set(exp);
        this.status.set(status);
    }

    public MembershipPackage(int packageID, String packageName, int exp) {
        this.packageID.set(packageID);
        this.packageName.set(packageName);
        this.exp.set(exp);
    }

    // Getters (value)
    public int getPackageID() { return packageID.get(); }
    public String getPackageName() { return packageName.get(); }
    public int getPrice() { return price.get(); }
    public String getDescription() { return description.get(); }
    public int getExp() { return exp.get(); }
    public boolean getStatus() { return status.get(); }

    // Setters (value)
    public void setPackageID(int id) { packageID.set(id); }
    public void setPackageName(String name) { packageName.set(name); }
    public void setPrice(int price) { this.price.set(price); }
    public void setDescription(String desc) { this.description.set(desc); }
    public void setExp(int exp) { this.exp.set(exp); }
    public void setStatus(boolean status) { this.status.set(status); }

    // Property getters (for TableView, bindings, etc.)
    public IntegerProperty packageIDProperty() { return packageID; }
    public StringProperty packageNameProperty() { return packageName; }
    public IntegerProperty priceProperty() { return price; }
    public StringProperty descriptionProperty() { return description; }
    public IntegerProperty expProperty() { return exp; }
    public BooleanProperty statusProperty() { return status; }

    @Override
    public String toString() {
        return packageName.get();
    }
}