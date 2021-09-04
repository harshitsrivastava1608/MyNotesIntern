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

public class UpdateFragment extends Fragment {
EditText uTitle,uDesc;String id;
    Button button;
    public UpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_update, container, false);
        uTitle=view.findViewById(R.id.utextTitle);
        uDesc=view.findViewById(R.id.utextDesc);
        button=view.findViewById(R.id.ubtnSave);
        setData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                MyDatabaseHelper myDatabaseHelper=new MyDatabaseHelper(getContext());
                Log.d("hsit",""+id+"*");
                myDatabaseHelper.updateData(String.valueOf(id),uTitle.getText().toString().trim(),uDesc.getText().toString(),sh.getString("name",""));
                HomeFragment fragment2 = new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl, fragment2);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
    void setData(){
        Bundle bundle=this.getArguments();

        if(bundle!=null){
            id=bundle.getString("id","");
            uTitle.setText(bundle.getString("nt",""));
            uDesc.setText(bundle.getString("nd",""));
        }
    }
}