package edu.dam.empresaapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

    // declaramos la referencia la BBDD
    DatabaseReference db;

    // declaramos un objeto Thread
    Thread thread;

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

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            SimpleDateFormat hourFormat = new SimpleDateFormat("H:mm:ss", Locale.getDefault());
            Date date = new Date();
            Date hour = new Date();

            fecha = dateFormat.format(date);
            hora  = hourFormat.format(hour);

            db.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // si el child "hora_entrada" para una fecha x no existe,
                    // guardamos la hora de entrada en la BBDD
                    if(!dataSnapshot.child(idTrabajador).child(fecha).child("hora_entrada").exists()){

                        db.child(idTrabajador).child(fecha).child("hora_entrada").setValue(hora);

                        // mostramos un ventana de diálogo informativa
                        AlertDialog.Builder ventana = new AlertDialog.Builder(LeerQRActivity.this);

                        ventana.setTitle("Mensaje");
                        ventana.setMessage("Entrada registrada a las " +
                                            hora + " del " + fecha);
                        ventana.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish(); // cerramos la activity
                            }
                        });
                        AlertDialog alert = ventana.create();
                        alert.show();


                    // si el child "hora_entrada" para una fecha X existe y el child
                    // "hora_salida" para esa misma fecha también existe, no guardamos
                    // nada en la BBDD y cerramos la activity. Es decir, como ya tenemos
                    // guardados en la BBDD la hora de entrada y salida para una fecha,
                    // no permitimos guardar más lecturas en la BBDD
                    } else if(dataSnapshot.child(idTrabajador).child(fecha).child("hora_entrada").exists()
                            && dataSnapshot.child(idTrabajador).child(fecha).child("hora_salida").exists()){

                        // mostramos un ventana de diálogo informativa
                        AlertDialog.Builder ventana = new AlertDialog.Builder(LeerQRActivity.this);

                        ventana.setTitle("Mensaje");
                        ventana.setMessage("Ya ha registrado la entrada y la salida en el día" +
                                        " de hoy");
                        ventana.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish(); // cerramos la activity
                            }
                        });
                        AlertDialog alert = ventana.create();
                        alert.show();


                    // si lo anterior no se cumple, si el child "hora_entrada" para una fecha x
                    // existe, pero el child "hora_salida" para esa fecha no existe, grabamos
                    // en la BBDD la hora de salida
                    } else if (dataSnapshot.child(idTrabajador).child(fecha).child("hora_entrada").exists()){
                    db.child(idTrabajador).child(fecha).child("hora_salida").setValue(hora);

                        // mostramos un ventana de diálogo informativa
                        AlertDialog.Builder ventana = new AlertDialog.Builder(LeerQRActivity.this);

                        ventana.setTitle("Mensaje");
                        ventana.setMessage("Salida registrada a las " +
                                hora + " del " + fecha);
                        ventana.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish(); // cerramos la activity
                            }
                        });
                        AlertDialog alert = ventana.create();
                        alert.show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        /*Toast.makeText(this, "Contents = " + rawResult.getContents() +
                ", Format = " + rawResult.getBarcodeFormat().getName() + " " + fecha + " " + hora, Toast.LENGTH_SHORT).show();*/


        // If you would like to resume scanning, call this method below:
        // esto es por si queremos seguir leyendo códigos QR
        //mScannerView.resumeCameraPreview(this);
    }
}