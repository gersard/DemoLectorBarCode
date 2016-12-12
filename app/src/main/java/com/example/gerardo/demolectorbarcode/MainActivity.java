package com.example.gerardo.demolectorbarcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLEctor = (Button) findViewById(R.id.btn_lector);

        btnLEctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,LectorActivity.class));
                startActivityForResult(new Intent(MainActivity.this,LectorActivity.class),CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            String barcode = data.getStringExtra("barcode");
            Toast.makeText(MainActivity.this, barcode, Toast.LENGTH_SHORT).show();
        }
    }
}
