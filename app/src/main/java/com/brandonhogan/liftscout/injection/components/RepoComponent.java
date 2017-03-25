package com.brandonhogan.liftscout.injection.components;

import com.brandonhogan.liftscout.managers.RecordsManager;
import com.brandonhogan.liftscout.injection.module.DatabaseModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Brandon on 3/14/2017.
 * Description :
 */

@Singleton
@Component(modules = {DatabaseModule.class})
public interface RepoComponent {

    void inject(RecordsManager manager);

}
