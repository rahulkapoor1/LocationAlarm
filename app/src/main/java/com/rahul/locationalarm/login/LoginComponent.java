package com.rahul.locationalarm.login;

import dagger.Component;

@Component(modules = LoginModule.class)
public interface LoginComponent {
    void inject(LoginActivity target);
    void inject(LoginDetailSaveAsync target);
}
