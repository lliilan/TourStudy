package com.kl.tourstudy.gsonbean;

import java.util.Date;

/**
 * Gson 聊天信息
 * Created by KL on 2017/5/27 0027.
 */

public class ChatInfo {
    private int chatId;
    private int chatA;
    private int chatB;
    private String nameA;
    private String nameB;
    private String content;
    private Date chatDate;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getChatA() {
        return chatA;
    }

    public void setChatA(int chatA) {
        this.chatA = chatA;
    }

    public int getChatB() {
        return chatB;
    }

    public void setChatB(int chatB) {
        this.chatB = chatB;
    }

    public String getNameA() {
        return nameA;
    }

    public void setNameA(String nameA) {
        this.nameA = nameA;
    }

    public String getNameB() {
        return nameB;
    }

    public void setNameB(String nameB) {
        this.nameB = nameB;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getChatDate() {
        return chatDate;
    }

    public void setChatDate(Date chatDate) {
        this.chatDate = chatDate;
    }
}
