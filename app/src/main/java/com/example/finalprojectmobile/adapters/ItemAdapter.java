package com.example.finalprojectmobile.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.finalprojectmobile.R;
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
        setImageViewWithByteArray(holder.ivThumbnail, i.getImage());
        holder.editButton.setOnClickListener(v->{
            // Edit Button Logic
            Toast.makeText(context, "Edit Button " + position, Toast.LENGTH_SHORT).show();
        });
        holder.deleteButton.setOnClickListener(v->{
            // Delete Button Logic
            Toast.makeText(context, "Delete Button " + position, Toast.LENGTH_SHORT).show();
        });
        holder.detailButton.setOnClickListener(v->{
            // Detail Button Logic
            Toast.makeText(context, "Detail Button " + position, Toast.LENGTH_SHORT).show();
        });
    }

    public static void setImageViewWithByteArray(ImageView view, byte[] data) {
        if(data == null) return;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        view.setImageBitmap(bitmap);
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
        ImageButton editButton, deleteButton;
        Button detailButton;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            editButton = itemView.findViewById(R.id.edit_btn);
            deleteButton = itemView.findViewById(R.id.delete_btn);
            detailButton = itemView.findViewById(R.id.detail_btn);
        }
    }
}
