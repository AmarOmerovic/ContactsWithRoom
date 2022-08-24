package com.amaromerovic.contactswithroom.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.amaromerovic.contactswithroom.data.ContactDAO;
import com.amaromerovic.contactswithroom.model.Contact;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactRoomDatabase extends RoomDatabase {

    public abstract ContactDAO contactDAO();
    public static final int NUMBER_OF_THREADS = 4;

    private static volatile ContactRoomDatabase instance;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ContactRoomDatabase getInstance(final Context context) {
        if (instance == null){
            synchronized (ContactRoomDatabase.class){
                if (instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(), ContactRoomDatabase.class, "contact_database").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return instance;
    }


    public static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                ContactDAO contactDAO = instance.contactDAO();
                contactDAO.deleteAll();
            });
        }
    };


}
