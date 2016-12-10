package com.example.lifecircle.BLL;

import com.example.lifecircle.DAL.InternetDataAccessor;
import com.example.lifecircle.Model.Credit;
import com.example.lifecircle.Model.User;
import com.example.lifecircle.Utils.Global;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LisaiZhang on 2016/4/29.
 */
public class PersonManager {
    private InternetDataAccessor IDA;

    public PersonManager(){
        IDA = new InternetDataAccessor();
    }

    public boolean login(User user) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account",user.getAccount()));
        params.add(new BasicNameValuePair("passwd",user.getPasswd()));
        JSONObject jsonObjResponse =
                new JSONObject(IDA.sendMsg(Global.SERVER_URL+"/user/UserAction!login.action",params));

        if(jsonObjResponse.getInt("suc") == 1)
            return true;
        return false;

    }

    public User getUserInfo(String account) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account",account));
        User user = new User();
        try {
            JSONObject jsonObject = new JSONObject(IDA.sendMsg(Global.SERVER_URL + "/user/UserAction!getUserInfo.action", params));
            user.setId(jsonObject.getInt("id"));
            user.setPasswd(jsonObject.getString("passwd"));
            user.setAccount(jsonObject.getString("account"));
            user.setName(jsonObject.getString("name"));
            user.setPhone(jsonObject.getInt("phone"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public Credit getUserCredit(Integer user_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id.toString()));
        Credit credit = new Credit();
        try {
            JSONObject jsonObject = new JSONObject(IDA.sendMsg(Global.SERVER_URL + "/user/UserAction!getUserCredit.action", params));
            credit.setCreditName(jsonObject.getString("credit"));
            credit.setReward(jsonObject.getInt("reward"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return credit;
    }


    public JSONObject getUserInfoInJson(String account,String user_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account",account));
        params.add(new BasicNameValuePair("user_id",user_id));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(IDA.sendMsg(Global.SERVER_URL + "/user/UserAction!getNewUserInfo.action", params));
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    public Integer getPublishNum(Integer user_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id.toString()));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(IDA.sendMsg(Global.SERVER_URL + "/user/UserAction!getPublishNum.action", params));
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.getInt("task_num");
    }

    public Integer getAcceptNum(Integer user_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id.toString()));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(IDA.sendMsg(Global.SERVER_URL + "/user/UserAction!getAcceptNum.action", params));
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.getInt("task_num");
    }

    public boolean register(String account,String name, String passwd,String phone) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account",account));
        params.add(new BasicNameValuePair("name",name));
        if(phone != null && !phone.equals(""))
            params.add(new BasicNameValuePair("passwd",passwd));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(IDA.sendMsg(Global.SERVER_URL + "/user/UserAction!register.action", params));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(jsonObject.getInt("suc") == 1)
            return true;
        return false;
    }
}
