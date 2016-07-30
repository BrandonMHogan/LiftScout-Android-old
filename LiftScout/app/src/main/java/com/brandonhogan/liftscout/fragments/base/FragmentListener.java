package com.brandonhogan.liftscout.fragments.base;

public interface FragmentListener {
    void fragmentTransitionTo(BaseFragment fragment);
    void fragmentTransitionTo(BaseFragment fragment, int animIn, int animOut);
    void fragmentTransitionStarted();
    void fragmentTransitionEnded();
}

