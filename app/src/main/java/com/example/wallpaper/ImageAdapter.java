package com.example.wallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {


    private Context context;

    private Integer[] array_idSfondi = { R.raw.neve, R.raw.alberi, R.raw.foglie, };
    private ArrayList<Bitmap> array_bitmap;


    public ImageAdapter(Context context, ArrayList<Bitmap> array_bitmap){
        this.context = context;
        this.array_bitmap = array_bitmap;
    }


    @Override
    public int getCount() {
        return array_idSfondi.length;
    }



    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return array_idSfondi[position];
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
        }
        else imageView = (ImageView) convertView;

        imageView.setImageBitmap(array_bitmap.get(position));
        return imageView;
    }
}