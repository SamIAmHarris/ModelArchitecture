package com.bignerdranch.android.initialnerdmart;

import android.app.Application;
import android.content.Context;

import com.bignerdranch.android.initialnerdmart.inject.DaggerNerdMartComponent;
import com.bignerdranch.android.initialnerdmart.inject.NerdMartApplicationModule;
import com.bignerdranch.android.initialnerdmart.inject.NerdMartComponent;

import timber.log.Timber;

/**
 * Created by SamMyxer on 4/5/16.
 *
 * Static inner class initializer so we only access it from this class
 */
public class NerdMartApplication extends Application {

    private NerdMartComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        buildComponentAndInject();
    }

    //Set mComponent to the component created from the initializer
    private void buildComponentAndInject() {
        mComponent = DaggerComponentInitializer.init(this);
    }

    public static NerdMartComponent component(Context context) {
        return ((NerdMartApplication)
                context.getApplicationContext()).getComponent();
    }

    public NerdMartComponent getComponent() {
        return mComponent;
    }

    //Inner class because only want it used in the NerdMartApplication class
    private final static class DaggerComponentInitializer {
        /**Calls builder method on generated DaggerNerdMartComponent class
         *
         * build method returns a NerdMartComponent: checks if NerdMartAppModule is null
         *
         * builder: sets NerdMartApplicationModule
         *
         **/
        public static NerdMartComponent init(NerdMartApplication app) {
            return DaggerNerdMartComponent.builder()
                    .nerdMartApplicationModule(new NerdMartApplicationModule(app))
                    .build();

        }
    }
}
