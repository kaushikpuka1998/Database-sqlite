package com.example.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity<a> extends AppCompatActivity {


    private  static final int APP_REQUEST_CODE = 7115;

    public void OnClick(View view)
     {
         Toast.makeText(getApplicationContext(), "You Have Clicked", Toast.LENGTH_SHORT).show();
     }
    public void Onpress(View view)
    {
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        TextView g = findViewById(R.id.skip);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
