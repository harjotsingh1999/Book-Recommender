package com.example.bookrecommender;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Book.class, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {

    private static BookDatabase databaseInstance;

    public abstract BookDao bookDao();

    private static final String DATABASE_FILE_NAME="books_database";

    public static synchronized BookDatabase getInstance(Context context)
    {
        if (databaseInstance == null){
            databaseInstance= Room.databaseBuilder(context.getApplicationContext()
                    ,BookDatabase.class
                    , BookDatabase.DATABASE_FILE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return databaseInstance;
    }
}
