package com.example.Chat_App;

public class Message {

    String Type;
    String Msg;
    String Status;
    
    public Message() {
    }

    public Message(String type, String msg, String status) {
        Type = type;
        Msg = msg;
        Status = status;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getType() {
        return Type;
    }


    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getMsg() {
        return Msg;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatus() {
        return Status;
    }

}
