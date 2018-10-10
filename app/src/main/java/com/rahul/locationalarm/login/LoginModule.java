package com.rahul.locationalarm.login;


import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    public LoginPresenter provideLoginPresenter() {
        return new LoginPresenter();
    }

    @Provides
    public LoginDAOImpl provideLoginDAO() {
        return new LoginDAOImpl();
    }
}
