package com.example.lifecircle.BLL;

import com.example.lifecircle.DAL.InternetDataAccessor;
import com.example.lifecircle.Model.Lifecircle;
import com.example.lifecircle.Model.User;
import com.example.lifecircle.Utils.Global;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LisaiZhang on 2016/5/9.
 */
public class LifecircleManager {
    private InternetDataAccessor IDA;

    public LifecircleManager(){
        IDA = new InternetDataAccessor();
    }

    public JSONArray getLifecircleList(Integer user_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id.toString()));
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/LifecircleAction!getLifecircleList.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray(response);
        return jsonArray;
    }

    public JSONArray getLifecircleListWithATask(Integer user_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id.toString()));
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/LifecircleAction!getLifecircleListWithATask.action", params);
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
            response = IDA.sendMsg(Global.SERVER_URL + "/user/LifecircleAction!getRecommendList.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray(response);
        return jsonArray;
    }

    public JSONArray getMemberList(Integer lifecircle_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("lifecircle_id",lifecircle_id.toString()));
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/LifecircleAction!getMemberList.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray(response);
        return jsonArray;
    }

    public JSONObject getLifecircleById(String lifecircle_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("lifecircle_id",lifecircle_id));
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/LifecircleAction!searchLifecircle.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject;
    }

    public JSONObject joinLifecircle(String user_id, String lifecircle_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id));
        params.add(new BasicNameValuePair("lifecircle_id",lifecircle_id));
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/UserAction!joinLifecircle.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject;
    }

    public JSONObject createLifecircle(String user_id,Lifecircle lifecircle) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id));
        params.add(new BasicNameValuePair("name",lifecircle.getName()));
        params.add(new BasicNameValuePair("info",lifecircle.getInfo()));
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/LifecircleAction!addLifecircle.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject;
    }

    public JSONObject getMember(String user_id,String lifecircle_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id));
        params.add(new BasicNameValuePair("lifecircle_id",lifecircle_id));
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/LifecircleAction!getMember.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject;
    }
}
