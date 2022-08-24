package com.amaromerovic.contactswithroom.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.amaromerovic.contactswithroom.model.Contact;

import java.util.List;

@Dao
public interface ContactDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addContact(Contact contact);

    @Query("DELETE FROM contact_table")
    void deleteAll();

    @Query("SELECT * FROM contact_table ORDER BY id ASC")
    LiveData<List<Contact>> getAllContacts();

    @Query("SELECT * FROM contact_table WHERE contact_table.id == :id")
    LiveData<Contact> getContact(int id);

    @Update
    void updateContact(Contact contact);

    @Delete
    void deleteContact(Contact contact);
}
