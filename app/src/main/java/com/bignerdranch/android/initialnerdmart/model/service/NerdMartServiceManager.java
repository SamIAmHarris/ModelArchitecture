package com.bignerdranch.android.initialnerdmart.model.service;

import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface;
import com.bignerdranch.android.nerdmartservice.service.payload.Cart;
import com.bignerdranch.android.nerdmartservice.service.payload.Product;

import java.util.UUID;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SamMyxer on 4/5/16.
 *
 * Api methods from NerdMartService return observables
 *
 * RxJava provides solutions to the problems of handling background/main thread, filtering,
 * and manipulating data
 *
 * Lambda function can replace anon inner classes
 *
 * Lambda function referred to as anonymous function
 *
 * map method:
 * common operator used in RxJava
 * takes an argument and evaluates a method on it, returning the results of that evaluation
 * this case is take a user and returns a boolean
 *
 * Scheduler:
 * allows us to indicate which thread the work of the Observable should be done on, and which
 * thread to use for the results.
 *
 * doOnNext is called after new data is emitted on the Observable
 *
 * :: is the method reference syntax which can be used when method takes 1 or no parameters
 * to simplify code and remove the verbosity
 *  doOnNext(user -> mDataStore.setCachedUser(user)) == doOnNext(mDataStore::setCachedUser)
 */
public class NerdMartServiceManager {

    private NerdMartServiceInterface mServiceInterface;
    private DataStore mDataStore;

    private final Observable.Transformer<java.util.Observable, java.util.Observable>
            mSchedulersTransformer = observable ->
            observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    public NerdMartServiceManager(NerdMartServiceInterface serviceInterface, DataStore dataStore) {
        mServiceInterface = serviceInterface;
        mDataStore = dataStore;
    }

    @SuppressWarnings("unchecked")
    private <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) mSchedulersTransformer;
    }

    public Observable<Boolean> authenticate(String username, String password) {
        //With Lambdas
        return mServiceInterface.authenticate(username, password)
                .doOnNext(mDataStore::setCachedUser)
                .map(user -> user != null)
                .compose(applySchedulers());
    }

        //With anon inner class
//        return mServiceInterface.authenticate(username, password)
//                .map(new Func1<User, Boolean>() {
//                    @Override
//                    public Boolean call(User user) {
//                        return user != null;
//                    }
//                });

    /**
     * Wraps the DataStore's cached auth token in an Observable
     * Now it can be combined with other Observables easily
     */
    public Observable<UUID> getToken() {
        return Observable.just(mDataStore.getCachedAuthToken());
    }

    /**
     * Holy RxJava/Java 8 Ballz
     *
     * flatMap: Returns an Observable that emits items based on applying a function that you
     * supply to each item emitted by the source Observable, where that function returns
     * an Observable, and then merging those resulting Observables and emitting the result of
     * this merger.
     *
     * flatMap is like map except it unwraps one level of Observable from the result. This makes it
     * so you can get to the actual items in the observable instead of the observables themselves.
     *
     * This method is a bit confusing but it:
     * 1. gets a list of products from service interface
     * 2. caches those products
     * 3. getting rid of the array list with Observable::from and turning it into an Observable<Product>,
     * but not sure why it is doing this
     * 4. Putting the network work on the io thread and putting the observe on work on the main thread
     *
     */
    public Observable<Product> getProducts() {
        return getToken().flatMap(mServiceInterface::requestProducts)
                .doOnNext(mDataStore::setCachedProducts)
                .flatMap(Observable::from)
                .compose(applySchedulers());
    }

    public Observable<Cart> getCart() {
        return getToken().flatMap(mServiceInterface::fetchUserCart)
                .doOnNext(mDataStore::setCachedCart)
                .compose(applySchedulers());
    }

    public Observable<Boolean> postProductToCart(final Product product) {
        return getToken()
                .flatMap(uuid -> mServiceInterface.addProductToCart(uuid, product))
                .compose(applySchedulers());
    }

    public Observable<Boolean> signout() {
        mDataStore.clearCache();
        return mServiceInterface.signout();
    }
}
