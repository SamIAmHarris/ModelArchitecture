package com.bignerdranch.android.initialnerdmart;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.bignerdranch.android.initialnerdmart.databinding.FragmentLoginBinding;

import rx.Subscription;

/**
 * subscribe method expects a lambda as an argument and defines what to do once the
 * request completes
 *
 * Until .subscribe is called on an Observable, the Observable will do no networking, data
 * manipulation or any of the work defined. This is called a cold Observable.
 */
public class LoginFragment extends NerdMartAbstractFragment {

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        FragmentLoginBinding fragmentLoginBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_login,
                container,
                false);

        fragmentLoginBinding.setLoginButtonClickListener(v -> {
            String username = fragmentLoginBinding.fragmentLoginUsername.getText().toString();
            String password = fragmentLoginBinding.fragmentLoginPassword.getText().toString();
            authenticateSubscription(username, password);
        });

        return fragmentLoginBinding.getRoot();
    }


    public Subscription authenticateSubscription(String username, String password) {
        return mNerdMartServiceManager
                .authenticate(username, password)
                .compose(loadingTransformer())
                .subscribe(this::respondToAuthentication);

                //Lamdas would be more like this
                //.subscribe(authenticated -> respondToAuthentication(authenticated));

    }

    public void respondToAuthentication(boolean authenticated) {
        if(!authenticated) {
            Toast.makeText(getActivity(),
                    R.string.auth_failure_toast,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getActivity(),
                R.string.auth_success_toast,
                Toast.LENGTH_SHORT).show();
        Intent intent = ProductsActivity.newIntent(getActivity());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

}
