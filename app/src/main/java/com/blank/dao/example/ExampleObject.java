package com.blank.dao.example;

import android.content.Context;

import com.blank.dao.BlankBaseDaoManager;
import com.blank.dao.BlankBaseDaoObject;
import com.blank.dao.annotations.BlankTransient;

public class ExampleObject extends BlankBaseDaoObject {

    public String name;

    @BlankTransient
    public String noSavedObject;

    public ExampleObject(Context ctx) {
        super(ctx);
    }

    @Override
    public BlankBaseDaoManager getBlankDaoManager() {
        return new ExampleBlankDaoManager(context);
    }
}
