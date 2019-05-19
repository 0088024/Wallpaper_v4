package com.example.wallpaper;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private GridView gridView;
    private ImageView imageView;
    private WallpaperManager wallpaperManager;
    private Drawable drawable;
    private ImageAdapter imageAdapter;

    public final int REQUEST_ID = 100;
    private int STATO_PERMISSION = 0;
    private int altezza,larghezza;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        imageView = findViewById(R.id.imageView);

        imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(this);


        Button bottone_impostazioni = (Button) findViewById(R.id.bottone_impostazioni);
        bottone_impostazioni.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS),
                        0);
            }
        });

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

        altezza = wallpaperManager.getDesiredMinimumHeight();
        larghezza = wallpaperManager.getDesiredMinimumWidth();
        Log.d("Wallpaper : ", "Desiderata minima altezza/larghezza = " +
                String.valueOf(altezza) + String.valueOf(larghezza)) ;

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
                    /* Dopo la prima volta che viene accettata non verrà più mostrata la finestra
                    all'utente a meno che non si rimuove a mano la permission dalle impostazioni
                    dell'emulatore o del dispositivo mobile.*/
                    Log.d("Wallpaper :", "Permission accettata");
                    updateWallpaper();

                }
                /*permission negata: disattiviamo i servizi che ne hanno bisogno*/
                else {
                    STATO_PERMISSION=-1;
                    Log.d("Wallpaper :", "Permission negata");
                }
            }

        }
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d("Wallpaper : ", "posizione cliccata" + position);

        /*imageView.setImageBitmap((int)imageAdapter.getItemId(position));*/
        imageView.setImageBitmap((Bitmap)imageAdapter.getItem(position));

        Bitmap bitmap = (Bitmap) imageAdapter.getItem(position);

        /* Se l'utente non si ricorda di aver negato l'autorizzazione e prova ad impostare uno degli sfondi
         * verrà avvisato che non lo può fare. Quindi continuerà a vedere solo e soltanto
         * l'ultimo sfondo impostato precedentemente */
        if (STATO_PERMISSION!=0){
            /* Allora informa l'utente che non può cambiare lo sfondo */
            DialogNoPermission mydialog = new DialogNoPermission();
            mydialog.show(getSupportFragmentManager(), "mydialog");
        }
        else {
            /* Puoi impostare lo sfondo selezionato */
            /*setWallpaper(this, (BitmapDrawable)drawable);*/
            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Wallpaper : ", "Eccezione in setResourse()");

            }
            Toast.makeText(this, "Wallpaper set successfully",
                    Toast.LENGTH_LONG).show();
        }
    }

}
