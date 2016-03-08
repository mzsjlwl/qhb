package com.handsome.qhb.bean;

/**
 * Created by handsome on 2016/3/8.
 */
public class Room {
    int id;
    int rid;
    int roomGameNum;
    String roomName;
    int roomMember;
    String roomPassword;
    String roomCreater;
    String roomCreateTime;
    String roomEndTime;
    String roomState;
    String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getRoomGameNum() {
        return roomGameNum;
    }

    public void setRoomGameNum(int roomGameNum) {
        this.roomGameNum = roomGameNum;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomMember() {
        return roomMember;
    }

    public void setRoomMember(int roomMember) {
        this.roomMember = roomMember;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    public String getRoomCreater() {
        return roomCreater;
    }

    public void setRoomCreater(String roomCreater) {
        this.roomCreater = roomCreater;
    }

    public String getRoomCreateTime() {
        return roomCreateTime;
    }

    public void setRoomCreateTime(String roomCreateTime) {
        this.roomCreateTime = roomCreateTime;
    }

    public String getRoomEndTime() {
        return roomEndTime;
    }

    public void setRoomEndTime(String roomEndTime) {
        this.roomEndTime = roomEndTime;
    }

    public String getRoomState() {
        return roomState;
    }

    public void setRoomState(String roomState) {
        this.roomState = roomState;
    }
}
