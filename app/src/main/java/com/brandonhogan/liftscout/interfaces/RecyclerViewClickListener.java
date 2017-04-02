package com.brandonhogan.liftscout.interfaces;

import android.view.View;

/**
 * Created by Brandon on 3/22/2017.
 * Description : This interface is used when passing click events from a
 * recyclerView to its parent fragment or activity
 */

public interface RecyclerViewClickListener {
        void onClick(View v, int position);
        void onLongClick(View v, int position);
        void onListUpdated(boolean isEmpty, boolean isClear);
}
