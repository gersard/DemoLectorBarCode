package com.example.gerardo.demolectorbarcode;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.RealmResults;

/**
 * Created by Gerardo on 01/01/2017.
 */

public class AdapterResultado extends RecyclerView.Adapter<AdapterResultado.ResultadoViewHolder> implements View.OnClickListener {

    Context context;
    RealmResults<Consulta> analisis;

    private View.OnClickListener listener;

    public AdapterResultado(Context context) {
        this.context = context;
    }

    @Override
    public ResultadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultado,parent,false);
        view.setOnClickListener(this);
        return new ResultadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultadoViewHolder holder, int position) {
        Consulta currentConsulta = analisis.get(position);

        holder.setTxtNombreProducto(currentConsulta.getNombre());
        holder.setTxtMarcaProducto(currentConsulta.getMarca());
        holder.setTxtFechaConsulta(currentConsulta.getFechaConsulta());
        holder.setImageResultado(currentConsulta.getResultado());
    }

    @Override
    public int getItemCount() {
        return analisis.size();
    }

    public void addAll(RealmResults<Consulta> analisis){
        this.analisis = analisis;
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    class ResultadoViewHolder extends RecyclerView.ViewHolder{

        TextView txtNombreProducto;
        TextView txtMarcaProducto;
        TextView txtFechaConsulta;
        ImageView imageResultado;

        public ResultadoViewHolder(View itemView) {
            super(itemView);

            txtNombreProducto = (TextView) itemView.findViewById(R.id.txt_item_nombre_producto);
            txtMarcaProducto = (TextView) itemView.findViewById(R.id.txt_item_marca_producto);
            txtFechaConsulta = (TextView) itemView.findViewById(R.id.txt_item_fecha_consulta);
            imageResultado = (ImageView) itemView.findViewById(R.id.image_item_resultado);

        }

        public void setTxtNombreProducto(String nombre) {
            this.txtNombreProducto.setText(nombre);
        }

        public void setTxtMarcaProducto(String marca) {
            txtMarcaProducto.setText(marca);
        }

        public void setTxtFechaConsulta(String fechaConsulta) {
            txtFechaConsulta.setText(fechaConsulta);
        }

        public void setImageResultado(int resultado) {
            switch (resultado){
                case Constants.RESULTADO_SIN_GLUTEN:
                    imageResultado.setImageResource(R.drawable.ic_victo_bueno);
                    break;
                case Constants.RESULTADO_PUEDE_CONTENER:
                    imageResultado.setImageResource(R.drawable.ic_puede_contener);
                    break;
                case Constants.RESULTADO_CON_GLUTEN:
                    imageResultado.setImageResource(R.drawable.ic_contiene);
                    break;
                default:
                    imageResultado.setImageResource(R.drawable.ic_puede_contener);
                    break;
            }
        }
    }


}
