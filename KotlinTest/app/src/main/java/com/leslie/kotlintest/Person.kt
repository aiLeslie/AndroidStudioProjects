package com.leslie.kotlintest

class Person {
    var name = ""
    var id = -1
        get() = field                // 后端变量
        set(value) {
            field =
                    if (value > 0) {       // 如果传入的值大于 0 返回该值
                        value
                    } else {
                        -1         // 否则返回 -1
                    }
        }

    var age = 0
        get() = field                // 后端变量
        set(value) {
            field =
                    if (value > 0) {       // 如果传入的值大于 0 返回该值
                        value
                    } else {
                        -1         // 否则返回 -1
                    }
        }

    constructor(name: String, id: Int) {
        this.name = name
        this.id = id
    }

    constructor(name: String) {
        this.name = name
    }

    override fun toString(): String {
        return "我的名字是${this.name}, 今年${this.age}岁啦!"
    }


}