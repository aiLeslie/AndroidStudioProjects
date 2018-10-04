package com.leslie.codebase.arouterproject.router;

import com.leslie.codebase.arouterproject.activities.MainActivity;
import com.leslie.codebase.router_api.Connectable;

import java.util.HashMap;

public class Main$$Router implements Connectable {
    @Override
    public void load(HashMap map) {
        map.put("main", MainActivity.class);
    }
}
