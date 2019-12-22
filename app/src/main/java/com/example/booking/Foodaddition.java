package com.example.booking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Foodaddition extends AppCompatActivity {

    EditText Editname,EditPrice;
    Button btnChoose,btnAdd,btnList;
    ImageView ImageView;

    final int REQUEST_CODE_GALLERY = 999;
    public static final int PICK_IMAGE= 1;
    public static SQLiteHelper SQLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodaddition);

        init();
        SQLiteHelper = new SQLiteHelper(this,"FoodDB.sqlite",null,1);

        SQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS FOOD(Id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR,price VARCHAR,image BLOB)");
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(Foodaddition.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        999);

            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Foodaddition.this,foodList.class);
                startActivity(intent);


            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SQLiteHelper.insertData(Editname.getText().toString().trim(),
                                            EditPrice.getText().toString().trim(),
                                            imageViewToByte(ImageView)
                    );
                    Toast.makeText(getApplicationContext(),"Added data Successfully",Toast.LENGTH_LONG).show();
                    Editname.setText("");
                    EditPrice.setText("");
                    ImageView.setImageResource(R.mipmap.ic_launcher);
                }

                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }

        });
    }
    public  static byte[] imageViewToByte(ImageView image)
    {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
        if(requestCode == PICK_IMAGE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("images/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
            }else {

                Toast.makeText(getApplicationContext(),"You Don't Have Permission to Access",Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1 && resultCode == RESULT_OK && data!= null)
        {
            Uri uri = data.getData();
            try{
               InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ImageView.setImageBitmap(bitmap);
            }catch(FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init()
    {
        Editname = (EditText)findViewById(R.id.editText2);
        EditPrice = (EditText)findViewById(R.id.editText3);
        btnAdd = (Button)findViewById(R.id.button2);
        btnChoose = (Button)findViewById(R.id.button);
        btnList = (Button)findViewById(R.id.button3);
        ImageView = (ImageView)findViewById(R.id.imageView);
    }
}
