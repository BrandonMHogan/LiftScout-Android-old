package com.brandonhogan.liftscout.fragments.base;

public abstract class BHDetailFragment extends BHFragment {

    public BHDetailFragment(String title) {
        super(title);
    }

    public BHDetailFragment() {
        super();
    }

    @Override
    public FragmentType fragmentType() {
        return FragmentType.Detail;
    }
}
