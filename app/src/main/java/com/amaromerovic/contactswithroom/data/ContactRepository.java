package com.amaromerovic.contactswithroom.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.amaromerovic.contactswithroom.model.Contact;
import com.amaromerovic.contactswithroom.util.ContactRoomDatabase;

import java.util.List;

public class ContactRepository {
    private final ContactDAO contactDAO;
    private final LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application) {
        ContactRoomDatabase database = ContactRoomDatabase.getInstance(application);
        contactDAO = database.contactDAO();

        allContacts = contactDAO.getAllContacts();
    }

    public LiveData<List<Contact>> getALlData() {
        return allContacts;
    }

    public void insert(Contact contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> contactDAO.addContact(contact));
    }

    public LiveData<Contact> getContact(int id){
        return contactDAO.getContact(id);
    }

    public void updateContact(Contact contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> contactDAO.updateContact(contact));
    }

    public void deleteContact(Contact contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> contactDAO.deleteContact(contact));
    }

    public void deleteAll() {
        ContactRoomDatabase.databaseWriteExecutor.execute(contactDAO::deleteAll);
    }

}
