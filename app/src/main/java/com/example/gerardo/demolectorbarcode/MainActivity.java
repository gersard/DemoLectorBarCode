package com.example.gerardo.demolectorbarcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    public static final int CODE = 1;
    private Tracker mTracker;
    public static HashMap<String, String> hashDatos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);


        Button btnLEctor = (Button) findViewById(R.id.btn_lector);

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


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Producto");
//
//        myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    hashDatos.put(data.getValue().toString(),data.getKey());
                }
//                String value = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("valores", "Failed to read value.", error.toException());
            }
        });




    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == CODE){

                String barcode = data.getStringExtra("barcode");

                Toast.makeText(MainActivity.this, hashDatos.get(barcode), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
