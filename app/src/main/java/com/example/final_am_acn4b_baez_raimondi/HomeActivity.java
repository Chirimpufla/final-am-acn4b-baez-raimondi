package com.example.final_am_acn4b_baez_raimondi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity {

    private final String TAG = "DBResponse";
    private ImageButton avatar;
    private TextView user, nombre;
    private Button turno;
    private ListView listaTurnos;
    private FirebaseUser usuario;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            usuario = (FirebaseUser) extras.get("user");
        }

        avatar = findViewById(R.id.avatar);
        avatar.setImageResource(R.drawable.avatar);
        avatar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        redirect(ProfileActivity.class);
                    }
                }
        );

        nombre = findViewById(R.id.apellido);
        nombre.setText(getNombreCompleto());

        user = findViewById(R.id.nombre);
        user.setText(getMail());

        turno = findViewById(R.id.nuevoturno);
        turno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect(FormActivity.class);
            }
        });

        listaTurnos = findViewById(R.id.turnos_list);
        listar();
    }

    public void redirect(Class c){
        Intent i = new Intent(this, c);
        i.putExtra("user", usuario);
        startActivity(i);
    }

    public String getNombreCompleto(){
        return usuario.getDisplayName();
    }

    public String getMail(){
        return usuario.getEmail();
    }

    public void listar(){
        db = FirebaseFirestore.getInstance();
        db.collection("turnos")
                .get()
                .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                    }
                                } else {
                                    Log.d(TAG, "Error al recuperar los datos: ", task.getException());
                                }
                            }
                        }
                );
    }
}