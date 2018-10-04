package com.example.administrator.chattingrobot;

/**
 * Created by Administrator on 2017/12/27.
 */

public class Chatting {
    private String myTalk;
    private String robotTalk;
    private int myImageId;
    private int robotImageId;

    public Chatting(String robotTalk, int robotImageId) {
        this.robotTalk = robotTalk;
        this.robotImageId = robotImageId;
    }

    public String getMyTalk() {
        return myTalk;
    }

    public void setMyTalk(String myTalk) {
        this.myTalk = myTalk;
    }

    public String getRobotTalk() {
        return robotTalk;
    }

    public void setRobotTalk(String robotTalk) {
        this.robotTalk = robotTalk;
    }

    public int getMyImageId() {
        return myImageId;
    }

    public void setMyImageId(int myImageId) {
        this.myImageId = myImageId;
    }

    public int getRobotImageId() {
        return robotImageId;
    }

    public void setRobotImageId(int robotImageId) {
        this.robotImageId = robotImageId;
    }

    public Chatting(int myImageId, String myTalk) {
        this.myTalk = myTalk;
        this.myImageId = myImageId;

    }

}
