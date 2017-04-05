package com.brandonhogan.liftscout.injection.scopes;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Brandon on 4/5/2017.
 * Description :
 */

@Scope
@Retention(RUNTIME)
public @interface PerActivity {}
