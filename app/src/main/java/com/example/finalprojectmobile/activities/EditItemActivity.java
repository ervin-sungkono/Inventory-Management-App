package com.example.finalprojectmobile.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalprojectmobile.ImageHelper;
import com.example.finalprojectmobile.R;
import com.example.finalprojectmobile.database.ItemDB;
import com.example.finalprojectmobile.models.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditItemActivity extends AppCompatActivity {
    EditText itemNameField, itemQtyField, itemDescField;
    Button uploadButton, submitButton;
    ImageView previewImage;
    ItemDB itemDB;
    FirebaseUser currentUser;
    private byte[] imageFile;

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if(data != null){
                        Uri resultImage = data.getData();
                        previewImage.setImageURI(resultImage);
                        imageFile = ImageHelper.getBytesArrayFromURI(resultImage, this);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        itemNameField = findViewById(R.id.et_item_name);
        itemQtyField = findViewById(R.id.et_item_qty);
        itemDescField = findViewById(R.id.et_item_desc);
        previewImage = findViewById(R.id.iv_preview);
        uploadButton = findViewById(R.id.upload_btn);
        submitButton = findViewById(R.id.submit_btn);

        Intent mIntent = getIntent();
        Bundle detailBundle = mIntent.getExtras();

        int itemId = detailBundle.getInt("itemId", 0);
        String itemName = detailBundle.getString("itemName", "");
        String itemDesc = detailBundle.getString("itemDesc", "");
        int itemQty = detailBundle.getInt("itemQty", 0);
        imageFile = detailBundle.getByteArray("itemImage");

        itemNameField.setText(itemName);
        itemDescField.setText(itemDesc);
        itemQtyField.setText(String.valueOf(itemQty));
        ImageHelper.setImageViewWithByteArray(previewImage, imageFile);

        uploadButton.setOnClickListener(v->{
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLaunch.launch(iGallery);
        });

        itemDB = new ItemDB(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        submitButton.setOnClickListener(v->{
            String name = itemNameField.getText().toString();
            String quantity = itemQtyField.getText().toString();
            String description = itemDescField.getText().toString();

            if(name.length() == 0 || quantity.length() == 0 || description.length() == 0){
                Toast.makeText(this, "Please fill in the name, quantity, and description", Toast.LENGTH_SHORT).show();
            } else if(name.length() < 5){
                Toast.makeText(this, "Item Name must be at least 5 characters.", Toast.LENGTH_SHORT).show();
            }else if(Integer.parseInt(quantity) <= 0){
                Toast.makeText(this, "Quantity must be greater than 0.", Toast.LENGTH_SHORT).show();
            }else{
                Item item = new Item(itemId, currentUser.getUid(), name, description, Integer.parseInt(quantity), imageFile);
                if(itemDB.updateItem(item) > 0){
                    Toast.makeText(this, "Item updated!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditItemActivity.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(this, "Fail to update item.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}