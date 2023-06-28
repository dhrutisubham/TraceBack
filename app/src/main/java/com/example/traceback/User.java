package com.example.traceback;

public class User {
    private String Email, Name, Phone, Pfp;
    public User(String email, String name, String phone, String pfp){
        Email=email;
        Name=name;
        Phone=phone;
        Pfp=pfp;

    }
    public User(){}

    public void setName(String name) {Name=name;}
    public void setPhone(String phone) {Phone=phone;}
    public void setMail(String name) {Email=name;}
    public void setPfp(String name) {Pfp=name;}

    public String getName(){ return Name;}
    public String getMail(){ return Email;}
    public String getPhone(){ return Phone;}
    public String getPfp(){ return Pfp;}



}
