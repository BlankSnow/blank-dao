package com.blank.dao.example;

import android.content.Context;

import com.blank.dao.BlankBaseDaoManager;
import com.blank.dao.BlankBaseDaoObject;

import java.util.ArrayList;
import java.util.List;

public class ExampleBlankDaoManager extends BlankBaseDaoManager {

    private static final int DATABASE_VERSION = 1;

    public ExampleBlankDaoManager(Context context) {
        super(context, DATABASE_VERSION);
    }

    @Override
    protected List<BlankBaseDaoObject> getAllTableObjects() {
        List<BlankBaseDaoObject> list = new ArrayList<>();

        // FIXME: Classes to CRUD in database
        list.add(new ExampleObject(context));
        // TODO: more classes...

        return list;
    }
}
