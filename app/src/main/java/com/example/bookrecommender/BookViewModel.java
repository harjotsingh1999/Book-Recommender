package com.example.bookrecommender;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private BookRepository bookRepository;
    private LiveData<List<Book>> allBooks;

    public BookViewModel(@NonNull Application application) {
        super(application);
        bookRepository = new BookRepository(application);
        allBooks = bookRepository.getAllBooks();
    }


    //MainActivity only has access to the viewModel class and not to the repository
    //Hence we have to implement all database operation methods here again by ourselves
    public void insertBook(Book book) {
        bookRepository.insertBook(book);
    }

    public void deleteBook(Book book) {
        bookRepository.deleteBook(book);
    }

    public void updateBook(Book book) {
        bookRepository.updateBook(book);
    }

    public void deleteAllBooks() {
        bookRepository.deleteAllBooks();
    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }
}
