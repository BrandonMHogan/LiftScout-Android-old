package com.brandonhogan.liftscout.interfaces;

/**
 * Created by Brandon on 4/5/2017.
 * Description : Triggered from the navigation manager, used in fragments.
 *      Example usage : On detail edit views that prompt the user that they are leaving
 *      without saving.
 */

public interface OnBackPressListener {
    boolean onBackPress();
}
