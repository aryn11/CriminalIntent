package com.example.aryn.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by aryn on 06.03.16.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
