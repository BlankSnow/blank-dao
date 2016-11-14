package com.blank.dao.example.builder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.blank.dao.ParseObj;
import com.blank.dao.example.MyOnAdapterListener;
import com.blank.dao.example.MyDaoManager;
import com.blank.dao.example.R;

import java.util.Date;
import java.util.List;

public class Example2Activity extends AppCompatActivity {

    MyDaoManager daoManager;
    RecyclerView recyclerView;
    Example2Adapter exampleAdapter;
    List<Example2Object> objectList;
    Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        daoManager = new MyDaoManager(this);
        loadPage();
    }

    private void loadPage() {
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        objectList = daoManager.getAll(new Example2Object.ExampleObjectMyBuilder().build());
        exampleAdapter = new Example2Adapter(objectList);
        exampleAdapter.setOnAdapterListener(new MyOnAdapterListener() {
            @Override
            public void onItemClick(View view, int position) {
                Example2Object obj  = objectList.get(position);
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
                long time = new Date().getTime();

                Example2Object newObject = new Example2Object.ExampleObjectMyBuilder()
                        .setSomeString("Obj")
                        .setSomeBoolean(time % 2 == 0)
                        .setSomeInteger(ParseObj.toInteger(time))
                        .build();
                daoManager.saveOrUpdate(newObject);
                loadPage();
            }
        });
    }
}
