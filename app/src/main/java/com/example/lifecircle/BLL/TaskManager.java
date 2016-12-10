package com.example.lifecircle.BLL;

import com.example.lifecircle.DAL.InternetDataAccessor;
import com.example.lifecircle.Model.Task;
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
public class TaskManager {
    private InternetDataAccessor IDA;

    public TaskManager(){
        IDA = new InternetDataAccessor();
    }

    public JSONArray getTaskList(Integer user_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id.toString()));
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/TaskAction!getTaskList.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray(response);
        return jsonArray;
    }

    public JSONArray getRecommendTaskList(Integer user_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id.toString()));
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/TaskAction!getRecommend.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray(response);
        return jsonArray;
    }

    public JSONArray getLifecircleTaskList(Integer lifecircle_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("lifecircle_id",lifecircle_id.toString()));
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/LifecircleAction!getLifecircleTask.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray(response);
        return jsonArray;
    }

    public JSONObject acceptTask(Integer user_id, Integer task_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id.toString()) );
        params.add(new BasicNameValuePair("task_id",task_id.toString()) );
        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/TaskAction!acceptTask.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject;
    }

    public JSONObject publicTask(Integer user_id, Integer lifecircle_id, Task task) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id",user_id.toString()) );
        params.add(new BasicNameValuePair("lifecircle_id",lifecircle_id.toString()) );
        params.add(new BasicNameValuePair("reward",task.getReward().toString()));
        params.add(new BasicNameValuePair("content",task.getContent()));

        if(task.getAudioPath() != null && !task.getAudioPath().equals(""))
            params.add(new BasicNameValuePair("audioPath",task.getAudioPath()));

        if(task.getPicturePath() != null && !task.getPicturePath().equals(""))
            params.add(new BasicNameValuePair("picturePath",task.getPicturePath()));

        String response = new String();
        try {
            response = IDA.sendMsg(Global.SERVER_URL + "/user/LifecircleAction!publishTask.action", params);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject;
    }


}
