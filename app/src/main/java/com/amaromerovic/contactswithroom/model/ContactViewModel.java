package com.amaromerovic.contactswithroom.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amaromerovic.contactswithroom.data.ContactRepository;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    public static ContactRepository repository;
    public final LiveData<List<Contact>> allContacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);

        repository = new ContactRepository(application);
        allContacts = repository.getALlData();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

    public static void addContact(Contact contact) {
        repository.insert(contact);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<Contact> getContact(int id) {
        return repository.getContact(id);
    }

    public static void updateContact(Contact contact) {
        repository.updateContact(contact);
    }

    public static void deleteContact(Contact contact) {
        repository.deleteContact(contact);
    }

}
