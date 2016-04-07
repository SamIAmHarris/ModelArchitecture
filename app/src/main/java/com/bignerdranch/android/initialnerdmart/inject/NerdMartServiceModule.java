package com.bignerdranch.android.initialnerdmart.inject;

import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.initialnerdmart.model.service.NerdMartServiceManager;
import com.bignerdranch.android.nerdmartservice.service.NerdMartService;
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by SamMyxer on 4/7/16.
 */
@Module
public class NerdMartServiceModule {

    @Provides
    @Singleton
    NerdMartServiceManager providesNerdMartServiceManager(
            NerdMartServiceInterface serviceInterface, DataStore dataStore) {
        return new NerdMartServiceManager(serviceInterface, dataStore,
                AndroidSchedulers.mainThread());
    }

    @Provides
    @Singleton
    NerdMartServiceInterface providesNerdMartServiceInterface() {
        return new NerdMartService();
    }
}
