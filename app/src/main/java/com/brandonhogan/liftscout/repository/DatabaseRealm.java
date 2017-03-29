package com.brandonhogan.liftscout.repository;

import io.realm.Realm;

public class DatabaseRealm {

    public DatabaseRealm() {
    }

    public Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }

    public void close() {
        getRealmInstance().close();
    }
}
