package com.example.booking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodListAdapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<food>  foodsList;

    public FoodListAdapter(Context context, int layout, ArrayList<food> foodsList) {
        this.context = context;
        this.layout = layout;
        this.foodsList = foodsList;
    }

    @Override
    public int getCount() {
        return foodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder{
        ImageView ImageView;
        TextView txtname,txtPrice;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = new ViewHolder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            holder.txtname = (TextView)row.findViewById(R.id.textView);
            holder.txtPrice = (TextView)row.findViewById(R.id.textView2);
            holder.ImageView = (ImageView)row.findViewById(R.id.imageView2);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();

        }
        food food = foodsList.get(position);
        holder.txtname.setText(food.getName());
        holder.txtPrice.setText(food.getPrice());

        byte[] foodImage = food.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage,0,foodImage.length);
        holder.ImageView.setImageBitmap(bitmap);

        return row;
    }
}
