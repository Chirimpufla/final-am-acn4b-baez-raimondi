package com.example.final_am_acn4b_baez_raimondi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName, txtSurname, txtFecha, txtHora;
        ImageView cancel;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;

        /*switch (v.getId())
        {
            case R.id.list_cancel:

                break;
        }*/
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataModel dataModel = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.list_name);
            viewHolder.txtSurname = (TextView) convertView.findViewById(R.id.list_surname);
            viewHolder.txtFecha = (TextView) convertView.findViewById(R.id.list_fecha);
            viewHolder.txtHora = (TextView) convertView.findViewById(R.id.list_hora);
            viewHolder.cancel = (ImageView) convertView.findViewById(R.id.list_cancel);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtSurname.setText(dataModel.getSurname());
        viewHolder.txtFecha.setText(dataModel.getFecha());
        viewHolder.txtHora.setText(dataModel.getHora());
        viewHolder.cancel.setTag(position);

        return convertView;
    }
}
