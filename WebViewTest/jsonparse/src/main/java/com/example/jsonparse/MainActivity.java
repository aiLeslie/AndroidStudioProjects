package com.example.jsonparse;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0)
                textView.setText((String) msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.buttonStart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText = (EditText) findViewById(R.id.editText);
                textView = (TextView) findViewById(R.id.textView);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        String city = null;
                        Reader reader = null;
                        try {
                            city = URLEncoder.encode(editText.getText().toString(), "utf-8");
                            // 拼地址
                            String apiUrl = String.format("http://www.sojson.com/open/api/weather/json.shtml?city=%s", city);
                            URL url = new URL(apiUrl);
                            StringBuffer content = new StringBuffer();
                            // 将InputStream转换成Reader的套路:使用装饰器类InputSreamrReader
                            reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
                            String line = null;
                            while ((line = ((BufferedReader) reader).readLine()) != null) {
                                content.append(line);
                                content.append(System.getProperty("line.separator"));
                            }
                            System.out.println(content);
                            JSONObject jsonObject = new JSONObject(content.toString());
                            content.setLength(0);
                            content.append("Date: " + jsonObject.getString("date"));
                            content.append(System.getProperty("line.separator"));
                            content.append("City: " + jsonObject.getString("city"));
                            content.append(System.getProperty("line.separator"));

                            JSONObject data = jsonObject.getJSONObject("data");
                            content.append("Temptrue: " + data.getString("wendu"));
                            content.append(System.getProperty("line.separator"));
                            content.append("humidity: " + data.getString("shidu"));
                            content.append(System.getProperty("line.separator"));
                            content.append("PM2.5: " + data.getInt("pm25"));
                            content.append(System.getProperty("line.separator"));
                            content.append("PM10: " + data.getInt("pm10"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Quality: " + data.getString("quality"));
                            content.append(System.getProperty("line.separator"));


                            JSONArray forecastArray = data.getJSONArray("forecast");
                            JSONObject today = forecastArray.getJSONObject(0);
                            content.append("Date: " + today.getString("date"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Sunrise: " + today.getString("sunrise"));
                            content.append(System.getProperty("line.separator"));
                            content.append("HighTemp: " + today.getString("high"));
                            content.append(System.getProperty("line.separator"));
                            content.append("LowTemp: " + today.getString("low"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Sunset: " + today.getString("sunset"));
                            content.append(System.getProperty("line.separator"));
                            content.append("FX: " + today.getString("fx"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Type: " + today.getString("type"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Notice: " + today.getString("notice"));
                            content.append(System.getProperty("line.separator"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Forecast");
                            content.append(System.getProperty("line.separator"));
                            today = forecastArray.getJSONObject(1);
                            content.append("Date: " + today.getString("date"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Sunrise: " + today.getString("sunrise"));
                            content.append(System.getProperty("line.separator"));
                            content.append("HighTemp: " + today.getString("high"));
                            content.append(System.getProperty("line.separator"));
                            content.append("LowTemp: " + today.getString("low"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Sunset: " + today.getString("sunset"));
                            content.append(System.getProperty("line.separator"));
                            content.append("FX: " + today.getString("fx"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Type: " + today.getString("type"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Notice: " + today.getString("notice"));
                            content.append(System.getProperty("line.separator"));

                            content.append(System.getProperty("line.separator"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Forecast");
                            content.append(System.getProperty("line.separator"));
                            today = forecastArray.getJSONObject(2);
                            content.append("Date: " + today.getString("date"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Sunrise: " + today.getString("sunrise"));
                            content.append(System.getProperty("line.separator"));
                            content.append("HighTemp: " + today.getString("high"));
                            content.append(System.getProperty("line.separator"));
                            content.append("LowTemp: " + today.getString("low"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Sunset: " + today.getString("sunset"));
                            content.append(System.getProperty("line.separator"));
                            content.append("FX: " + today.getString("fx"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Type: " + today.getString("type"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Notice: " + today.getString("notice"));
                            content.append(System.getProperty("line.separator"));

                            content.append(System.getProperty("line.separator"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Forecast");
                            content.append(System.getProperty("line.separator"));
                            today = forecastArray.getJSONObject(3);
                            content.append("Date: " + today.getString("date"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Sunrise: " + today.getString("sunrise"));
                            content.append(System.getProperty("line.separator"));
                            content.append("HighTemp: " + today.getString("high"));
                            content.append(System.getProperty("line.separator"));
                            content.append("LowTemp: " + today.getString("low"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Sunset: " + today.getString("sunset"));
                            content.append(System.getProperty("line.separator"));
                            content.append("FX: " + today.getString("fx"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Type: " + today.getString("type"));
                            content.append(System.getProperty("line.separator"));
                            content.append("Notice: " + today.getString("notice"));
                            content.append(System.getProperty("line.separator"));
                            Message message = handler.obtainMessage();
                            message.what = 0;
                            message.obj = content.toString();
                            handler.sendMessage(message);


                        } catch (Exception e) {
                            e.printStackTrace();

                        } finally {
                            try {
                                if (reader != null)
                                    reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();


            }
        });
        button = (Button) findViewById(R.id.buttonClear);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText);
                editText.setText("");
            }
        });
    }
}
