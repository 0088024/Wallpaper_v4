package com.example.wallpaper;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private GridView gridView;
    private ImageView imageView;
    private WallpaperManager wallpaperManager;
    private Drawable drawable;
    private ImageAdapter imageAdapter;

    public final int REQUEST_ID = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        imageView = findViewById(R.id.imageView);

        imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(this);


        /*int checkSelfPermission (Context context, String permission).
        Determina se ti è stata concessa una particolare autorizzazione.
        Dove permission =  The name of the permission being checked.
        Return = PERMISSION_GRANTED if you have the permission, or PERMISSION_DENIED if not.

        PackageManager : class for retrieving various kinds of information related to the
        application packages that are currently installed on the device.You can find this class
        through Context#getPackageManager*/

        /*Controllo se l'app ha il permesso: se non lo ha*/
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            /*Ottiene se dovresti mostrare la UI con motivazioni per la richiesta di
            un'autorizzazione. Dovresti farlo solo se non hai il permesso e il contesto in cui
            viene richiesta l'autorizzazione non comunica chiaramente all'utente quale sarebbe
            il beneficio dal concedere questa autorizzazione.*/
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    /*mostriamo una finestra di dialogo che spiega perchè l'app ha bisogno della
                    permission e successivamente ne chiediamo l'accettazione*/
                    Log.d("Wallpaper :", "shouldShowRequestPermissionRationale() == true");
                Log.d("Wallpaper :", "requestPermission()");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_ID);

            }
            /*altrimenti viene proposto all'utente di accettare la permission*/
            else {
                /*Requests permissions to be granted to this application. These permissions must be
                requested in your manifest, they should not be granted to your app, and they should
                have protection level #PROTECTION_DANGEROUS dangerous
                If your app does not have the requested permissions the user will be presented with
                UI for accepting them. After the user has accepted or rejected the requested
                permissions you will receive a callback reporting whether the permissions were
                granted or not. Your activity has to implement
                ActivityCompat.OnRequestPermissionsResultCallback and the results of permission
                requests will be delivered to its
                onRequestPermissionsResult(int, String[], int[]) method.*/
                Log.d("Wallpaper :", "requestPermission()");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_ID);
            }
        }

        else{
            Log.d("Wallpaper", "Permesso già concesso");
            updateWallpaper();

        }

        /*Tale codice verrà eseguito solo se ci troviamo in Android 6: in tutte le altre
        versioni la permission verrà considerata concessa.*/

        /*Con il metodo requestPermissions verrà aperta la finestra di dialogo con cui
        l’utente accetta o meno le permission. La risposta sarà inoltrata al metodo
        onRequestPermissionsResult all’interno del quale riconosceremo la richiesta grazie
        al parametro REQUEST_ID usato:*/


    }



    private void updateWallpaper() {

        /*qui l'indianino metteva getApllicationContext() invece che this*/
        /*getInstance() recupera 1 WallpaperManager associato con il dato contesto*/
        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());




        /*Returns the desired minimum height for the wallpaper. Callers of
        setBitmap(android.graphics.Bitmap) or setStream(java.io.InputStream) should check this
        value beforehand to make sure the supplied wallpaper respects the desired minimum height.
        If the returned value is <= 0, the caller should use the height of the default display
        instead.
        Return: The desired minimum height for the wallpaper. This value should be honored by
        applications that set the wallpaper but it is not mandatory/obbligtorio.*/
        int altezza = wallpaperManager.getDesiredMinimumHeight();
        int larghezza = wallpaperManager.getDesiredMinimumWidth();
        Log.d("Wallpaper : ", "Desiderata minima altezza/larghezza = " +
                String.valueOf(altezza) + "/" + String.valueOf(larghezza)) ;

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

        /*imposta il wallpaper corrente come contenuto di questa ImageView*/
        imageView.setImageDrawable(drawable);


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*permission accettata: possiamo attivare il codice*/
                    Log.d("Wallpaper :", "Permission accettata");

                    updateWallpaper();

                }
                /*permission negata: disattiviamo i servizi che ne hanno bisogno*/
                else {
                    Log.d("Wallpaper :", "Permission negata");
                }
            }

        }
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d("Wallpaper : ", "posizione cliccata" + position);

        imageView.setImageResource((int)imageAdapter.getItemId(position));

        try {
            wallpaperManager.setResource((int)imageAdapter.getItemId(position));
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Wallpaper : ", "Errore in WallpaperManager.setResource()");

        }
    }

}
