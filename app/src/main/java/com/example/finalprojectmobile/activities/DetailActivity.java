package com.example.finalprojectmobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalprojectmobile.ImageHelper;
import com.example.finalprojectmobile.R;

public class DetailActivity extends AppCompatActivity {
    Intent mIntent;
    Bundle detailBundle;
    ImageView itemImageView;
    TextView itemNameText, itemDescText, itemQtyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mIntent = getIntent();
        detailBundle = mIntent.getExtras();

        itemImageView = findViewById(R.id.iv_item_image);
        itemNameText = findViewById(R.id.tv_name);
        itemDescText = findViewById(R.id.tv_description);
        itemQtyText = findViewById(R.id.tv_quantity);

        String itemName = detailBundle.getString("itemName", "");
        String itemDesc = detailBundle.getString("itemDesc", "");
        int itemQty = detailBundle.getInt("itemQty", 0);
        byte[] itemImage = detailBundle.getByteArray("itemImage");

        itemNameText.setText(itemName);
        itemDescText.setText(itemDesc);
        itemQtyText.setText("Quantity: " + itemQty);
        ImageHelper.setImageViewWithByteArray(itemImageView, itemImage);
    }
}