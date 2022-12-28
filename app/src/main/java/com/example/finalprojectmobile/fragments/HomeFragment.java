package com.example.finalprojectmobile.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.finalprojectmobile.R;
import com.example.finalprojectmobile.adapters.ItemAdapter;
import com.example.finalprojectmobile.database.ItemDB;
import com.example.finalprojectmobile.models.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ArrayList<Item> itemsArrayList;
    RecyclerView rvItems;
    SearchView searchView;
    ItemAdapter itemAdapter;
    ItemDB itemDB;
    FirebaseUser currentUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        itemDB = new ItemDB(getActivity());
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        itemsArrayList = itemDB.getAllItems(currentUser.getUid());

        rvItems = view.findViewById(R.id.rv_items);
        itemAdapter = new ItemAdapter(itemsArrayList, getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        rvItems.setLayoutManager(linearLayoutManager);
        rvItems.setAdapter(itemAdapter);

        searchView = view.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }

    private void filter(String text){
        ArrayList<Item> filteredList = new ArrayList<>();
        for (Item item : itemsArrayList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_SHORT).show();
        }
        itemAdapter.filterList(filteredList);
    }
}