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

    private Integer[] array_idSfondi = { R.raw.neve, /*R.raw.spiaggia, R.raw.wallpapers_21,*/
            R.raw.alberi, /*R.raw.colori,*/ R.raw.foglie, };
    private ArrayList<Bitmap> array_bitmap;


    public ImageAdapter(Context context){
        this.context = context;
        this.array_bitmap = new ArrayList<Bitmap>();
    }


    @Override
    public int getCount() {
        return array_idSfondi.length;
    }



    @Override
    public Object getItem(int position) {
        return array_bitmap.get(position);
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
        array_bitmap.add(bitmap);
        imageView.setImageBitmap(bitmap);
        /*imageView.setImageResource(array_idSfondi[position]);*/
        return imageView;
    }
}