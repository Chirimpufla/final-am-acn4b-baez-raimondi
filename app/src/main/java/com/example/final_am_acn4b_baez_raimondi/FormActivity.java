package com.example.final_am_acn4b_baez_raimondi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FormActivity extends AppCompatActivity {

    private final String TAG = "DBResponse";
    private ImageButton volver;
    private ImageView logo;
    private EditText nombre, apellido, fecha, hora;
    private Button registrar;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private ProgressDialog carga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            user = (FirebaseUser) extras.get("user");

        db = FirebaseFirestore.getInstance();

        volver = findViewById(R.id.volver);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeRedirect();
            }
        });

        logo = findViewById(R.id.logo_calendario);
        logo.setImageResource(R.drawable.calendario);

        nombre = findViewById(R.id.form_nombre);

        apellido = findViewById(R.id.form_apellido);

        fecha = findViewById(R.id.form_fecha);

        hora = findViewById(R.id.form_hora);

        registrar = findViewById(R.id.registrar_turno);
        registrar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registrarTurno();
                    }
                }
        );
    }

    public void homeRedirect(){
        Intent i = new Intent(getBaseContext(), HomeActivity.class);
        i.putExtra("user", user);
        startActivity(i);
    }

    public void registrarTurno(){

        final String NAME = nombre.getText().toString().trim();
        final String SURNAME = apellido.getText().toString().trim();
        final String FECHA = fecha.getText().toString().trim();
        final String HORA = hora.getText().toString().trim();
        carga = new ProgressDialog(this);

        if(NAME.isEmpty()){
            nombre.setError("Este campo no puede estar vacio.");
            nombre.requestFocus();
        } else if (SURNAME.isEmpty()) {
            apellido.setError("Este campo no puede estar vacio.");
            apellido.requestFocus();
        } else if(FECHA.isEmpty()){
            fecha.setError("Debe elegir una fecha para su turno.");
            fecha.requestFocus();
        } else if(HORA.isEmpty()){
            hora.setError("Debe elegir una hora para su turno.");
            hora.requestFocus();
        } else {
            carga.setTitle("Registrando turno.");
            carga.setMessage("Por favor espere...");
            carga.show();
            DocumentReference docRef = db.collection("usuarios").document(user.getUid()).collection("turnos").document("1");
            docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(!document.exists()){
                                Map<String, Object> params = new HashMap<>();
                                params.put("name", NAME);
                                params.put("surname", SURNAME);
                                params.put("fecha", FECHA);
                                params.put("hora", HORA);
                                params.put("estado", "vigente");
                                db.collection("usuarios")
                                    .document(user.getUid())
                                    .collection("turnos")
                                    .document("1")
                                    .set(params)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                carga.dismiss();
                                                Toast.makeText(getBaseContext(), "Turno agregado correctamente.", Toast.LENGTH_SHORT).show();
                                                homeRedirect();
                                            } else {
                                                carga.dismiss();
                                                Log.d(TAG, "Error: ", task.getException());
                                                Toast.makeText(getBaseContext(), "Ha ocurrido un error. Por favor intente mas tarde.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            } else {
                                db.collection("usuarios")
                                    .document(user.getUid())
                                    .collection("turnos")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                Integer id = 1;
                                                for(QueryDocumentSnapshot document : task.getResult()){
                                                    id = Integer.valueOf(document.getId()) + 1;
                                                }
                                                Map <String, Object> params = new HashMap<>();
                                                params.put("name", NAME);
                                                params.put("surname", SURNAME);
                                                params.put("fecha", FECHA);
                                                params.put("hora", HORA);
                                                params.put("estado", "vigente");
                                                db.collection("usuarios")
                                                    .document(user.getUid())
                                                    .collection("turnos")
                                                    .document(String.valueOf(id))
                                                    .set(params)
                                                    .addOnCompleteListener(
                                                            new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        carga.dismiss();
                                                                        Toast.makeText(getBaseContext(), "Turno agregado correctamente.", Toast.LENGTH_SHORT).show();
                                                                        homeRedirect();
                                                                    } else {
                                                                        carga.dismiss();
                                                                        Toast.makeText(getBaseContext(), "Error al registrar el turno. Por favor intente mas tarde.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }
                                                    );
                                            }
                                        }
                                    });
                            }
                        } else {
                            Log.d(TAG, "Error: ", task.getException());
                            Toast.makeText(getBaseContext(), "Ha ocurrido un error. Por favor intente mas tarde", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }
}