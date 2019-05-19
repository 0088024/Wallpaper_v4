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


    public ImageAdapter(Context context){
        this.context = context;
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

        InputStream inputStream = context.getResources().openRawResource(array_idSfondi[position]);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        imageView.setImageBitmap(bitmap);
        return imageView;
    }
}