package com.brandonhogan.liftscout.repository;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

public class DatabaseRealm {

    public DatabaseRealm() {
    }

    public Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }

    public <T extends RealmObject> T copyToRealmOrUpdate(T model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();
        return model;
    }

    public <T extends RealmObject> List<T> findAll(Class<T> clazz) {
        return getRealmInstance().where(clazz).findAll();
    }

    public void close() {
        getRealmInstance().close();
    }
}
