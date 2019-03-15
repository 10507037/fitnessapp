package com.example.apple.fitpal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private ArrayList<Workout> wodList;
    private ArrayList<String> title;

    private Adapter adap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.lv);


        wodList = ExtractData.loadworkout(this );
        title= new ArrayList<>();
        for(int i=0; i<wodList.size(); i++)
        {
            String str=wodList.get(i).getTitle();
            title.add(str);
        }



        adap=new ArrayAdapter<String>(this ,android.R.layout.simple_list_item_1,title);
        lv.setAdapter((ListAdapter) adap);
    }
}
