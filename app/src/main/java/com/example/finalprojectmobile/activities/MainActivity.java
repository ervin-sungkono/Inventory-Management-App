package com.example.finalprojectmobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.finalprojectmobile.R;
import com.example.finalprojectmobile.adapters.ItemAdapter;
import com.example.finalprojectmobile.database.ItemDB;
import com.example.finalprojectmobile.models.Item;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Item> itemsArrayList;
    RecyclerView rvItems;
    ItemAdapter itemAdapter;
    ItemDB itemDB;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> {
        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(authStateListener);

        itemDB = new ItemDB(this);

        itemsArrayList = new ArrayList<>();
        itemsArrayList.add(new Item(
                1,
                null,
                "Test Item",
                "Just another description",
                2,
                null
        ));
        itemsArrayList.add(new Item(
                2,
                null,
                "Test Item 2",
                "Just another description",
                2,
                null
        ));
        itemsArrayList.add(new Item(
                3,
                null,
                "Test Item 3",
                "Just another description",
                2,
                null
        ));

        rvItems = findViewById(R.id.rv_items);
        itemAdapter = new ItemAdapter(itemsArrayList, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rvItems.setLayoutManager(linearLayoutManager);
        rvItems.setAdapter(itemAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout){
            mAuth.signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}