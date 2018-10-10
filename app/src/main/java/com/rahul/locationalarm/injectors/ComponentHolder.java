package com.rahul.locationalarm.injectors;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Class to hold reference of Components
 */
public class ComponentHolder {

    private ApplicationComponent mApplicationComponent;

    private ComponentHolder() {

    }

    private static class SingletonHelper {
        private static final ComponentHolder INSTANCE = new ComponentHolder();
    }

    public static ComponentHolder getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public void setApplicationComponent(@NonNull Context context) {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(context))
                    .build();
        }
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

}
