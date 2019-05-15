package com.example.wallpaper;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private GridView gridView;
    private ImageView imageView;
    private WallpaperManager wallpaperManager;
    private Drawable drawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        imageView = findViewById(R.id.imageView);

        updateWallpaper();

    }


    private void updateWallpaper() {

        /*qui l'indianino metteva getApllicationContext() invece che this*/
        /*getInstance() recupera 1 WallpaperManager associato con il dato contesto*/
        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());



        /*Retrieve the current system wallpaper; if no wallpaper is set, the system built-in
        (incorporato) static wallpaper is returned. This is returned as an abstract Drawable that
        you can install in a View to display whatever/qualsiasi wallpaper the user has currently
        set.
        Returns a Drawable object that will draw the system wallpaper, or null if no system
        wallpaper exists or if the calling application is not able to access the wallpaper.
        This method can return null if there is no system wallpaper available, if wallpapers are not
        supported in the current user, or if the calling app is not permitted to access the system
        wallpaper.*/
        drawable = wallpaperManager.getDrawable();
        if(drawable == null) {
            Log.d("Wallpaper : ", "Drawable = null");

        }

        /*imposta 1 drawable come contenuto di questa ImageView*/
        imageView.setImageDrawable(drawable);


        try {
            wallpaperManager.setResource(R.raw.lloyd_thumbs);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Wallpaper : ", "Errore in setResource()");

        }


    }


}
