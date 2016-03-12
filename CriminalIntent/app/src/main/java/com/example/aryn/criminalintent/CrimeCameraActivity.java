package com.example.aryn.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by user on 12.03.2016.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}
