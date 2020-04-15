package com.example.bookrecommender;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {

    private BookDao bookDao;
    private LiveData<List<Book>> allBooks;

    public BookRepository(Application application)
    {
        BookDatabase bookDatabase=BookDatabase.getInstance(application);
        bookDao=bookDatabase.bookDao();
        allBooks=bookDao.getAllBooks();
    }

    public void insertBook(Book book)
    {
        new InsertBookAsyncTask( bookDao).execute(book);
    }
    public void updateBook(Book book)
    {
        new UpdateBookAsyncTask(bookDao).execute(book);
    }
    public void deleteBook(Book book)
    {
        new DeleteBookAsyncTask(bookDao).execute(book);
    }
    public void deleteAllBooks()
    {
        new DeleteAllBooksAsyncTask(bookDao).execute();
    }

    public LiveData<List<Book>> getAllBooks()
    {
        return allBooks;
    }

    private static class InsertBookAsyncTask extends AsyncTask<Book, Void, Void>
    {
        private BookDao bookDao;

        public InsertBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.addBook(books[0]);
            return null;
        }
    }
    private static class UpdateBookAsyncTask extends AsyncTask<Book, Void, Void>
    {
        private BookDao bookDao;

        public UpdateBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.updateBook(books[0]);
            return null;
        }
    }
    private static class DeleteBookAsyncTask extends AsyncTask<Book, Void, Void>
    {
        private BookDao bookDao;

        public DeleteBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.deleteBook(books[0]);
            return null;
        }
    }

    private static class DeleteAllBooksAsyncTask extends AsyncTask<Void, Void, Void> {
        private BookDao bookDao;

        public DeleteAllBooksAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bookDao.deleteAllBooks();
            return null;
        }
    }
}
