package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroActivity extends AppCompatActivity {

    // Declaramos las vistas
    private EditText etClave;
    private EditText etEmail;
    private EditText etNif;
    private EditText etNombre;
    private EditText etApellido1;
    private EditText etApellido2;
    private EditText etTelefono;
    private Button btnRegistrar;

    // Declaramos la variable Booleana "esResponsable", y le asignamos
    // false, ya que todos los trabajadores por defecto no serán responsables,
    // solo habrá un usuario que será responsable
    private Boolean esResponsable = false;

    // nos valemos de la funcionalidad "Firebase Authentication" de Firebase,
    // la cual nos permite crear usuarios que utilizan sus direcciones de correo
    // electrónico y contraseñas para acceder.
    // https://firebase.google.com/docs/auth/android/password-auth?hl=es-419
    // Para ello declaramos un objeto FirebaseAuth
    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        //inicializamos el objeto FirebaseAuth y obtenemos la instancia
        mAuth = FirebaseAuth.getInstance();

        //referenciamos las vistas
        etClave      = findViewById(R.id.etClave);
        etEmail      = findViewById(R.id.etEmail);
        etNif        = findViewById(R.id.etNif);
        etNombre     = findViewById(R.id.etNombre);
        etApellido1  = findViewById(R.id.etApellido1);
        etApellido2  = findViewById(R.id.etApellido2);
        etTelefono   = findViewById(R.id.etTeléfono);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        progressBar  = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //llamamos al método cuando pulsamos el botón "registrar"
                registerUser();

            }
        });

    }

    // creamos el método "registerUser" para registrar un usuario/trabajador
    private void registerUser() {
        final String email     = etEmail.getText().toString().trim();
        String password        = etClave.getText().toString().trim();
        final String nif       = etNif.getText().toString().trim();
        final String nombre    = etNombre.getText().toString().trim();
        final String apellido1 = etApellido1.getText().toString().trim();
        final String apellido2 = etApellido2.getText().toString().trim();
        final String telefono  = etTelefono.getText().toString().trim();

        // realizamos diferentes validaciones, entre ellas que ningún
        // campo esté vacío

        if (email.isEmpty()) {
            etEmail.setError("introduzca un email");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("introduzca un email válido");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etClave.setError("introduzca una contraseña");
            etClave.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etClave.setError("la contraseña debe tener al menos seis caracteres");
            etClave.requestFocus();
            return;
        }

        if (nif.isEmpty()) {
            etNif.setError("introduzca un número de NIF");
            etNif.requestFocus();
            return;
        }

        if (nombre.isEmpty()) {
            etNombre.setError("introduzca su nombre");
            etNombre.requestFocus();
            return;
        }

        if (apellido1.isEmpty()) {
            etApellido1.setError("introduzca su primer apellido");
            etApellido1.requestFocus();
            return;
        }

        if (apellido2.isEmpty()) {
            etApellido2.setError("introduzca su segundo apellido");
            etApellido2.requestFocus();
            return;
        }


        if (telefono.isEmpty()) {
            etTelefono.setError("introduzca un número de teléfono");
            etTelefono.requestFocus();
            return;
        }

        if (telefono.length() != 9) {
            etTelefono.setError("el número de teléfono debe contener 9 dígitos");
            etTelefono.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //valiéndonos del método "createUserWithEmailAndPassword"
        //de FirebaseAuth, se crea un usuario con su identificador,
        //clave y email
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //si la creación de email y password en el servidor de
                        //autenticación de Firebase es correcto, instanciamos
                        //un objeto "Trabajador"
                        if (task.isSuccessful()) {

                            Trabajador trabajador = new Trabajador(
                                    FirebaseAuth.getInstance().getCurrentUser().getUid(), //el uid del usuario
                                    email,
                                    nif,
                                    nombre,
                                    apellido1,
                                    apellido2,
                                    telefono,
                                    esResponsable
                            );

                            //obtenemos la instancia de la base de datos y establecemos la
                            //referencia de dicha base de datos en un nodo llamado "Trabajadores",
                            //obtenemos la instancia de FirebaseAuth del usuario que se acaba de
                            //crear y obtenemos su identificador único (UID) -el cual se generó
                            //automáticamente con el método "createUserWithEmailAndPassword"- y
                            //le añadimos un hijo con ese UID y los datos del objeto "trabajador"
                            //que acabamos de instanciar
                            FirebaseDatabase.getInstance().getReference("Trabajadores")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(trabajador).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    //si la creación del usuario en la base de datos es correcta,
                                    //aparecerá un mensaje indicándolo y se devolverá al usuario
                                    //a la pantalla de "login"
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegistroActivity.this, "Registro completado", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);

                                        //si la creación del usuario en la BBDD no es correcta,
                                        // se muestra un mensaje
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                                    }

                                    progressBar.setVisibility(View.GONE);
                                }

                            });

                            //comprobamos que el email no se encuentre registrado
                            //en la BBDD
                        } else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegistroActivity.this, "El email ya existe ", Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.GONE);

                                //si falla la creación del email y la contraseña en el servicio
                                //de autenticación de Firebase, obtenemos el mensaje de la
                                //excepción que se genere
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

    }

    //método que al pulsar en el ImageView o TextView de la cabecera
    //nos lleva a la pantalla de Login
    public void login(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
