package com.example.gerardo.demolectorbarcode;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uncopt.android.widget.text.justify.JustifiedTextView;

/**
 * Created by Gerardo on 02/01/2017.
 */

public class ResultadoDialog extends Dialog {

    private int resultado;

    ImageView imageIcon;
    TextView txtTitle;
    JustifiedTextView txtMessage;
    Button btnEntendido;

    public ResultadoDialog(Context context, int resultado) {
        super(context);
        this.resultado = resultado;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureDialog();
    }

    private void configureDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_resultado);

        txtTitle = (TextView) findViewById(R.id.txt_dialog_title);
        txtMessage = (JustifiedTextView) findViewById(R.id.txt_dialog_message);
        imageIcon = (ImageView) findViewById(R.id.image_dialog_icon);
        btnEntendido = (Button) findViewById(R.id.btn_dialog_entendido);


        switch (resultado){
            case Constants.RESULTADO_SIN_GLUTEN:
                imageIcon.setImageResource(R.drawable.ic_victo_bueno);
                txtTitle.setText(getContext().getString(R.string.title_libre_gluten));
                txtMessage.setText(getContext().getString(R.string.message_libre_gluten));
                break;
            case Constants.RESULTADO_PUEDE_CONTENER:
                imageIcon.setImageResource(R.drawable.ic_puede_contener);
                txtTitle.setText(getContext().getString(R.string.title_puede_gluten));
                txtMessage.setText(getContext().getString(R.string.message_puede_gluten));
                break;
            case Constants.RESULTADO_CON_GLUTEN:
                imageIcon.setImageResource(R.drawable.ic_contiene);
                txtTitle.setText(getContext().getString(R.string.title_contiene_gluten));
                txtMessage.setText(getContext().getString(R.string.message_contiene_gluten));
                break;
        }

        btnEntendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        getWindow().setLayout(Funciones.ConvertDpToPx(360), ViewGroup.LayoutParams.WRAP_CONTENT );
        getWindow().setGravity(Gravity.CENTER);

    }



}
