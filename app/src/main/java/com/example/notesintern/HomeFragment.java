package com.example.notesintern;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    private FloatingActionButton fab;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    MyDatabaseHelper myDatabaseHelper;
    ArrayList<String> notesId,notesTitle,notesDescp;
    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_home, container, false);
        fab=root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNote fragment2 = new AddNote();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl, fragment2);
                fragmentTransaction.commit();
            }
        });
        myDatabaseHelper=new MyDatabaseHelper(getContext());
        notesId=new ArrayList<>();
        notesTitle=new ArrayList<>();
        notesDescp=new ArrayList<>();
        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        storeData(sh.getString("name",""));
        recyclerView=root.findViewById(R.id.recyclerView);
        customAdapter=new CustomAdapter(getActivity(),getContext(),notesId,notesTitle,notesDescp);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return root;

    }
    void storeData(String s){
        Cursor cursor=myDatabaseHelper.readAllData(s);
        if (cursor.getCount()==0){
            Log.d("hs","no data");
            Toast.makeText(getContext(),"No Data",Toast.LENGTH_LONG).show();
        }else{
            while (cursor.moveToNext()){
                Log.d("hs","on data");
                notesId.add(cursor.getString(0));
                notesTitle.add(cursor.getString(1));
                notesDescp.add(cursor.getString(2));
            }
        }
    }
}