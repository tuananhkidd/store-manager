package com.kidd.store_manager.view.search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.kidd.store_manager.R;
import com.kidd.store_manager.SQLiteHelper.DBManager;
import com.kidd.store_manager.adapter.BookAdapter;
import com.kidd.store_manager.models.Book;
import com.kidd.store_manager.models.Category;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener{

    RecyclerView rcv_search;
    Button btn_search;
    RadioButton rd_category;
    RadioButton rd_book;
    EditText edt_search;
    BookAdapter adapter;
    Spinner spinner;
    List<Book> lsBook;
    List<Category> lsCategory;
    List<String> lsString;
    DBManager db;
    final String[] categoryID = new String[1];


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initWidget(view);
        return view;
    }

    void initWidget(View view) {
        rcv_search = view.findViewById(R.id.rcv_search);
        btn_search = view.findViewById(R.id.btn_search);
        rd_book = view.findViewById(R.id.rd_book);
        rd_category = view.findViewById(R.id.rd_category);
        edt_search = view.findViewById(R.id.edt_search);
        btn_search.setOnClickListener(this);
        spinner = view.findViewById(R.id.spinner);

        db = new DBManager(getActivity());
        lsBook = new ArrayList<>();
        lsString = new ArrayList<>();
        lsCategory = db.getAllCategory();
        adapter = new BookAdapter(getActivity(), lsBook);
        for (Category category : lsCategory) {
            lsString.add(category.getTitle());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,lsString);
        spinner.setAdapter(arrayAdapter);
        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_search.setLayoutManager(ll);
        rcv_search.setAdapter(adapter);

        rd_book.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    spinner.setVisibility(View.GONE);
                }else {
                }
            }
        });

        rd_category.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    spinner.setVisibility(View.VISIBLE);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryID[0] = new String(lsCategory.get(i).getId());
                //Toast.makeText(getActivity(), "ID"+categoryID[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search: {
                if (rd_book.isChecked()) {
                    if (edt_search.getText().toString().isEmpty()) {
                        edt_search.setError("Empty!");
                        return;
                    }
                    lsBook.clear();
                    lsBook.addAll(db.getAllBookByTitle(edt_search.getText().toString()));
                    adapter.notifyDataSetChanged();
                }else if(rd_category.isChecked()){
                    if (edt_search.getText().toString().isEmpty()) {
                        edt_search.setError("Empty!");
                        return;
                    }


                    Log.i("Idcategory", categoryID[0]+"onClick: ");
                    lsBook.clear();
                    lsBook.addAll(db.getAllBookByCategotyAndTitle(edt_search.getText().toString(),categoryID[0]));
                    adapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }

    void searchByBook() {

    }


}
