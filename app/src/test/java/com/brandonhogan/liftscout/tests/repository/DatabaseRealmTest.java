package com.brandonhogan.liftscout.tests.repository;

import com.brandonhogan.liftscout.BuildConfig;
import com.brandonhogan.liftscout.repository.DatabaseRealm;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.realm.Realm;
import io.realm.log.RealmLog;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = Config.NONE, sdk = 19)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@SuppressStaticInitializationFor("io.realm.internal.Util")
@PrepareForTest({Realm.class, RealmLog.class})
public class DatabaseRealmTest {

    private DatabaseRealm databaseRealm;

    @Rule
    public PowerMockRule rule = new PowerMockRule();
    private Realm mockRealm;

    @Before
    public void setUp() {
        mockStatic(RealmLog.class);
        mockStatic(Realm.class);

        Realm mockRealm = PowerMockito.mock(Realm.class);
        PowerMockito.when(Realm.getDefaultInstance()).thenReturn(mockRealm);

        this.mockRealm = mockRealm;
        databaseRealm = new DatabaseRealm();
    }

    @Test
    public void test_getRealmInstance() {
        assertThat(databaseRealm.getRealmInstance(), is(mockRealm));
    }

    @Test
    public void test_close() {
        databaseRealm.close();
    }
}
