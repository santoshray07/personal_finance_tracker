package com.example.mr_me.views.activities;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MyRealmMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        // Migration logic for each version
        if (oldVersion == 0) {
            // Version 1: Add the "Transaction" class
            schema.create("Transaction")
                    .addField("id", long.class) // Primary key
                    .addField("type", String.class)
                    .addField("category", String.class)
                    .addField("paymentMethod", String.class)
                    .addField("description", String.class)
                    .addField("date", Date.class)
                    .addField("amount", double.class)
                    .addField("timestamp", long.class);
            oldVersion++;
        }

        // Add more migrations as you increment schema versions
        if (oldVersion == 1) {
            // Example: Add a new field to an existing class
            schema.get("Transaction")
                    .addField("newField", String.class); // Add a new field
            oldVersion++;
        }
    }
}
