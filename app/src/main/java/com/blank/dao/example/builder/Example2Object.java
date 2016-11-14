package com.blank.dao.example.builder;

import com.blank.dao.DaoBaseObject;
import com.blank.dao.annotations.BlankTransient;

public class Example2Object extends DaoBaseObject {

    private final String someString;
    private final Boolean someBoolean;
    private final Integer someInteger;

    @BlankTransient public String  noSavedString;
    @BlankTransient public Boolean noSavedBoolean;
    @BlankTransient public Integer noSavedInteger;

    private Example2Object() {
        this.someString = null;
        this.someBoolean = null;
        this.someInteger = null;
    }

    private Example2Object(ExampleObjectMyBuilder builder) {
        super();
        this.someString = builder.someString;
        this.someBoolean = builder.someBoolean;
        this.someInteger = builder.someInteger;
    }

    public Boolean getSomeBoolean() {
        return someBoolean;
    }

    public Integer getSomeInteger() {
        return someInteger;
    }

    public String getSomeString() {
        return someString;
    }

    public static class ExampleObjectMyBuilder {
        private String someString;
        private Boolean someBoolean;
        private Integer someInteger;

        public Example2Object build() {
            return new Example2Object(this);
        }

        public ExampleObjectMyBuilder setSomeString(String someString) {
            this.someString = someString;
            return this;
        }

        public ExampleObjectMyBuilder setSomeBoolean(Boolean someBoolean) {
            this.someBoolean = someBoolean;
            return this;
        }

        public ExampleObjectMyBuilder setSomeInteger(Integer someInteger) {
            this.someInteger = someInteger;
            return this;
        }
    }
}
