package com.bignerdranch.android.initialnerdmart;

import android.os.Build;

import com.bignerdranch.android.initialnerdmart.inject.NerdMartApplicationModule;
import com.bignerdranch.android.initialnerdmart.inject.NerdMartMockApplicationModule;
import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.initialnerdmart.model.service.NerdMartServiceManager;
import com.bignerdranch.android.nerdmartservice.model.NerdMartDataSourceInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by SamMyxer on 4/7/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, constants = BuildConfig.class)
public class TestNerdMartServiceAuthentication {

    @Inject
    NerdMartServiceManager mNerdMartServiceManager;

    @Inject
    DataStore mDataStore;

    @Inject
    NerdMartDataSourceInterface mNerdMartDataSourceInterface;

    @Singleton
    @Component(modules = NerdMartMockApplicationModule.class)
    public interface TestNerdMartServiceAuthenticationComponent {
        TestNerdMartServiceAuthentication inject(
                TestNerdMartServiceAuthentication testNerdMartServiceManager);
    }

    @Before
    public void setup() {
        NerdMartMockApplicationModule nerdMartMockApplicationModule
                = new NerdMartMockApplicationModule(RuntimeEnvironment.application);
        DaggerTestNerdMartServiceAuthentication_TestNerdMartServiceAuthenticationComponent
                .builder()
                .nerdMartMockApplicationModule(nerdMartMockApplicationModule)
                .build()
                .inject(this);
    }

    @Test
    public void testDependencyInjectionWorked() {
        assertThat(mNerdMartServiceManager).isNotNull();
        assertThat(mDataStore).isNotNull();
        assertThat(mNerdMartDataSourceInterface).isNotNull();
    }

    @Test
    public void testAuthenticateMethodReturnsFalseWithInvalidCredentials() {
        TestSubscriber<Boolean> subscriber = new TestSubscriber<>();
        mNerdMartServiceManager.authenticate("johnnydoe", "WRONGPASSWORD")
                .subscribe(subscriber);
        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().get(0)).isEqualTo(false);
        assertThat(mDataStore.getCachedUser()).isEqualTo(null);
    }

    @Test
    public void testAuthenticateMethodReturnsTrueWithValidCredentials() {
        TestSubscriber<Boolean> subscriber = new TestSubscriber<>();
        mNerdMartServiceManager.authenticate("johnnydoe", "pizza")
                .subscribe(subscriber);
        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().get(0)).isEqualTo(true);
        assertThat(mDataStore.getCachedUser()).isEqualTo(mNerdMartDataSourceInterface.getUser());
    }

    @Test
    public void testSignOutRemovesUserObjects() {
        TestSubscriber<Boolean> subscriber = new TestSubscriber<>();
        mNerdMartServiceManager.authenticate("johnnydoe", "pizza")
                .subscribe(subscriber);
        subscriber.awaitTerminalEvent();
        TestSubscriber<Boolean> signoutSubscriber = new TestSubscriber<>();
        mNerdMartServiceManager.signout().subscribe(signoutSubscriber);
        signoutSubscriber.awaitTerminalEvent();
        assertThat(mDataStore.getCachedUser()).isEqualTo(null);
        assertThat(mDataStore.getCachedCart()).isEqualTo(null);
    }
}
