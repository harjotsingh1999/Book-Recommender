package com.example.bookrecommender;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


public class BookAdapter extends ListAdapter<Book, BookAdapter.BookViewHolder> {

    //instead of keeping our list directly in our BookAdapter we will pass the list directly
    // to the superclass ListAdapter
    //private List<Book> booksList = new ArrayList<>();
    public OnItemClickListener mlistener;
    public Context mContext;

    public BookAdapter(Context context) {
        super(DIFF_CALLBACK);
        mContext=context;
    }

    private static final DiffUtil.ItemCallback<Book> DIFF_CALLBACK = new DiffUtil.ItemCallback<Book>() {
        //The below two methods are where we do the comparision logic

        @Override
        public boolean areItemsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            //we have to return true only if the two items are same
            //Does'nt mean the contents of the two have to be same
            //should be only the same entry in our database which can be found by comparing ids
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            //return true only if NOTHING in the same item changed
            //meaning the title, author, readStatus of both items is same
            return oldItem.getBookName().equals(newItem.getBookName())
                    && oldItem.getBookAuthor().equals(newItem.getBookAuthor())
                    && oldItem.getReadStatus() == newItem.getReadStatus();
        }
    };


    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bookItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);

        return new BookViewHolder(bookItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder holder, int position) {
        Book currentBook = getItem(position);
        holder.bookNameTextView.setText("Name: "+currentBook.getBookName());
        holder.authorTextView.setText("Author: "+currentBook.getBookAuthor());

        int readStatus = currentBook.getReadStatus();
        if (readStatus == 0) {
            holder.readStatusTextView.setText(R.string.book_unread);
        } else {
            holder.readStatusTextView.setText(R.string.book_read);
            holder.bookCardView.setCardBackgroundColor(Color.parseColor("#ffe0b2"));
        }
    }

//    ListAdapter will take care of this
//    @Override
//    public int getItemCount() {
//        return booksList.size();
//    }

//    public void setBooksList(List<Book> booksList) {
//        this.booksList = booksList;
//        notifyDataSetChanged();
//    }

    public Book getBookAt(int position) {
        return getItem(position);
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView bookNameTextView;
        TextView authorTextView;
        TextView readStatusTextView;
        CardView bookCardView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookNameTextView = itemView.findViewById(R.id.book_name_text_view);
            authorTextView = itemView.findViewById(R.id.author_text_view);
            readStatusTextView = itemView.findViewById(R.id.book_read_status);
            bookCardView=itemView.findViewById(R.id.book_item_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (mlistener != null && position != RecyclerView.NO_POSITION) {
                        mlistener.onItemClick(getItem(position));
                    }
                }

            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public void setOnItemClickLister(OnItemClickListener lister) {
        this.mlistener = lister;
    }

    /*notifySetDataChanged should not be used since it tell the adapter that the whole list is invalid
    so it has to drop it and redraw it from scratch resulting in inefficiency
    to get rid of this Recycler View provides other methods
    nut they take integers as inputs which are the positions where the changes happened
    but whenever our LiveData onChanged method is triggered we just get passed a whole new list
    without any position information so even if we wanted to call notifyIteminserted or notifyItemRemoved
    we wouldn't know which position to pass
    so we need another way to compare old list to the new list passed and calculate at which position the
    change happened.
    Luckily there is a convenience class that we can use called DiffUtil which contains all logic to compare
    two lists and dispatch the position information to the adapter just by providing the logic on how we want
    to compare two items and DiffUtil takes care of the rest
    Instead of directly adding this to our adapter we are using ListAdapter class instead, which Extends
    RecyclerAdapter(not the depricated ListAdapter) and already implements diffutil and thus automatically takes care of
    some of the methods for us
    It does all the comparision logic on the background thread which is very important when our list gets bigger*/
}
