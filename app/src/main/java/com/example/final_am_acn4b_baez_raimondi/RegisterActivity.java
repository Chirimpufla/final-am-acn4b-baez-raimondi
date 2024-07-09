package com.example.final_am_acn4b_baez_raimondi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "EmailPassword";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ImageView avatar;
    private EditText nombre, apellido, telefono, mail;
    private Button registro;
    private ProgressDialog carga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        avatar = findViewById(R.id.register_avatar);
        avatar.setImageResource(R.drawable.avatar);

        nombre = findViewById(R.id.register_name);

        apellido = findViewById(R.id.register_surname);

        mail = findViewById(R.id.register_mail);

        telefono = findViewById(R.id.register_phone);

        registro = findViewById(R.id.register_button);
        registro.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registrar();
                    }
                }
        );
    }

    public void registrar(){

        final String NAME = nombre.getText().toString().trim();
        final String SURNAME = apellido.getText().toString().trim();
        final String EMAIL = mail.getText().toString().trim();
        final String PHONE = telefono.getText().toString().trim();
        final String PASS = "123456";

        if(NAME.isEmpty()){
            nombre.setError("Este campo no puede estar vacio.");
            nombre.requestFocus();
        } else if (SURNAME.isEmpty()) {
            apellido.setError("Este campo no puede estar vacio.");
            apellido.requestFocus();
        } else if (EMAIL.isEmpty()) {
            mail.setError("Este campo no puede estar vacio.");
            mail.requestFocus();
        } else if (PHONE.isEmpty()){
            telefono.setError("Este campo no puede estar vacio.");
            telefono.requestFocus();
        } else {
            carga = new ProgressDialog(this);
            carga.setTitle("Registrando usuario");
            carga.setMessage("Por favor espere...");
            carga.show();

            db = FirebaseFirestore.getInstance();

            mAuth.createUserWithEmailAndPassword(EMAIL, PASS)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mAuth.signInWithEmailAndPassword(NAME, PASS)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                Log.d("TAG", "Inicio de sesion exitoso.");
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                if(user != null){
                                                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(NAME + " " + SURNAME)
                                                            .build();
                                                    user.updateProfile(profileUpdate)
                                                                .addOnCompleteListener(
                                                                        new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                Map<String, Object> params = new HashMap<>();
                                                                                params.put("name", NAME);
                                                                                params.put("surname", SURNAME);
                                                                                params.put("activo", true);
                                                                                db.collection("usuarios").document(user.getUid())
                                                                                    .set(params)
                                                                                    .addOnCompleteListener(
                                                                                            new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    Toast.makeText(getBaseContext(), "Su contraseña es 123456. Puede cambiarla tocando la foto en la pagina principal.", Toast.LENGTH_LONG).show();
                                                                                                    homeRedirect(user);
                                                                                                }
                                                                                            }
                                                                                    );
                                                                            }
                                                                        }
                                                                );
                                                }
                                            }
                                        });
                            } else {
                                carga.dismiss();
                                Log.d(TAG, "Error al crear usuario: " + task.getException());
                                Toast.makeText(getBaseContext(), "Error al crear el usuario. Por favor intente mas tarde.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void homeRedirect(FirebaseUser u){

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("user", u);
        startActivity(i);

    }
}