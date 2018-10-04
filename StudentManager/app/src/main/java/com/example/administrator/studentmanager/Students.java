package com.example.administrator.studentmanager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 学生管理类
 * 项目名称：StudentManage
 * 类名称：Students
 * 类描述：
 * 创建人：李玮斌
 * 创建时间：2017年11月17日 下午3:10:49
 * @version 1.0
 */
class Students implements Serializable {
    public static final String savePath = "src/com/bin/javabase/data.dat";
    public static List<Students> list= new ArrayList();


    public Students(String name, long number, String sex, String password) {
        super();
        this.name = name;
        this.number = number;
        this.sex = sex;
        this.password = password;
        list.add(this);
    }
    private String name;
    private long number;
    private String sex;
    private String password;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getNumber() {
        return number;
    }
    public void setNumber(long number) {
        this.number = number;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public static boolean sign(Students student) {
        list.add(student);
        return true;

    }
    public static void saveData() {
        ObjectOutputStream out =  null;

        try {
            out =  new ObjectOutputStream(new FileOutputStream(savePath));
            out.writeObject(list);
            System.out.println("~保存数据成功~");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static void loadData() {
        ObjectInputStream in =  null;

        try {
            in =  new ObjectInputStream(new FileInputStream(savePath));
            list = (List<Students>)in.readObject();
            System.out.println("~加载数据成功~");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
