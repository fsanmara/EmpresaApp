package edu.dam.empresaapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.dam.empresaapp.R;

public class MainActivity extends AppCompatActivity {

    //declaramos las vistas
    private EditText txtEmail;
    private EditText txtClave;
    private Button btnLogin;

    //declaramos un objeto FirebaseAuth
    FirebaseAuth mAuth;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmail      = findViewById(R.id.txtEmail);
        txtClave      = findViewById(R.id.txtContraseña);
        btnLogin      = findViewById(R.id.btnLogin);
        progressBar   = findViewById(R.id.loginProgressbar);

        progressBar.setVisibility(View.GONE);

        //obtenemos la instancia del objeto FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //listener que se ejecuta al pulsar el botón Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txtEmail.getText().toString().trim();
                String clave = txtClave.getText().toString().trim();

                //realizamos validaciones por si el formato de email
                //no es válido o están los campos vacíos
                if (email.isEmpty()) {
                    txtEmail.setError("introduzca su email");
                    txtEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    txtEmail.setError("introduzca un email válido");
                    txtEmail.requestFocus();
                    return;
                }

                if (clave.isEmpty()) {
                    txtClave.setError("introduzca su contraseña");
                    txtClave.requestFocus();
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                //nos valemos del método signInWithEmailAndPassword del
                // objeto FirebaseAuth para hacer el login
                mAuth.signInWithEmailAndPassword(email, clave)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                                    startActivity(intent);

                                    progressBar.setVisibility(View.GONE);

                                } else {

                                    Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos",
                                            Toast.LENGTH_SHORT).show();

                                    progressBar.setVisibility(View.GONE);
                                }

                            }
                        });

            }
        });

    }

    //método que al pulsar en el ImageView o TextView de la cabecera
    //nos lleva a la pantalla de Registro
    public void signup(View view) {
        Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
        startActivity(intent);

    }

}

