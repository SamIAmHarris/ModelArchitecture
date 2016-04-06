package com.bignerdranch.android.initialnerdmart;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.android.initialnerdmart.databinding.FragmentProductsBinding;
import com.bignerdranch.android.nerdmartservice.service.payload.Product;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 *
 * Checking with Timber to see if we set up injection correctly
 *
 */
public class ProductsFragment extends NerdMartAbstractFragment {

    private ProductRecyclerViewAdapter mAdapter;
    private FragmentProductsBinding mFragmentsProductsBinding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Timber.i("injected: " + mNerdMartServiceManager);
        mFragmentsProductsBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_products, container, false);
        ProductRecyclerViewAdapter.AddProductClickEvent addProductClickEvent =
                this::postProductToCart;

        //this = product

        mAdapter = new ProductRecyclerViewAdapter(Collections.EMPTY_LIST,
                getActivity(), addProductClickEvent);
        setupAdapter();
        updateUI();
        return mFragmentsProductsBinding.getRoot();
    }

    private void setupAdapter() {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity());
        mFragmentsProductsBinding.fragmentProductsRecyclerView
                .setLayoutManager(linearLayoutManager);
        mFragmentsProductsBinding.fragmentProductsRecyclerView
                .setAdapter(mAdapter);
    }

    /**
     * Add subscription to the list of subscriptions so we do not get NPE after fragment is destroyed
     *
     * Make getProducts call and log if you successfully received them
     */
    private void updateUI() {
        addSubscription(getProductsSubscription());
    }

    /**
     * A whole lot of IDK what is going on in this method
     */
    private void postProductToCart(Product product) {
        Observable<Boolean> cartSuccessObservable = mNerdMartServiceManager
                .postProductToCart(product)
                .compose(loadingTransformer())
                .cache();

        Subscription cartUpdateNotificationObservable = cartSuccessObservable
                .subscribe(aBoolean -> {
                    int message = aBoolean ? R.string.product_add_success_message :
                            R.string.product_add_failure_message;
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                });

        addSubscription(cartUpdateNotificationObservable);
        addSubscription(cartSuccessObservable.filter(aBoolean -> aBoolean)
                .subscribeOn(Schedulers.newThread())
                .flatMap(aBoolean -> mNerdMartServiceManager.getCart())
                .subscribe(cart -> {
                    ((NerdMartAbstractActivity) getActivity()).updateCartStatus(cart);
                    updateUI();
                }));
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
        mAdapter.setProducts(products);
        mAdapter.notifyDataSetChanged();
    }
}
