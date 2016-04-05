package com.bignerdranch.android.initialnerdmart.inject;

import com.bignerdranch.android.initialnerdmart.NerdMartAbstractFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by SamMyxer on 4/5/16.
 *
 * Component is the bridge between a Module, and the instantiation of classes the Module
 * provides in your Application.
 *
 * List the classes that we intend to target with dependency injection
 *
 * @Component annotation marks the NerdMartComponent as a Component in the Dagger 2 framework
 * Also configures which modules will be used with the Component defined
 *
 * It is an interface not a class. Dagger 2 will generate a concrete version of NerdMartComponent
 * for us on compilation
 */
@Singleton
@Component(modules = {NerdMartApplicationModule.class})
public interface NerdMartComponent {
    void inject(NerdMartAbstractFragment fragment);
}
