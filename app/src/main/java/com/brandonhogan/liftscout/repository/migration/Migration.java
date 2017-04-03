package com.brandonhogan.liftscout.repository.migration;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by Brandon on 4/3/2017.
 * Description :
 */

public class Migration implements RealmMigration {

    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {
        // During a migration, a DynamicRealm is exposed. A DynamicRealm is an untyped variant of a normal Realm, but
        // with the same object creation and query capabilities.
        // A DynamicRealm uses Strings instead of Class references because the Classes might not even exist or have been
        // renamed.

        // Access the Realm schema in order to create, modify or delete classes and their fields.
        RealmSchema schema = realm.getSchema();

        /************************************************
         // Version 0
         class Person
         @Required
         String firstName;
         @Required
         String lastName;
         int    age;
         // Version 1
         class Person
         @Required
         String fullName;            // combine firstName and lastName into single field.
         int age;
         ************************************************/
        // Migrate from version 0 to version 1
        if (oldVersion == 0) {
            RealmObjectSchema categorySchema = schema.get("Category");


            oldVersion++;


//
//            // Combine 'firstName' and 'lastName' in a new field called 'fullName'
//            personSchema
//                    .addField("fullName", String.class, FieldAttribute.REQUIRED)
//                    .transform(new RealmObjectSchema.Function() {
//                        @Override
//                        public void apply(DynamicRealmObject obj) {
//                            obj.set("fullName", obj.getString("firstName") + " " + obj.getString("lastName"));
//                        }
//                    })
//                    .removeField("firstName")
//                    .removeField("lastName");
//            oldVersion++;
        }
    }
}
