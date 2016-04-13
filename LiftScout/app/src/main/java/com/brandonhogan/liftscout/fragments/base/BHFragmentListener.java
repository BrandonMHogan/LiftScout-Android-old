package com.brandonhogan.liftscout.fragments.base;

public interface BHFragmentListener{
    void fragmentTransitionTo(BHFragment fragment);
    void fragmentTransitionTo(BHFragment fragment, int animIn, int animOut);
    void fragmentTransitionStarted();
    void fragmentTransitionEnded();
}

