# blank-dao
Simple access to SQLite database in Android

## Usage

Create a java class with a content like this:
```
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
```
Note in objects that you want save into database.

Create base class extended of BlankBaseDaoObject with and override getBlankDaoManager() method with your ExampleBlankDaoManager
```
public class ExampleBaseObject extends BlankBaseDaoObject {

    public ExampleBaseObject(Context ctx) {
        super(ctx);
    }

    @Override
    public BlankBaseDaoManager getBlankDaoManager() {
        return new ExampleBlankDaoManager(context);
    }
}
```

Create a CRUD class that extends base class created before
```
pubic class ExampleObject extends ExampleBaseObject {

    public String name;

    @BlankTransient
    public String noSavedObject;

    public ExampleObject(Context ctx) {
        super(ctx);
    }

}
```
Note that the @BlankTransient attributes will not stored in database. Stored attributes must be Objects.

Finally you could use like this:
```
ExampleObject newObject = new ExampleObject(getActivity());
newObject.name = "This is a name";
BlankDao.saveOrUpdate(newObject);

//...

BlankDao.delete(newObject);

//...

// List<ExampleObject> objects = BlankDao.getAll(new ExampleObject());
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
