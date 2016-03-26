package com.handsome.qhb.bean;

import java.io.Serializable;

/**
 * Created by handsome on 2016/3/8.
 */
public class Room implements Serializable {
    int rid;
    int sortId;
    int roomGameNum;
    String roomName;
    String roomMember;
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

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
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

    public String getRoomMember() {
        return roomMember;
    }

    public void setRoomMember(String roomMember) {
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

    public Room(){

    }

    public Room(int rid, int sortId, int roomGameNum, String roomName, String roomMember, String roomPassword, String roomCreater, String roomCreateTime, String roomEndTime, String roomState, String flag) {
        this.rid = rid;
        this.sortId = sortId;
        this.roomGameNum = roomGameNum;
        this.roomName = roomName;
        this.roomMember = roomMember;
        this.roomPassword = roomPassword;
        this.roomCreater = roomCreater;
        this.roomCreateTime = roomCreateTime;
        this.roomEndTime = roomEndTime;
        this.roomState = roomState;
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Room{" +
                "rid=" + rid +
                ", sortId=" + sortId +
                ", roomGameNum=" + roomGameNum +
                ", roomName='" + roomName + '\'' +
                ", roomMember=" + roomMember +
                ", roomPassword='" + roomPassword + '\'' +
                ", roomCreater='" + roomCreater + '\'' +
                ", roomCreateTime='" + roomCreateTime + '\'' +
                ", roomEndTime='" + roomEndTime + '\'' +
                ", roomState='" + roomState + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
