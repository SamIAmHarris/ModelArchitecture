package com.bignerdranch.android.initialnerdmart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

/**
 * subscribe method expects a lambda as an argument and defines what to do once the
 * request completes
 *
 * Until .subscribe is called on an Observable, the Observable will do no networking, data
 * manipulation or any of the work defined. This is called a cold Observable.
 */
public class LoginFragment extends NerdMartAbstractFragment {

    @Bind(R.id.fragment_login_username)
    EditText mUsernameEditText;

    @Bind(R.id.fragment_login_password)
    EditText mPasswordEditText;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.fragment_login_login_button)
    public void handleLoginClick() {
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        addSubscription(authenticateSubscription(username, password));
    }

    public Subscription authenticateSubscription(String username, String password) {
        return mNerdMartServiceManager
                .authenticate(username, password)
                .compose(loadingTransformer())
                .subscribe(authenticated -> respondToAuthentication());
    }

    public void respondToAuthentication() {
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
