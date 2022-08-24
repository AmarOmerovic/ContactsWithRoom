package com.amaromerovic.contactswithroom.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_table")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int id;

    @ColumnInfo(name = "Image")
    private String image;

    @ColumnInfo(name = "FirstName")
    private String firstName;

    @ColumnInfo(name = "LastName")
    private String lastName;

    @ColumnInfo(name = "Occupation")
    private String occupation;

    @ColumnInfo(name = "PhoneNumber")
    private String phoneNumber;


    public Contact(String image, String firstName, String lastName, String occupation, String phoneNumber) {
        this.image = image;
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
        this.phoneNumber = phoneNumber;
    }

    public Contact() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}