package com.bignerdranch.android.initialnerdmart.inject;

import android.content.Context;

import com.bignerdranch.android.nerdmartservice.model.NerdDataSource;
import com.bignerdranch.android.nerdmartservice.model.NerdMartDataSourceInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SamMyxer on 4/7/16.
 */
@Module( includes = {NerdMartMockServiceModule.class, NerdMartCommonModule.class})
public class NerdMartMockApplicationModule {

    private Context mApplicationContext;

    public NerdMartMockApplicationModule(Context mApplicationContext) {
        mApplicationContext = mApplicationContext;
    }

    @Provides
    @Singleton
    public NerdMartDataSourceInterface providesNerdMartDataSourceInterface() {
        return new NerdDataSource();
    }

    @Provides
    public Context providesContext() {
        return mApplicationContext;
    }

}
