package com.example.lifecircle.UI;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.lifecircle.BLL.LifecircleManager;
import com.example.lifecircle.BLL.TaskManager;
import com.example.lifecircle.Model.Lifecircle;
import com.example.lifecircle.Model.Member;
import com.example.lifecircle.Model.Task;
import com.example.lifecircle.Model.User;
import com.example.lifecircle.R;

import org.json.JSONArray;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LifecircleFragment extends ListFragment {

    private User user;
    private List<Task> taskList;
    private List<Lifecircle> lifecircleList;
    private ListView lifecircleListView;
    private Activity parentActivity;
    private LifecircleManager lifecircleManager;
    private SimpleAdapter simpleAdapter;
    private View layout;
    private List<HashMap<String,Object>> dataSet;

    public LifecircleFragment() {
    }

    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 0:
                    showListView();
                    break;
                case 1:

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Manager
        if(this.lifecircleManager == null)
            this.lifecircleManager = new LifecircleManager();
        //data (user + lifecircleList)
        dataSet = new ArrayList<HashMap<String, Object>>();
        Bundle bundle = getArguments();
        if(this.lifecircleList == null)
            lifecircleList = new ArrayList<Lifecircle>();
        if(this.taskList == null)
            taskList = new ArrayList<Task>();
        this.user = new User();
        this.user.setId(bundle.getInt("id"));
        this.user.setPasswd(bundle.getString("passwd"));
        this.user.setPhone(bundle.getLong("phone"));
        this.user.setName(bundle.getString("name"));
        this.user.setAccount(bundle.getString("account"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(layout == null)
        {
            parentActivity = this.getActivity();
            layout = parentActivity.getLayoutInflater().inflate(R.layout.fragment_lifecircle,null);

            this.lifecircleListView = (ListView)layout.findViewById(android.R.id.list);
        }
        else
        {
            ViewGroup parent = (ViewGroup)layout.getParent();
            if(parent != null)
                parent.removeView(layout);
        }
        InternetDataProcess();
        return layout;
    }

    @Override
    public final void onHiddenChanged(boolean seen){
        if(seen)
            InternetDataProcess();
    }

    private void showListView(){

        if(dataSet.size() != 0) {
            dataSet.clear();
            simpleAdapter.notifyDataSetChanged();
        }
        for(int i = 0;i < lifecircleList.size();i++){
            HashMap<String,Object> dataItem = new HashMap<String, Object>();
            dataItem.put("text_lifecircle_name", lifecircleList.get(i).getName());
            dataItem.put("text_lifecircle_content", taskList.get(i).getContent());
            if(taskList.get(i).getTime() != null) {
                SimpleDateFormat format = new SimpleDateFormat ("yyyy/MM/dd KK:mm:ss a");//定义日期类型格式
                String dateStr = format.format(taskList.get(i).getTime()); //转换为字符串
                dataItem.put("text_lifecircle_time", dateStr.subSequence(5,16));
            }
            dataSet.add(dataItem);
        }
        simpleAdapter = new SimpleAdapter(parentActivity,dataSet,R.layout.item_lifecircle,
                new String[]{"text_lifecircle_name","text_lifecircle_content","text_lifecircle_time"},
                new int[]{R.id.text_lifecircle_name,R.id.text_lifecircle_content,R.id.text_lifecircle_time});
        lifecircleListView.setAdapter(simpleAdapter);
        lifecircleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(parentActivity, InLifecircleActivity.class);
                intent.putExtra("user_id", user.getId());
                intent.putExtra("lifecircle_id",lifecircleList.get(i).getId());
                intent.putExtra("lifecircle_name",lifecircleList.get(i).getName());
                startActivity(intent);
            }
        });
    }

    private void InternetDataProcess(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray = lifecircleManager.getLifecircleListWithATask(user.getId());
                    if(lifecircleList.size() != 0)
                        lifecircleList.clear();
                    if(taskList.size() != 0)
                        taskList.clear();
                    for(int i = 0;i < jsonArray.length();i++){
                        Lifecircle lifecircle = new Lifecircle();
                        lifecircle.setId(jsonArray.getJSONObject(i).getInt("id"));
                        lifecircle.setName(jsonArray.getJSONObject(i).getString("name"));
                        lifecircle.setInfo(jsonArray.getJSONObject(i).getString("info"));
                        lifecircleList.add(lifecircle);
                        Task task = new Task();
                        if(jsonArray.getJSONObject(i).has("content")){
                            task.setContent(jsonArray.getJSONObject(i).getString("content"));
                            task.setTime(new Timestamp(jsonArray.getJSONObject(i).getJSONObject("time").getLong("time")));
                        }
                        else{
                            task.setContent("");
                            task.setTime(null);
                        }
                        taskList.add(task);
                    }
                    sendMessage(0, "suc");
                }catch (Exception e){
                    e.printStackTrace();
                    e.getCause();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * send message to handler.
     * @param number handler inform
     * @param message string message
     */
    public void sendMessage(int number,String message)
    {
        Message msg = handler.obtainMessage();
        msg.what = number;
        msg.obj = message ;
        this.handler.sendMessage(msg);
    }

}
