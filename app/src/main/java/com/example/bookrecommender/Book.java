package com.example.bookrecommender;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "book_table")
public class Book {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String bookName;

    @ColumnInfo(name = "author")
    private String bookAuthor;

    @ColumnInfo(name="read_status")
    private int readStatus;

    @Ignore
    public Book(String bookName, String bookAuthor, int readStatus) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.readStatus = readStatus;
    }

    public Book(int id, String bookName, String bookAuthor, int readStatus) {
        this.id = id;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.readStatus = readStatus;
    }

    public int getId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setId(int id) {
        this.id = id;
    }
}
