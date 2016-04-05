package com.bignerdranch.android.initialnerdmart.model;

import com.bignerdranch.android.nerdmartservice.service.payload.Product;
import com.bignerdranch.android.nerdmartservice.service.payload.User;

import java.util.List;
import java.util.UUID;

/**
 * Created by SamMyxer on 4/5/16.
 *
 * Will be injected by Dagger and be used within the ServiceManager object
 */
public class DataStore {

    private User mCachedUser;
    private List<Product> mCachedProducts;

    public UUID getCachedAuthToken() {
        return mCachedUser.getAuthToken();
    }

    public User getCachedUser() {
        return mCachedUser;
    }

    public void setCachedUser(User user) {
        mCachedUser = user;
    }

    public void setCachedProducts(List<Product> products) {
        mCachedProducts = products;
    }
}
