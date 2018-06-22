package com.example.xyzreader.di;

import com.example.xyzreader.XYZReader;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        AndroidSupportInjectionModule.class,
        ActivityBindingModule.class,
        RepositoryModule.class
})
public interface AppComponent extends AndroidInjector<XYZReader> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<XYZReader> {
    }
}
