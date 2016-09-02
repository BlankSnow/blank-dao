package com.blank.dao.example;

import android.content.Context;

import com.blank.dao.DaoBaseObject;
import com.blank.dao.DaoManager;

import java.util.ArrayList;
import java.util.List;

public class ExampleDaoManager extends DaoManager {

    private static final int DATABASE_VERSION = 1;

    public ExampleDaoManager(Context context) {
        super(context, DATABASE_VERSION);
    }

    @Override
    protected List<DaoBaseObject> getAllTableObjects() {
        List<DaoBaseObject> objectList = new ArrayList<>();

        // FIXME: Classes to CRUD in database
        objectList.add(new ExampleObject());
        // TODO: more classes...

        return objectList;
    }
}
