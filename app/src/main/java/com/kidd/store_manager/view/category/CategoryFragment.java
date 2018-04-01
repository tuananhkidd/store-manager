package com.kidd.store_manager.view.category;


import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;


import com.kidd.store_manager.R;
import com.kidd.store_manager.SQLiteHelper.DBManager;
import com.kidd.store_manager.adapter.CategoryAdapter;
import com.kidd.store_manager.models.Book;
import com.kidd.store_manager.models.Category;

import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    ExpandableListView expandableListView;
    FloatingActionButton floatingActionButton;
    DBManager db;
    List<Category> categoryList;
    CategoryAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        initWidget(view);

        return view;
    }

    public void initWidget(View view) {
        expandableListView = view.findViewById(R.id.exp_category);
        floatingActionButton = view.findViewById(R.id.flb_add);
        swipeRefreshLayout = view.findViewById(R.id.refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               refreshAdapter();
            }
        });

        floatingActionButton.setOnClickListener(mOnClick);

        db = new DBManager(getActivity());

        categoryList = db.getAllCategory();
        for (Category category : categoryList) {
            category.setLsBook(getActivity(), category.getId());
        }
        adapter = new CategoryAdapter(categoryList);
        expandableListView.setAdapter(adapter);
        expandableListView.expandGroup(0);
    }

    View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_category);
            final EditText edt_title;
            Button btn_insert_category;
            edt_title = dialog.findViewById(R.id.edt_title);
            btn_insert_category = dialog.findViewById(R.id.btn_insert_category);

            btn_insert_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UUID uuid = UUID.randomUUID();
                    Category category = new Category(uuid.toString(), edt_title.getText().toString());
                    db.insertCategory(category);
                    refreshAdapter();
                    Toast.makeText(getActivity(), "Successfully!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    };

    public void refreshAdapter() {
        categoryList.clear();
        categoryList.addAll(db.getAllCategory());
        for (Category c : categoryList) {
            c.setLsBook(getActivity(), c.getId());
        }
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        expandableListView.expandGroup(0);

    }
}
