package com.example.mr_me.views.activities;

import java.util.Date;
import java.util.Objects;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MyRealmMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            // Version 1: Create the Transaction class schema
            schema.create("Transaction")
                    .addField("id", long.class, io.realm.FieldAttribute.PRIMARY_KEY)
                    .addField("type", String.class)
                    .addField("category", String.class)
                    .addField("paymentMethod", String.class)
                    .addField("description", String.class)
                    .addField("date", Date.class)
                    .addField("amount", double.class)
                    .addField("timestamp", long.class);
            oldVersion++;
        }

//        if (oldVersion == 1) {
//            // Example: Add new fields or modify schema for future versions
//            Objects.requireNonNull(schema.get("Transaction"))
//                    .addField("newField", String.class);
//            oldVersion++;
//        }
    }
}
