package com.leslie.network_framework

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.leslie.network_framework.gson.MyResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val hadler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.setOnClickListener { v ->

            HttpHelper.obtain().post("http://v.juhe.cn/weather/index?&cityname=%E9%95%BF%E6%B2%99&key=fd0f609b22905a0a56a48d7cf59a558b",null , object: HttpCallback<MyResult>() {
                override fun onFailure(error: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onSuccess(result: MyResult?) {
                    hadler.post(Runnable {
                        Toast.makeText(this@MainActivity, result.toString(), Toast.LENGTH_SHORT).show()
                    })
                }
            })
        }


    }


}
