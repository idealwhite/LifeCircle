package com.example.lifecircle.BLL;

import com.example.lifecircle.DAL.InternetDataAccessor;
import com.example.lifecircle.Utils.Global;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LisaiZhang on 2016/5/9.
 */
public class ContactManager {
    private InternetDataAccessor IDA;

    public ContactManager(){
        IDA = new InternetDataAccessor();
    }

    public JSONArray getContactList(Integer user_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id.toString()));
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/ContactAction!getContactList.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray(response);
        return jsonArray;
    }

    public JSONArray getRecommendList(Integer user_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id.toString()));
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/ContactAction!getRecommendFriend.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray(response);
        return jsonArray;
    }

    public JSONArray addFriend(Integer user_id, Integer target_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("host_id",user_id.toString()));
        params.add(new BasicNameValuePair("target_id",target_id.toString()));
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/ContactAction!addFriend.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray(response);
        return jsonArray;
    }
}
