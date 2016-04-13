package com.handsome.qhb.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 2016/3/8.
 */
public class Room implements Serializable {
    private int rid;
    private int sortId;
    private String roomPhoto;
    private int roomGameNum;
    private String roomName;
    private String roomMember;
    private String roomPassword;
    private String roomCreater;
    private String roomCreateTime;
    private String roomEndTime;
    private String roomState;
    private String flag;
    private List<ChatMessage> chatMessageList= new ArrayList<ChatMessage>();
    private ChatMessage lastMessage;



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


    public List<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }

    public void setChatMessageList(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }


    public ChatMessage getLastMessage() {
        return lastMessage;
    }
    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }


    public String getRoomPhoto() {
        return roomPhoto;
    }

    public void setRoomPhoto(String roomPhoto) {
        this.roomPhoto = roomPhoto;
    }

    public Room(){

    }

    public Room(int rid, int sortId, String roomPhoto, int roomGameNum, String roomName, String roomMember, String roomPassword, String roomCreater, String roomCreateTime, String roomEndTime, String roomState, String flag, List<ChatMessage> chatMessageList, ChatMessage lastMessage) {
        this.rid = rid;
        this.sortId = sortId;
        this.roomPhoto = roomPhoto;
        this.roomGameNum = roomGameNum;
        this.roomName = roomName;
        this.roomMember = roomMember;
        this.roomPassword = roomPassword;
        this.roomCreater = roomCreater;
        this.roomCreateTime = roomCreateTime;
        this.roomEndTime = roomEndTime;
        this.roomState = roomState;
        this.flag = flag;
        this.chatMessageList = chatMessageList;
        this.lastMessage = lastMessage;
    }

    @Override
    public String toString() {
        return "Room{" +
                "rid=" + rid +
                ", sortId=" + sortId +
                ", roomPhoto='" + roomPhoto + '\'' +
                ", roomGameNum=" + roomGameNum +
                ", roomName='" + roomName + '\'' +
                ", roomMember='" + roomMember + '\'' +
                ", roomPassword='" + roomPassword + '\'' +
                ", roomCreater='" + roomCreater + '\'' +
                ", roomCreateTime='" + roomCreateTime + '\'' +
                ", roomEndTime='" + roomEndTime + '\'' +
                ", roomState='" + roomState + '\'' +
                ", flag='" + flag + '\'' +
                ", chatMessageList=" + chatMessageList +
                ", lastMessage=" + lastMessage +
                '}';
    }
}
