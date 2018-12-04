package com.example.administrator.bluetoothtest.activities.checkup.model;


import java.util.HashMap;

public class Database extends AbstractDatabase<HashMap<String, Object>> {
    private static Database _instance;

    private Database() {

    }

    public static Database getInstance() {
        if (_instance == null) {
            _instance = new Database();
        }
        return _instance;
    }

    /**
     * 初始化数据库
     */
    @Override
    public void initBase() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("text", "环境温度监测");
        map.put("Y_MIN", 0f);
        map.put("Y_MAX", 50f);
        map.put("Y_DESC", "环境温度 / °C");
        map.put("Y_UNIT", 10f);

        map.put("X_MIN",0f );
        map.put("X_MAX", 10f);
        map.put("X_DESC", "时间 / S");
        map.put("X_UNIT", 1f);
        updataList(map, AbstractDatabase.ADD_DATA);

        map.put("text", "环境温度监测");
        map.put("Y_MIN", 0f);
        map.put("Y_MAX", 50f);
        map.put("Y_DESC", "环境温度 / °C");
        map.put("Y_UNIT", 10f);

        map.put("X_MIN",0f );
        map.put("X_MAX", 10f);
        map.put("X_DESC", "时间 / S");
        map.put("X_UNIT", 1f);
        updataList(map, AbstractDatabase.ADD_DATA);

        map.put("X_MIN",0f );
        map.put("X_MAX", 10f);
        map.put("X_DESC", "时间 / S");
        map.put("X_UNIT", 1f);
        updataList(map, AbstractDatabase.ADD_DATA);

        map.put("X_MIN",0f );
        map.put("X_MAX", 10f);
        map.put("X_DESC", "时间 / S");
        map.put("X_UNIT", 1f);
        updataList(map, AbstractDatabase.ADD_DATA);
    }

    /**
     * 获得数据库中的数据
     *
     * @param listener
     */
    @Override
    public void fetchData(OnFetchListener listener) {
        if (listener != null) {
            listener.onFetch(dataList);
        }
    }


}
