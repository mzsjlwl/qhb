package com.handsome.qhb.bean;

import java.util.Arrays;

/**
 * Created by zhang on 2016/3/26.
 */
public class XGMessage {
    private String title;
    private ChatMessage content;
    private String[] accept_time;
    private String[] custom_content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ChatMessage getContent() {
        return content;
    }

    public void setContent(ChatMessage content) {
        this.content = content;
    }

    public String[] getAccept_time() {
        return accept_time;
    }

    public void setAccept_time(String[] accept_time) {
        this.accept_time = accept_time;
    }

    public String[] getCustom_content() {
        return custom_content;
    }

    public void setCustom_content(String[] custom_content) {
        this.custom_content = custom_content;
    }

    public XGMessage(){

    }
    public XGMessage(String title, ChatMessage content, String[] accept_time, String[] custom_content) {
        this.title = title;
        this.content = content;
        this.accept_time = accept_time;
        this.custom_content = custom_content;
    }

    @Override
    public String toString() {
        return "XGMessage{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", accept_time=" + Arrays.toString(accept_time) +
                ", custom_content=" + Arrays.toString(custom_content) +
                '}';
    }
}
