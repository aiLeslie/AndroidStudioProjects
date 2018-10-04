package com.example.administrator.actionbardemo;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        


//        ActionBar actionBar = this.getActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        ActionBar.Tab tab = actionBar.newTab();
//        tab.setText("tab1");
//        tab.setTabListener(this);
//        actionBar.addTab(tab);
//        tab = actionBar.newTab();
//        tab.setText("tab2");
//        tab.setTabListener(this);
//        actionBar.addTab(tab);
        //actionBar.addTab(actionBar.newTab().setText("tab2").setTabListener(this));

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemPayly:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Main2Activity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        Toast.makeText(this, tab.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        Toast.makeText(this, tab.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        Toast.makeText(this, tab.getText(), Toast.LENGTH_SHORT).show();
    }
}

class myAdapter extends ArrayAdapter<View> {

    public myAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public myAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public myAdapter(@NonNull Context context, int resource, @NonNull View[] objects) {
        super(context, resource, objects);
    }

    public myAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull View[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public myAdapter(@NonNull Context context, int resource, @NonNull List<View> objects) {
        super(context, resource, objects);
    }

    public myAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<View> objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
