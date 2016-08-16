package com.blank.dao.example;

import android.content.Context;

import com.blank.dao.BlankBaseDaoManager;
import com.blank.dao.BlankBaseDaoObject;

public class ExampleBaseObject extends BlankBaseDaoObject {

    public ExampleBaseObject(Context ctx) {
        super(ctx);
    }

    @Override
    public BlankBaseDaoManager getBlankDaoManager() {
        return new ExampleBlankDaoManager(context);
    }
}
