package com.leslie.codebase.router_api;

import android.app.Activity;

import java.util.HashMap;


public interface Connectable {
    public void load(HashMap<String, Class<? extends Activity>> map);
}
