package com.calftracker.project.adapters.protocols;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.calftracker.project.models.Illness;
import com.calftracker.project.calftracker.R;
import com.calftracker.project.models.Treatment_Protocol;

import java.util.List;

/**
 * Created by AlexanderGlowacki on 11/1/17.
 */

public class IllnessAdapter extends BaseAdapter {

    private Context context;
    private List<Illness> illnessList;
    private LayoutInflater layoutInflater;
    private Treatment_Protocol treatmentProtocol;

    public IllnessAdapter(Context context, List<Illness> illnessList) {
        this.context = context;
        this.illnessList = illnessList;
        this.layoutInflater = LayoutInflater.from(context);
        this.treatmentProtocol = treatmentProtocol;
    }

    public class ViewHolder {
        public TextView illnessName;
        public TextView treatmentNotes;
    }

    public int getCount() {
        return illnessList.size();
    }
    public Illness getItem(int position) {
        return illnessList.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder =  new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.illness_list, null);
            holder.illnessName = (TextView) convertView.findViewById(R.id.illness_name);
            holder.treatmentNotes = (TextView) convertView.findViewById(R.id.treatment_notes);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.illnessName.setText(illnessList.get(position).getName());
        holder.treatmentNotes.setText(illnessList.get(position).getTreatmentProtocol().getNotes());
        return convertView;
    }
}
