package com.example.notesintern;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static android.content.Context.MODE_PRIVATE;

public class AddNote extends Fragment {
    EditText title,descp;
    Button btnSave;
    public AddNote() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_add_note, container, false);
        title=root.findViewById(R.id.textTitle);
        descp=root.findViewById(R.id.textDesc);
        btnSave=root.findViewById(R.id.btnSave);
        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDatabaseHelper=new MyDatabaseHelper(getContext());
                myDatabaseHelper.addNote(title.getText().toString().trim(),descp.getText().toString().trim(),sh.getString("name",""));
                HomeFragment fragment2 = new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl, fragment2);
                fragmentTransaction.commit();
            }
        });
        return root;
    }
}