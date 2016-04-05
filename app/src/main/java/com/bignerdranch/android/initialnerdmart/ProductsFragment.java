package com.bignerdranch.android.initialnerdmart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.nerdmartservice.service.payload.Product;

import java.util.List;

import rx.Subscription;
import timber.log.Timber;

/**
 *
 * Checking with Timber to see if we set up injection correctly
 *
 */
public class ProductsFragment extends NerdMartAbstractFragment {
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Timber.i("injected: " + mNerdMartServiceManager);
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        updateUI();
        return view;
    }

    /**
     * Add subscription to the list of subscriptions so we do not get NPE after fragment is destroyed
     *
     * Make getProducts call and log if you successfully received them
     */
    private void updateUI() {
        addSubscription(getProductsSubscription());
    }

    public Subscription getProductsSubscription() {
        return mNerdMartServiceManager
                .getProducts()
                .toList()
                .compose(loadingTransformer())
                .subscribe(this::respondToGetProducts);

        //this = products
    }

    public void respondToGetProducts(List<Product> products) {
        Timber.i("received products: " + products);
    }
}
