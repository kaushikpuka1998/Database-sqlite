package com.example.booking;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class foodList extends AppCompatActivity {

    GridView gridview;
    ArrayList<food> list;
    FoodListAdapter adapter ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list_activity);

        gridview = (GridView) findViewById(R.id.gridview);
        list = new ArrayList<>();
        adapter = new FoodListAdapter(this,R.layout.food_item,list);
        gridview.setAdapter(adapter);

        Cursor cursor = Foodaddition.SQLiteHelper.getData("SELECT * FROM FOOD");
        list.clear();
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String price = cursor.getString(2);
            byte[] image = cursor.getBlob(3);

            list.add(new food(id,name,price,image));
        }
        adapter.notifyDataSetChanged();

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
               CharSequence[] items = {"Update","Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(foodList.this);
                dialog.setTitle("choose An Action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item)
                    {
                        if(item == 0)
                        {
                            Cursor c = Foodaddition.SQLiteHelper.getData("SELECT id FROM FOOD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext())
                            {
                                arrID.add(c.getInt(0));
                            }
                            showDialogUpdate(foodList.this,arrID.get(position));
                        }
                        else
                        {
                            Cursor c = Foodaddition.SQLiteHelper.getData("SELECT id FROM FOOD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext())
                            {
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;

            }
        });
    }
    private void showDialogDelete(final int uid){
        AlertDialog.Builder dialogDelte = new AlertDialog.Builder(foodList.this);
        dialogDelte.setTitle("Warning");
        dialogDelte.setMessage("Are you want to delete it?");
        dialogDelte.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    Foodaddition.SQLiteHelper.deleteData(uid);
                    Toast.makeText(getApplicationContext(),"Deleted Successfully",Toast.LENGTH_LONG).show();
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                updaterefresh();

            }
        });
        dialogDelte.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelte.show();


    }

    ImageView imageViewFood;
    private void showDialogUpdate(Activity activity, final int id){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_food_activity);
        dialog.setTitle("Update");


        final EditText editName = (EditText)dialog.findViewById(R.id.editNameUpdate);
        final EditText editPrice = (EditText)dialog.findViewById(R.id.editTextpriceupdate);

        Button btnupdate = (Button)dialog.findViewById(R.id.buttonUpdate);


        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels*0.95);

        int height = (int)(activity.getResources().getDisplayMetrics().heightPixels*0.8);
        dialog.getWindow().setLayout(width,height);
        dialog.show();

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Foodaddition.SQLiteHelper.updateData(
                            editName.getText().toString().trim(),
                            editPrice.getText().toString().trim(),
                            id
                            );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Updation Successful", Toast.LENGTH_SHORT).show();
                }catch(Exception error){
                   Toast.makeText(getApplicationContext(),"Updation Error",Toast.LENGTH_LONG).show();

                }
                updaterefresh();
            }
        });

    }
    private void updaterefresh()
    {
        Cursor cursor = Foodaddition.SQLiteHelper.getData("SELECT * FROM FOOD");
        list.clear();
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String price = cursor.getString(2);
            byte[] image = cursor.getBlob(3);

            list.add(new food(id,name,price,image));
        }
        adapter.notifyDataSetChanged();
    }

}
