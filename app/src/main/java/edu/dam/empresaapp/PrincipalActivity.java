package edu.dam.empresaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class PrincipalActivity extends AppCompatActivity {

    TextView tvUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        tvUser = findViewById(R.id.tvUser);

    }
}
