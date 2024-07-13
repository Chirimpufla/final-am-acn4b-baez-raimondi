package com.example.final_am_acn4b_baez_raimondi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private final String TAG = "Adapter message:";
    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtNombre, txtApellido, txtFecha, txtHora;
        ImageView cancel;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        DataModel dataModel = (DataModel) object;

        AlertDialog.Builder alerta = new AlertDialog.Builder(v.getContext());
        alerta.setTitle("¡Atención!");
        alerta.setMessage("¿Esta seguro de que desea borrar este turno?");
        alerta.setMessage("Fecha: " + dataModel.getFecha() +
            " - Hora: " + dataModel.getHora());
        alerta.setCancelable(false);
        alerta.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Borrado");
                dialog.dismiss();
            }
        });
        alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alerta.show();
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataModel dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtNombre = (TextView) convertView.findViewById(R.id.list_name);
            viewHolder.txtApellido = (TextView) convertView.findViewById(R.id.list_surname);
            viewHolder.txtFecha = (TextView) convertView.findViewById(R.id.list_fecha);
            viewHolder.txtHora = (TextView) convertView.findViewById(R.id.list_hora);
            viewHolder.cancel = (ImageView) convertView.findViewById(R.id.list_cancel);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        /*Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;*/

        viewHolder.txtNombre.setText(dataModel.getName());
        viewHolder.txtApellido.setText(dataModel.getSurname());
        viewHolder.txtFecha.setText(dataModel.getFecha());
        viewHolder.txtHora.setText(dataModel.getHora());
        viewHolder.cancel.setOnClickListener(this);
        viewHolder.cancel.setTag(position);

        return convertView;
    }
}