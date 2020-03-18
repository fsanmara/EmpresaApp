package edu.dam.empresaapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class LeerQRActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private String fecha;
    private String hora;
    private String idTrabajador;
    private ZBarScannerView mScannerView;

    DatabaseReference db;

    // declaramos un objeto "trabajador"
    private Trabajador trabajador;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // creamos un objeto "Trabajador", cuyo contenido será el objeto
        // "Trabajador" que creamos en la Activity "PrincipalActivity"
        // y que pasamos a esta Activity mediante putExtra
        new Trabajador();
        trabajador = getIntent().getParcelableExtra("parametro");

        // obtenemos el id del trabajador para luego pasarlo a la BBDD
        idTrabajador = trabajador.getId().toString();

        // referenciamos la BBDD
        db = FirebaseDatabase.getInstance().getReference().child("Fichajes");

        // solicitamos permiso para acceder a la cámara
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(LeerQRActivity.this, new String[] {Manifest.permission.CAMERA}, 0);

        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                       // Set the scanner view as the content view

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        /*// mostramos un sonido cuando el QR se lee, además de mostrar el Toast
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();*/

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            SimpleDateFormat hourFormat = new SimpleDateFormat("H:mm:ss", Locale.getDefault());
            Date date = new Date();
            Date hour = new Date();

            fecha = dateFormat.format(date);
            hora  = hourFormat.format(hour);

            db.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(!dataSnapshot.child(idTrabajador).child(fecha).child("hora_entrada").exists()){

                        db.child(idTrabajador).child(fecha).child("hora_entrada").setValue(hora);

                        finish();

                    } else if(dataSnapshot.child(idTrabajador).child(fecha).child("hora_entrada").exists()
                            && dataSnapshot.child(idTrabajador).child(fecha).child("hora_salida").exists()){

                        finish();

                    } else if (dataSnapshot.child(idTrabajador).child(fecha).child("hora_entrada").exists()){
                    db.child(idTrabajador).child(fecha).child("hora_salida").setValue(hora);

                        finish();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        /*} catch (Exception e) {}*/

        Toast.makeText(this, "Contents = " + rawResult.getContents() +
                ", Format = " + rawResult.getBarcodeFormat().getName() + " " + fecha + " " + hora, Toast.LENGTH_SHORT).show();

        /*Intent intent = new Intent (getApplicationContext(), FichajesActivity.class);
        intent.putExtra("parametro", trabajador); //pasamos el objeto trabajador
        startActivity(intent);*/
         // cerramos la actividad y volvemos a la pantalla anterior
        //mScannerView.stopCamera();

        // If you would like to resume scanning, call this method below:
        // esto es por si queremos seguir leyendo códigos QR
        //mScannerView.resumeCameraPreview(this);
    }
}