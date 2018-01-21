package com.ttdevs.dagger;

import dagger.Component;

/**
 * Created by ttdevs on 22/01/2018.
 */
@Component(modules = A01SimpleModule.class)
public interface A01SimpleComponent {
    void inject(MainActivity activity);
}
