package com.blank.dao.example;

import com.blank.dao.DaoBaseObject;
import com.blank.dao.annotations.BlankTransient;

public class ExampleObject extends DaoBaseObject {

    public String  someString;
    public Boolean someBoolean;
    public Integer someInteger;

    @BlankTransient public String  noSavedString;
    @BlankTransient public Boolean noSavedBoolean;
    @BlankTransient public Integer noSavedInteger;

}
