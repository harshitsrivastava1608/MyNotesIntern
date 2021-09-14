package com.example.notesintern;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private Activity activity;
    private ArrayList notesId,notesTitle,notesDescp;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_notes,parent,false);
        return new MyViewHolder(view);
    }

    public CustomAdapter(Activity activity,Context context,
                         ArrayList notesId, ArrayList notesTitle, ArrayList notesDescp) {
        this.context=context;
        this.activity=activity;
        this.notesTitle=notesTitle;
        this.notesId=notesId;
        this.notesDescp=notesDescp;
    }

    @Override
    public void onBindViewHolder(@NonNull  CustomAdapter.MyViewHolder holder, int position) {
        holder.title.setText(String.valueOf(notesTitle.get(position)));
        holder.description.setText(String.valueOf(notesDescp.get(position)));
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putString("id",String.valueOf(notesId.get(position)));
                bundle.putString("nt",String.valueOf(notesTitle.get(position)));
                bundle.putString("nd",String.valueOf(notesDescp.get(position)));
                UpdateFragment updateFragment=new UpdateFragment();
                updateFragment.setArguments(bundle);
                AppCompatActivity activity=(AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl,updateFragment).commit();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDatabaseHelper=new MyDatabaseHelper(v.getContext());
                myDatabaseHelper.deleteOneRow(String.valueOf(notesId.get(position)));
                notesId.remove(position);
                notesDescp.remove(position);
                notesTitle.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesId.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,description;
        ImageButton btnUpdate,btnDelete;
        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.tvTitle);
            description=itemView.findViewById(R.id.tvDesc);
            btnDelete=itemView.findViewById(R.id.btnDelete);
            btnUpdate=itemView.findViewById(R.id.btnUpdate);
        }
    }
}
