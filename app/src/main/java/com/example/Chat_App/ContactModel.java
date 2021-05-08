package com.example.Chat_App;

class ContactModel {
    String Name;
    String Phone_Number;
    String Password;
    String Picture_Url;


    public void setName(String name) { Name = name; }

    public void setPhone_Number(String phone_Number) { Phone_Number = phone_Number; }

    public void setPassword(String password) { Password = password; }

    public void setPicture_Url(String picture_Url) { Picture_Url = picture_Url; }

    public ContactModel() { }

    public ContactModel(String name, String Phone_Number, String Password, String urls) {
        this.Picture_Url = urls;
        this.Name = name;
        this.Phone_Number = Phone_Number;
        this.Password = Password;
    }

    public String getName() {
        return Name;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public String getPassword() {
        return Password;
    }

    public String getPicture_Url() { return Picture_Url; }

}
