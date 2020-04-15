package com.example.bookrecommender;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BookViewModel bookViewModel;
    private BookAdapter bookAdapter;
    private RecyclerView recyclerView;
    FloatingActionButton addBookFAB;

    public static final int ADD_BOOK_REQUEST_CODE = 1;
    public static final int EDIT_BOOK_REQUEST_CODE = 2;

    public static final int BOOK_STATUS_READ = 1;
    public static final int BOOK_STATUS_UNREAD = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBookFAB = findViewById(R.id.button_add_book);
        addBookFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //we cant just pass "this" as packageContext to the intent cuz this points to the OnClickListener context
                //hence we use MainActivity.this
                Intent addBookIntent = new Intent(MainActivity.this, AddBookActivity.class);
                //Intent will be sent and data will be received from the other activity
                //data will be received in another overridden method onActivityResult
                startActivityForResult(addBookIntent, ADD_BOOK_REQUEST_CODE);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        bookAdapter = new BookAdapter(this);
        recyclerView.setAdapter(bookAdapter);

        bookViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(BookViewModel.class);
        bookViewModel.getAllBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                //Update Recycler View here
                //everytime a change is triggered meaning everytime data in the database changes
                //our adapter is updated to the new list and refreshed
                bookAdapter.submitList(books);
            }
        });

        //Swipe to delete functionality
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(//variables directions we want to support//
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override

            //used for drag and drop functionality
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // get the position of the viewholder swiped using viewHolder.getAdapterPosition()
                //we need to get the book object at this position so as to call the viewmodel delete function
                //and for this we add a new method "getBookAt(int position)" in adapter class which will give back the Book at a position
                bookViewModel.deleteBook(bookAdapter.getBookAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Book deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        bookAdapter.setOnItemClickLister(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                Intent editBookIntent = new Intent(MainActivity.this, AddBookActivity.class);

                //we have to send the id as well for the room to figure out which entry to update
                editBookIntent.putExtra(AddBookActivity.EXTRA_BOOK_ID, book.getId());
                editBookIntent.putExtra(AddBookActivity.EXTRA_BOOK_NAME, book.getBookName());
                editBookIntent.putExtra(AddBookActivity.EXTRA_BOOK_AUTHOR, book.getBookAuthor());
                if (book.getReadStatus() == 0)
                    editBookIntent.putExtra(AddBookActivity.EXTRA_SWITCH_STATUS, false);
                else
                    editBookIntent.putExtra(AddBookActivity.EXTRA_SWITCH_STATUS, true);
                startActivityForResult(editBookIntent, EDIT_BOOK_REQUEST_CODE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_BOOK_REQUEST_CODE && resultCode == RESULT_OK) {
            String bookName = data.getStringExtra(AddBookActivity.EXTRA_BOOK_NAME);
            String bookAuthor = data.getStringExtra(AddBookActivity.EXTRA_BOOK_AUTHOR);
            boolean readStatus = data.getBooleanExtra(AddBookActivity.EXTRA_SWITCH_STATUS, false);

            int bookReadStatus = BOOK_STATUS_UNREAD;
            if (readStatus == true)
                bookReadStatus = BOOK_STATUS_READ;

            Book newBook = new Book(bookName, bookAuthor, bookReadStatus);
            bookViewModel.insertBook(newBook);
            Toast.makeText(this, "Book saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_BOOK_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddBookActivity.EXTRA_BOOK_ID, -1);
            if (id == -1) //something went wrong
            {
                Toast.makeText(MainActivity.this, "Book can't be saved", Toast.LENGTH_SHORT).show();
                return;
            }
            String bookName = data.getStringExtra(AddBookActivity.EXTRA_BOOK_NAME);
            String bookAuthor = data.getStringExtra(AddBookActivity.EXTRA_BOOK_AUTHOR);
            boolean readStatus = data.getBooleanExtra(AddBookActivity.EXTRA_SWITCH_STATUS, false);

            int bookReadStatus = BOOK_STATUS_UNREAD;
            if (readStatus == true)
                bookReadStatus = BOOK_STATUS_READ;

            Book updatedBook = new Book(bookName, bookAuthor, bookReadStatus);
            updatedBook.setId(id);
            bookViewModel.updateBook(updatedBook);
            Toast.makeText(MainActivity.this, "Book updated", Toast.LENGTH_SHORT).show();

        } else
            Toast.makeText(this, "Book not saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_books_menu_item) {
            bookViewModel.deleteAllBooks();
            Toast.makeText(MainActivity.this, "All books deleted", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

///Shift+F6 on a word to change the spelling of all its usages
// Alt+Ctrl+L to rearrange all code