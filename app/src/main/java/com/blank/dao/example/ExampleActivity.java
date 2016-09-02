package com.blank.dao.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.blank.dao.ParseObj;

import java.util.Date;
import java.util.List;

public class ExampleActivity extends AppCompatActivity {

    ExampleDaoManager daoManager;
    RecyclerView recyclerView;
    ExampleAdapter exampleAdapter;
    List<ExampleObject> objectList;
    Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        daoManager = new ExampleDaoManager(this);
        loadPage();
    }

    private void loadPage() {
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        objectList = daoManager.getAll(new ExampleObject());
        exampleAdapter = new ExampleAdapter(objectList);
        exampleAdapter.setOnAdapterListener(new ExampleOnAdapterListener() {
            @Override
            public void onItemClick(View view, int position) {
                ExampleObject obj  = objectList.get(position);
                daoManager.delete(obj);
                loadPage();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // Do nothing
            }
        });
        recyclerView.setAdapter(exampleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonAdd = (Button)findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExampleObject newObject = new ExampleObject();
                long time = new Date().getTime();
                newObject.someString = "Obj";
                newObject.someInteger = ParseObj.toInteger(time);
                newObject.someBoolean = time % 2 == 0;
                daoManager.saveOrUpdate(newObject);
                loadPage();
            }
        });
    }
}
