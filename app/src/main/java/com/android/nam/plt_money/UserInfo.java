package com.android.nam.plt_money;

/**
 * Created by Nam on 2/7/2019.
 */

public class UserInfo {
    private int id;
    private String username, email, password, cpassword;

    public UserInfo(int id, String username, String email, String password, String cpassword){
        this.id=id;
        this.username=username;
        this.email=email;
        this.password=password;
        this.cpassword=cpassword;
    }

    public UserInfo() {

    }

    public int getId(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getCpassword(){
        return cpassword;
    }
    public void setId(int id){
        this.id=id;
    }
    public void setUsername(String username){
        this.username=username;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void setCpassword(String cpassword){
        this.cpassword=cpassword;
    }
}
