package com.bignerdranch.android.initialnerdmart.inject;

import android.content.Context;

import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.initialnerdmart.viewmodel.NerdMartViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SamMyxer on 4/7/16.
 */
@Module
public class NerdMartCommonModule {

    @Provides
    NerdMartViewModel providesNerdMartViewModel(
            Context context, DataStore dataStore) {

        return new NerdMartViewModel(context, dataStore.getCachedCart(),
                dataStore.getCachedUser());
    }

    @Provides
    @Singleton
    DataStore providesDataStore() {
        return new DataStore();
    }
}
