package com.example.gerardo.demolectorbarcode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public static final int CODE = 1;
    private Tracker mTracker;
    public static HashMap<String, String> hashDatos = new HashMap<>();
    private AdapterResultado adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        adapter = new AdapterResultado(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button btnLEctor = (Button) findViewById(R.id.btn_lector);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Consulta> consultas = realm.where(Consulta.class).findAll();
        TextView txtSinRegistros = (TextView) findViewById(R.id.txt_sin_registros);
        if (consultas.size() == 0){
            txtSinRegistros.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            adapter.addAll(consultas);
            txtSinRegistros.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(adapter);
        }

        btnLEctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,LectorActivity.class));


                Answers.getInstance().logCustom(new CustomEvent("Abrió el lector de codigo de barra"));

                startActivityForResult(new Intent(MainActivity.this,LectorActivity.class),CODE);
            }
        });

        // TODO: Use your own attributes to track content views in your app
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Pantalla Principal")
                .putContentType("Boton para escanear")
                .putContentId("112233"));
//                .putCustomAttribute("Favorites Count", 20)
//                .putCustomAttribute("Screen Orientation", "Landscape"));







    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    private void buscarResultado(String codigo, final ProgressDialog dialog){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Producto");
//
//        myRef.setValue("Hello, World!");
        Query queryCodebar = FirebaseDatabase.getInstance().getReference()
                .child("Producto")
                .orderByKey()
                .equalTo(codigo)
                .limitToFirst(1);
        // Read from the database
        queryCodebar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("CONSULTA","KEY: "+dataSnapshot.getKey());
                Log.d("CONSULTA","VALUE: "+dataSnapshot.getValue().toString());


                for (final DataSnapshot data : dataSnapshot.getChildren()){

                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Consulta consulta = realm.createObject(Consulta.class,getFechaActual());
                            consulta.setNombre(data.child("nombre").getValue().toString());
                            consulta.setMarca(data.child("marca").getValue().toString());
                            consulta.setResultado(Integer.parseInt(data.child("resultado").getValue().toString()));
                            consulta.setCodigo(Long.valueOf(data.child("codigo").getValue().toString()));
                            updateRecyclerView(realm);
                            if (dialog.isShowing()){
                                dialog.dismiss();
                            }
                        }
                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("valores", "Failed to read value.", error.toException());
            }
        });
    }

    private void updateRecyclerView(Realm realm){
        RealmResults<Consulta> consultas = realm.where(Consulta.class).findAll();
        adapter.addAll(consultas);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == CODE){
                ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setTitle("Consultando información");
                dialog.setMessage("Espere unos segundos");
                dialog.setCancelable(false);
                dialog.show();
                String barcode = data.getStringExtra("barcode");

                buscarResultado(barcode, dialog);
            }
        }
    }

    private String getFechaActual(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

}
