package com.example.budgetreceipt.activities;

import com.example.budgetreceipt.adapters.UsersAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetreceipt.R;
import com.example.budgetreceipt.database.DBcatch;
import com.example.budgetreceipt.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {

    private AppCompatActivity activity = UsersListActivity.this;
    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewUsers;
    private List<User> listUsers;
    private UsersAdapter usersAdapter;
    private DBcatch dbCatch;
    private String emailFromIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        getSupportActionBar().setTitle("");

        initViews();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUsers);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listUsers = new ArrayList<>();
        usersAdapter = new UsersAdapter(listUsers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(usersAdapter);
        dbCatch = new DBcatch(activity);

        emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);

        getDataFromSQLite();
    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listUsers.clear();
                listUsers.addAll(dbCatch.getAllUser());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                usersAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
