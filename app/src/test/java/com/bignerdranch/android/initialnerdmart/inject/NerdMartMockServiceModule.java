package com.bignerdranch.android.initialnerdmart.inject;

import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.initialnerdmart.model.service.NerdMartServiceManager;
import com.bignerdranch.android.nerdmartservice.model.NerdMartDataSourceInterface;
import com.bignerdranch.android.nerdmartservice.service.NerdMartService;
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

/**
 * Created by SamMyxer on 4/7/16.
 */
@Module
public class NerdMartMockServiceModule {

    @Provides
    @Singleton
    NerdMartServiceManager providesNerdMartServiceManager(
            NerdMartServiceInterface nerdMartServiceInterface, DataStore dataStore) {
        return new NerdMartServiceManager(nerdMartServiceInterface, dataStore,
                Schedulers.immediate());
    }

    @Provides
    @Singleton
    NerdMartServiceInterface providesNerdMartServiceInterface(
            NerdMartDataSourceInterface dataSourceInterface) {
        return new NerdMartService(dataSourceInterface);
    }
}
