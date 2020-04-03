package edu.dam.empresaapp.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.dam.empresaapp.R;
import edu.dam.empresaapp.Turnos;

public class AdaptadorMes extends BaseAdapter {

    private Context contexto;
    private ArrayList<Turnos> listItems;

    public AdaptadorMes(Context contexto, ArrayList<Turnos> listItems) {
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
        String mes;
        Turnos item = (Turnos) getItem(position);

        convertView = LayoutInflater.from(contexto).inflate(R.layout.item_adaptador_mes, null);

        ImageView ivAnio = convertView.findViewById(R.id.ivAnio);
        ivAnio.setImageResource(R.drawable.ic_calendar);

        mes = item.getMesTurno();
        TextView tvMes= convertView.findViewById(R.id.tvMes);
        tvMes.setText(mes);

        return convertView;
    }
}
