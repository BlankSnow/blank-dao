package com.blank.dao.example;

import android.content.Context;

import com.blank.dao.DaoBaseObject;
import com.blank.dao.DaoManager;
import com.blank.dao.example.base.ExampleObject;
import com.blank.dao.example.builder.Example2Object;

import java.util.ArrayList;
import java.util.List;

public class MyDaoManager extends DaoManager {

    private static final int DATABASE_VERSION = 1;

    public MyDaoManager(Context context) {
        super(context, DATABASE_VERSION);
    }

    @Override
    protected List<DaoBaseObject> getAllTableObjects() {
        List<DaoBaseObject> objectList = new ArrayList<>();

        // FIXME: Classes to CRUD in database
        objectList.add(new ExampleObject());
        objectList.add(new Example2Object.ExampleObjectMyBuilder().build());
        // TODO: more classes...

        return objectList;
    }
}
