package com.example.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    List<WeatherReportModel> list;

    public Adapter(List<WeatherReportModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WeatherReportModel model=list.get(position);

        holder.date.setText(model.getApplicable_date());
        holder.minres.setText(String.valueOf(model.getMin_temp()));
        holder.maxres.setText(String.valueOf(model.getMax_temp()));
        holder.res.setText(model.getWeather_state_name());
        holder.temp.setText(String.valueOf(model.getThe_temp()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView date;
        public TextView res;
        public TextView minres;
        public TextView maxres;
        public TextView temp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            res=itemView.findViewById(R.id.status);
            minres=itemView.findViewById(R.id.minres);
            maxres=itemView.findViewById(R.id.maxres);

            temp=itemView.findViewById(R.id.temp);
        }
    }
}
