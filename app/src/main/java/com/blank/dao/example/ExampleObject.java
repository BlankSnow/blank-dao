package com.blank.dao.example;

import android.content.Context;

import com.blank.dao.annotations.BlankTransient;

public class ExampleObject extends ExampleBaseObject {

    public String name;

    @BlankTransient
    public String noSavedObject;

    public ExampleObject(Context ctx) {
        super(ctx);
    }

}
