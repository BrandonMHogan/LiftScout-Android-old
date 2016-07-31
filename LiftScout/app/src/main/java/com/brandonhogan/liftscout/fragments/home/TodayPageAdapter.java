package com.brandonhogan.liftscout.fragments.home;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.Date;
import java.util.TimeZone;

public class TodayPageAdapter   {
  //  extends FragmentStatePagerAdapter
//        private final Cursor cursor;
//
//        private final int count;
//
//        public TodayPageAdapter( FragmentManager fm ) {
//            super( fm );
//            cursor = initCursorSomehow();
//            count = cursor.getCount();
//        }
//
//        @Override
//        public int getCount() {
//            return count;
//        }
//
//        @Override
//        public CharSequence getPageTitle( int position ) {
//            cursor.moveToPosition( position );
//            return cursor.getString( cursor.getColumnIndex( "title" ) );
//        }
//
//        @Override
//        public Fragment getItem( int position ) {
//            Bundle b = new Bundle();
//            cursor.moveToPosition( position );
//            b.putString( "someDate", cursor.getString( cursor.getColumnIndex( "someDate" ) ) );
//            b.putString( "someInt", cursor.getInt( cursor.getColumnIndex( "someInt" ) ) );
//            return Fragment.instantiate( getApp(), TodayFragment.class.getName(), b );
//        }
}