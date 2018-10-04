package com.leslie.javabase.fragmentproject.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public  abstract class FragmentActivity extends AppCompatActivity {
    protected FragmentManager mFragmentManager = getSupportFragmentManager();
    protected FragmentTransaction transaction = mFragmentManager.beginTransaction();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    protected FragmentManager getmFragmentManager() {
        return mFragmentManager;
    }

    public FragmentTransaction getTransaction() {
        return transaction;
    }
}
