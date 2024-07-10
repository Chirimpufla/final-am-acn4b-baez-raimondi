package com.example.final_am_acn4b_baez_raimondi;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG = "DBResponse";
    private ImageView avatar;
    private EditText nombre, apellido,pass;
    private Button cambiar, guardar;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            user = (FirebaseUser) extras.get("user");
        }

        db = FirebaseFirestore.getInstance();


        avatar = findViewById(R.id.profile_avatar);
        avatar.setImageResource(R.drawable.avatar);

        apellido = findViewById(R.id.profile_user);

        nombre = findViewById(R.id.profile_name);

        setInfo();

        guardar = findViewById(R.id.guardar);
        guardar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        guardar();
                    }
                }
        );

        pass = findViewById(R.id.profile_password);
        pass.setHint("Cambiar contraseña");

        cambiar = findViewById(R.id.cambiar);
        cambiar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cambiarContrasena();
                    }
                }
        );

    }

    public void setInfo(){
        DocumentReference docRef = db.collection("usuarios").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        nombre.setText(document.getString("name"));
                        apellido.setText(document.getString("surname"));
                    } else {
                        nombre. setText("No encontrado.");
                        apellido.setText("No encontrado");
                    }
                } else {
                    Log.d(TAG, "Error: ", task.getException());
                    Toast.makeText(getBaseContext(), "No se ha podido encontrar el usuario.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void guardar(){

        final String NAME = nombre.getText().toString().trim();
        final String SURNAME = apellido.getText().toString().trim();

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
            .setDisplayName(NAME + " " + SURNAME)
            .build();
        user.updateProfile(profileUpdate).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Map <String, Object> params = new HashMap<>();
                            params.put("name", NAME);
                            params.put("surname", SURNAME);
                            DocumentReference docRef = db.collection("usuarios").document(user.getUid());
                            docRef.update(params).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getBaseContext(), "Cambios realizados con exito.", Toast.LENGTH_SHORT).show();
                                                homeRedirect();
                                            } else {
                                                Log.d(TAG, "Error: ", task.getException());
                                                Toast.makeText(getBaseContext(), "No se pudieron realizar los cambios.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                            );
                        } else {
                            Log.d(TAG, "Error: ", task.getException());
                            Toast.makeText(getBaseContext(), "Ha ocurrido un error al realizar el cambio. Por favor intente mas tarde.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

    }

    public void cambiarContrasena(){

    }

    public void homeRedirect(){

        user = FirebaseAuth.getInstance().getCurrentUser();
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("user", user);
        startActivity(i);

    }
}