package com.leslie.kotlintest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    val TAG = "Leslie"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        printInfo("Hello Kotlin")

//        printInfo(repalce())

//        printNum()

        val person = Person("李玮斌")

        person.age = -100


        printInfo(person.toString())
    }

    fun printInfo(content: String) {
        Log.d(TAG, "你输入的内容是 $content")

        Log.d(TAG, "你输入的内容长度是 ${content.length}")

    }

    fun repalce(): String {
        var a = 1
        // 模板中的简单名称：
        val s1 = "a is $a"

        a = 2
        // 模板中的任意表达式：
        val s2 = "${s1.replace("is", "was")}, but now is $a"

        return s2
    }

    fun printNum() {
        for (num in 100 downTo 1 step 5) {
            val i = num % 2
            when (i) {
                0 -> Log.d(TAG, "$num 是偶数")
                1 -> Log.d(TAG, "$num 是奇数")
                else -> Log.d(TAG, "$num")
            }

        }
    }
}
