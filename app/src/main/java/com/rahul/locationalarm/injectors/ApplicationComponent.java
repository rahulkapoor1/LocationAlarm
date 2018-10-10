package com.rahul.locationalarm.injectors;

import com.rahul.locationalarm.dashboard.alarms.AlarmDAOImpl;
import com.rahul.locationalarm.location.LocationDAOImpl;
import com.rahul.locationalarm.location.LocationPostService;
import com.rahul.locationalarm.location.SavedLocationPresenterImpl;
import com.rahul.locationalarm.login.LoginDAOImpl;
import com.rahul.locationalarm.login.LoginPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(LoginPresenter target);
    void inject(LocationPostService target);
    void inject(LoginDAOImpl target);
    void inject(AlarmDAOImpl target);
    void inject(LocationDAOImpl target);
    void inject(SavedLocationPresenterImpl target);
}