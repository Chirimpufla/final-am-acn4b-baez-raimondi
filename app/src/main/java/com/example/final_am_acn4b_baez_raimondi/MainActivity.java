package com.example.final_am_acn4b_baez_raimondi;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private EditText user, pass;
    private ProgressDialog carga;
    private ImageView logo;
    private Button login, register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logo);
        logo.setImageResource(R.drawable.logo);

        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login(){

        final String USR = user.getText().toString().trim();
        final String PASS = user.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(USR, PASS).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Inicio de sesion exitoso");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user != null){
                                Toast.makeText(getBaseContext(), "Inicio de sesion exitoso.", Toast.LENGTH_SHORT).show();
                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                Intent i = new Intent(getBaseContext(), HomeActivity.class);
                                i.putExtra("name", name);
                                i.putExtra("mail", email);
                                startActivity(i);
                            }
                        } else {
                            Log.d(TAG, "Fallo al iniciar sesion" + task.getException());
                            Toast.makeText(getBaseContext(), "Error al iniciar sesion.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}