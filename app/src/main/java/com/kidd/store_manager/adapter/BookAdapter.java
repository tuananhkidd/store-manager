package com.kidd.store_manager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.kidd.store_manager.R;
import com.kidd.store_manager.common.Interface.OnItemClick;
import com.kidd.store_manager.models.Book;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by TuanAnhKid on 3/31/2018.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    Context context;
    List<Book> lsBooks;
    OnItemClick click;

    public BookAdapter(Context context, List<Book> books, OnItemClick click) {
        this.context = context;
        this.lsBooks = books;
        this.click = click;
    }

    public BookAdapter(Context context, List<Book> lsBooks) {
        this.context = context;
        this.lsBooks = lsBooks;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_book, parent, false);

        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, final int position) {
        Book book = lsBooks.get(position);
        holder.txt_title.setText(book.getTitle());
        holder.txt_author.setText(book.getAuthor());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txt_price.setText(decimalFormat.format(book.getPrice()) + " $");

        holder.img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.onDeleteItem(position);
            }
        });

        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.onEditItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lsBooks.size();
    }


    class BookViewHolder extends RecyclerView.ViewHolder {

        ImageView img_logo;
        TextView txt_title;
        TextView txt_author;
        TextView txt_price;
        ImageView img_edit;
        ImageView img_del;

        public BookViewHolder(View itemView) {
            super(itemView);
            img_logo = itemView.findViewById(R.id.img_logo);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_author = itemView.findViewById(R.id.txt_author);
            txt_price = itemView.findViewById(R.id.txt_price);
            img_del = itemView.findViewById(R.id.img_del);
            img_edit = itemView.findViewById(R.id.img_edit);

        }
    }
}
