package com.example.bookrecommender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class AddBookActivity extends AppCompatActivity {

    //This is how you should define keys
    public static final String EXTRA_BOOK_ID = "com.example.bookrecommender.EXTRA_BOOK_ID";
    public static final String EXTRA_BOOK_NAME = "com.example.bookrecommender.EXTRA_BOOK_NAME";
    public static final String EXTRA_BOOK_AUTHOR = "com.example.bookrecommender.EXTRA_BOOK_AUTHOR";
    public static final String EXTRA_SWITCH_STATUS = "com.example.bookrecommender.EXTRA_SWITCH_STATUS";

    EditText bookNameEditText;
    EditText bookAuthorEditText;
    Switch bookReadStatusSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        bookNameEditText = findViewById(R.id.book_name_edit_text);
        bookAuthorEditText = findViewById(R.id.book_author_edit_text);
        bookReadStatusSwitch = findViewById(R.id.book_read_status_switch);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intendThatStartedThisActivity = getIntent();
        if (intendThatStartedThisActivity.hasExtra(EXTRA_BOOK_ID)) {
            setTitle("Edit Book");
            bookNameEditText.setText(intendThatStartedThisActivity.getStringExtra(EXTRA_BOOK_NAME));
            bookAuthorEditText.setText(intendThatStartedThisActivity.getStringExtra(EXTRA_BOOK_AUTHOR));
            bookReadStatusSwitch.setChecked(intendThatStartedThisActivity.getBooleanExtra(EXTRA_SWITCH_STATUS, false));
        } else {
            setTitle("Add Book");
        }
    }

    void saveBook() {
        String bookName = bookNameEditText.getText().toString();
        String bookAuthor = bookAuthorEditText.getText().toString();
        Boolean isSwitchChecked = bookReadStatusSwitch.isChecked();

        if (bookName.trim().isEmpty() || bookAuthor.trim().isEmpty()) {
            Toast.makeText(this, "Add name and author", Toast.LENGTH_SHORT).show();
            return;
        }
        //Not creating viewModel here but sending back the data input back to @MainActivity
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BOOK_NAME, bookName);
        intent.putExtra(EXTRA_BOOK_AUTHOR, bookAuthor);
        intent.putExtra(EXTRA_SWITCH_STATUS, isSwitchChecked);

        int id = getIntent().getIntExtra(EXTRA_BOOK_ID, -1);
        //-1 because we will never have any entry in the database with id=-1 and this way we will know
        //whenever the id=-1, it is invalid only otherwise we add id into the intent
        if (id != -1) {
            intent.putExtra(EXTRA_BOOK_ID, id);
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_book_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_book_menu_item) {
            saveBook();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
