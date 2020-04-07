package edu.dam.empresaapp.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.dam.empresaapp.pojos.Fichajes;
import edu.dam.empresaapp.R;

public class AdaptadorFichajes extends BaseAdapter {

    private Context contexto;
    private ArrayList<Fichajes> listItems;

    public AdaptadorFichajes(Context contexto, ArrayList<Fichajes> listItems) {
        this.contexto = contexto;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Fichajes item = (Fichajes) getItem(position);

        convertView = LayoutInflater.from(contexto).inflate(R.layout.item_fichajes, null);

        ImageView ivUsuario = convertView.findViewById(R.id.ivReloj);
        ivUsuario.setImageResource(R.drawable.clock);

        TextView tvFecha= convertView.findViewById(R.id.tvFecha);
        tvFecha.setText(item.getFecha());

        TextView tvEntrada = convertView.findViewById(R.id.tvEntrada);
        tvEntrada.setText(item.getHoraEntrada() + " -- " + item.getTextoEntrada());

        TextView tvSalida = convertView.findViewById(R.id.tvSalida);
        tvSalida.setText(item.getHoraSalida() + " -- " + item.getTextoSalida());

        return convertView;
    }
}
