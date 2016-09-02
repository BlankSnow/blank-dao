# blank-dao
Simple access to SQLite database in Android

## Usage
We create the objects that we want to save in database, they must extend the class BlankDaoObject, for example:
```java
public class ExampleObject extends DaoBaseObject {

    public String  someString;
    public Boolean someBoolean;
    public Integer someInteger;

    @BlankTransient public String  noSavedString;
    @BlankTransient public Boolean noSavedBoolean;
    @BlankTransient public Integer noSavedInteger;

}
```
Note that the @BlankTransient annotation avoids save one field in database. Stored attributes must be Objects.

Create an SomeNameBlankDaoManager that extends BlankDaoManager with a content like this:
```java
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
```
Note that objectList contains all objects that you want save into database.

Finally you could use like this:
```java
ExampleDaoManager daoManager = new ExampleDaoManager(this);

ExampleObject newObject = new ExampleObject();
newObject.name = "This is a name";
daoManager.saveOrUpdate(newObject);

//...

daoManager.delete(obj);

//...

// List<ExampleObject> objectList = daoManager.getAll(new ExampleObject());
```

### License

```
Copyright 2016 Jose Casado

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
