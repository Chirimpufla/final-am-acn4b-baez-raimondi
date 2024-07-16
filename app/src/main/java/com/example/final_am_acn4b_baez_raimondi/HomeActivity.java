package com.example.final_am_acn4b_baez_raimondi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private final String TAG = "DBResponse";
    private ImageButton avatar;
    private TextView user, nombre;
    private Button turno;
    private ListView listaTurnos;
    private FirebaseUser usuario;
    private FirebaseFirestore db;
    private ArrayList<DataModel> datamodels;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            usuario = (FirebaseUser) extras.get("user");
        }

        db = FirebaseFirestore.getInstance();

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
        listaTurnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object object = parent.getItemAtPosition(position);
                DataModel dataModel = (DataModel) object;

                Dialog info_turno = new Dialog(view.getContext());
                info_turno.setContentView(R.layout.info_turno);

                TextView txtTitulo = (TextView) info_turno.findViewById(R.id.turno_title);
                TextView txtNombre = (TextView) info_turno.findViewById(R.id.turno_nombre);
                TextView txtFecha = (TextView) info_turno.findViewById(R.id.turno_fecha);
                TextView txtHora = (TextView) info_turno.findViewById(R.id.turno_hora);
                Button ok = (Button) info_turno.findViewById(R.id.turno_ok);

                txtTitulo.setText("Información del turno: ");
                txtNombre.setText(dataModel.getName() + " " + dataModel.getSurname());
                txtFecha.setText(dataModel.getFecha());
                txtHora.setText(dataModel.getHora());
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info_turno.dismiss();
                    }
                });

                info_turno.show();
            }
        });
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
        db.collection("usuarios")
            .document(usuario.getUid())
            .collection("turnos")
            .whereEqualTo("estado", "vigente")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        datamodels = new ArrayList<>();
                        for(QueryDocumentSnapshot document : task.getResult()){
                            datamodels.add(new DataModel(
                                    document.getString("name"),
                                    document.getString("surname"),
                                    document.getString("fecha"),
                                    document.getString("hora"),
                                    Integer.valueOf(document.getId())
                            ));
                        }

                        adapter = new CustomAdapter(datamodels, HomeActivity.this);
                        listaTurnos.setAdapter(adapter);
                    }
                }
            });
    }
}