package com.kidd.store_manager.view.book;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.kidd.store_manager.R;
import com.kidd.store_manager.SQLiteHelper.DBManager;
import com.kidd.store_manager.adapter.BookAdapter;
import com.kidd.store_manager.common.Interface.OnItemClick;
import com.kidd.store_manager.models.Book;
import com.kidd.store_manager.models.Category;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends Fragment implements OnItemClick {

    RecyclerView rcv_book;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton floatingActionButton;
    DBManager db;
    BookAdapter adapter;
    List<Book> lsBook;
    List<Category> lsCategory;

    public BookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        initWidget(view);
        return view;
    }

    public void initWidget(View view) {
        rcv_book = view.findViewById(R.id.rcv_book);
        floatingActionButton = view.findViewById(R.id.flb_add);

        floatingActionButton.setOnClickListener(mOnClick);

        db = new DBManager(getActivity());
        lsBook = db.getAllBook();
        adapter = new BookAdapter(getActivity(), lsBook, this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_book.setLayoutManager(manager);
        rcv_book.setAdapter(adapter);

        lsCategory = db.getAllCategory();

    }

    View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setTitle("Add Book");
            dialog.setContentView(R.layout.dialog_add_book);
            final EditText edt_title;
            final EditText edt_author;
            final EditText edt_publisher;
            final EditText edt_price;
            final Spinner spinner;

            Button btn_insert_book;

            edt_title = dialog.findViewById(R.id.edt_title);
            edt_author = dialog.findViewById(R.id.edt_author);
            edt_publisher = dialog.findViewById(R.id.edt_publisher);
            edt_price = dialog.findViewById(R.id.edt_price);
            spinner = dialog.findViewById(R.id.spinner);
            btn_insert_book = dialog.findViewById(R.id.btn_insert_book);

            List<String> ls = new ArrayList<>();
            for (Category category : lsCategory) {
                ls.add(category.getTitle());
            }
            final String[] categoryID = {null};

            ArrayAdapter<String> adapter1 =
                    new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, ls);

            spinner.setAdapter(adapter1);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    categoryID[0] = lsCategory.get(i).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            btn_insert_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UUID uuid = UUID.randomUUID();
                    Book book = new Book(uuid.toString(), edt_title.getText().toString(), edt_author.getText().toString(),
                            edt_publisher.getText().toString(), Integer.parseInt(edt_price.getText().toString()));
                    db.insertBook(book, categoryID[0]);


                    lsBook.clear();
                    lsBook.addAll(db.getAllBook());
                    adapter.notifyDataSetChanged();

                    Toast.makeText(getActivity(), "Successfully!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    };


    @Override
    public void onEditItem(int pos) {
        buildDialog(pos);
    }

    @Override
    public void onDeleteItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete book");
        builder.setMessage("Are you sure to delete this book ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.deleteBook(lsBook.get(pos).getId());
                lsBook.remove(lsBook.get(pos));
                adapter.notifyDataSetChanged();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    public void buildDialog(final int pos) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("Add Book");
        dialog.setContentView(R.layout.dialog_add_book);
        final EditText edt_title;
        final EditText edt_author;
        final EditText edt_publisher;
        final EditText edt_price;
        final Spinner spinner;

        Button btn_insert_book;

        edt_title = dialog.findViewById(R.id.edt_title);
        edt_author = dialog.findViewById(R.id.edt_author);
        edt_publisher = dialog.findViewById(R.id.edt_publisher);
        edt_price = dialog.findViewById(R.id.edt_price);
        spinner = dialog.findViewById(R.id.spinner);
        btn_insert_book = dialog.findViewById(R.id.btn_insert_book);

        final Book book = lsBook.get(pos);

        edt_title.setText(book.getTitle());
        edt_author.setText(book.getAuthor());
        edt_publisher.setText(book.getPublisher());
        edt_price.setText((book.getPrice())+"");

        List<String> ls = new ArrayList<>();
        for (Category category : lsCategory) {
            ls.add(category.getTitle());
        }
        final String[] categoryID = {null};

        final ArrayAdapter<String> adapter1 =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, ls);

        spinner.setAdapter(adapter1);
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryID[0] = lsCategory.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_insert_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book.setTitle(edt_title.getText().toString());
                book.setAuthor(edt_author.getText().toString());
                book.setPublisher(edt_publisher.getText().toString());
                book.setPrice(Integer.parseInt(edt_price.getText().toString()));
                db.updateBook(book, categoryID[0]);

                adapter.notifyItemChanged(pos);
                Toast.makeText(getActivity(), "Successfully!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
