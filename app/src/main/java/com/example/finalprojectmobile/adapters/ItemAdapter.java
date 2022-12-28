package com.example.finalprojectmobile.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectmobile.ImageHelper;
import com.example.finalprojectmobile.R;
import com.example.finalprojectmobile.activities.DetailActivity;
import com.example.finalprojectmobile.activities.EditItemActivity;
import com.example.finalprojectmobile.database.ItemDB;
import com.example.finalprojectmobile.models.Item;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.viewHolder>{
    private ArrayList<Item> itemsArrayList;
    private Context context;
    ItemDB itemDB;

    public ItemAdapter(ArrayList<Item> itemsArrayList, Context context) {
        this.itemsArrayList = itemsArrayList;
        this.context = context;
        this.itemDB = new ItemDB(context.getApplicationContext());
    }

    @NonNull
    @Override
    public ItemAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.viewHolder holder, int position) {
        Item i = itemsArrayList.get(position);
        holder.tvName.setText(i.getName());
        holder.tvQuantity.setText("Qty: " + i.getQuantity());
        ImageHelper.setImageViewWithByteArray(holder.ivThumbnail, i.getImage());

        Bundle itemBundle = new Bundle();
        itemBundle.putInt("itemId", i.getId());
        itemBundle.putString("itemName", i.getName());
        itemBundle.putString("itemDesc", i.getDescription());
        itemBundle.putInt("itemQty", i.getQuantity());
        itemBundle.putByteArray("itemImage", i.getImage());

        holder.detailButton.setOnClickListener(v->{
            Intent detailIntent = new Intent(context, DetailActivity.class);
            detailIntent.putExtras(itemBundle);
            context.startActivity(detailIntent);
        });

        holder.editButton.setOnClickListener(v -> {
            Intent updateIntent = new Intent(context, EditItemActivity.class);
            updateIntent.putExtras(itemBundle);
            context.startActivity(updateIntent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder
                    .setTitle("Delete \"" + i.getName() + "\" from your inventory?")
                    .setCancelable(false)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        if(itemDB.deleteItem(i.getId()) > 0){
                            itemsArrayList.remove(position);
                            Toast.makeText(context, "Delete success!", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(context, "Fail to delete item.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    public void filterList(ArrayList<Item> filteredList){
        itemsArrayList = filteredList;
        notifyDataSetChanged();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvQuantity;
        ImageView ivThumbnail;
        Button detailButton;
        ImageButton editButton, deleteButton;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            detailButton = itemView.findViewById(R.id.detail_btn);
            editButton = itemView.findViewById(R.id.edit_btn);
            deleteButton = itemView.findViewById(R.id.delete_btn);
        }
    }
}
