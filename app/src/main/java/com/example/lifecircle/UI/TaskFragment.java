package com.example.lifecircle.UI;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.lifecircle.BLL.TaskManager;
import com.example.lifecircle.Model.Lifecircle;
import com.example.lifecircle.Model.Member;
import com.example.lifecircle.Model.Task;
import com.example.lifecircle.Model.User;
import com.example.lifecircle.R;
import com.example.lifecircle.Utils.UrlUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {

    private View layout;
    private User user;
    private TaskManager taskManager;
    private Activity parentActivity;
    private ListView taskListView;
    private List<HashMap<String,Object>> dataSet;
    private SimpleAdapter simpleAdapter;
    private List<Task> taskList;
    public TaskFragment() {
    }

    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 0:
                    createListView();
                    break;
                case 1:
                    InternetDataProcess();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(layout == null)
        {
            parentActivity = this.getActivity();
            layout = parentActivity.getLayoutInflater().inflate(R.layout.fragment_task,null);

            taskManager = new TaskManager();
            taskList = new ArrayList<Task>();

            this.taskListView = (ListView)layout.findViewById(R.id.listview_task);
        }
        else
        {
            ViewGroup parent = (ViewGroup)layout.getParent();
            if(parent != null)
                parent.removeView(layout);
        }

        LocalDataProcess();
        InternetDataProcess();
        return layout;
    }

    private boolean acceptTask(Integer i){
        int task_id = taskList.get(i).getId();
        boolean suc = false;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = taskManager.acceptTask(user.getId(), task_id);
            if(jsonObject.getInt("suc") == 1)
                suc = true;
        } catch (Exception e) {
            e.getCause();
        }
        return suc;
    }

    private void LocalDataProcess(){
        Bundle bundle = getArguments();
        this.user = new User();
        this.user.setId(bundle.getInt("id"));
        this.user.setPasswd(bundle.getString("passwd"));
        this.user.setPhone(bundle.getLong("phone"));
        this.user.setName(bundle.getString("name"));
        this.user.setAccount(bundle.getString("account"));
    }
    private void InternetDataProcess(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if(taskList.size() != 0)
                        taskList.clear();
                    JSONArray jsonArray = taskManager.getRecommendTaskList(user.getId());
                    JSONArray taskArray = taskManager.getTaskList(user.getId());
                    for(int i = 0;i < taskArray.length();i++){
                        boolean inRecommend = false;
                        for(int j = 0;j < jsonArray.length();j++){
                            if(jsonArray.getJSONObject(j).getInt("id") == taskArray.getJSONObject(i).getInt("id"))
                                inRecommend = true;
                        }
                        if(!inRecommend)
                            jsonArray.put(taskArray.get(i));
                    }
                    for(int i = 0;i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Task task = new Task();
                        task.setId(jsonObject.getInt("id"));
                        task.setTime(new Timestamp(jsonObject.getJSONObject("time").getLong("time")));
                        task.setContent(jsonObject.getString("content"));
                        task.setReward(jsonObject.getInt("reward"));
                        //task.setMember
                        JSONObject jsonMember = jsonObject.getJSONObject("member");
                        Member member = new Member();
                        member.setId(jsonMember.getInt("id"));
                        member.setName(jsonMember.getString("name"));
                        //task.setMember.setUser
                        JSONObject jsonUser = jsonObject.getJSONObject("member").getJSONObject("user");
                        User webUser = new User();
                        webUser.setId(jsonUser.getInt("id"));
                        webUser.setName(jsonUser.getString("name"));
                        member.setUser(webUser);
                        //task.setMember.setLifecircle
                        JSONObject jsonLifecircle = jsonMember.getJSONObject("lifecircle");
                        Lifecircle lifecircle = new Lifecircle();
                        lifecircle.setId(jsonLifecircle.getInt("id"));
                        lifecircle.setName(jsonLifecircle.getString("name"));
                        lifecircle.setInfo(jsonLifecircle.getString("info"));
                        member.setLifecircle(lifecircle);
                        task.setMember(member);
                        taskList.add(task);
                    }
                    Collections.sort(taskList, new Comparator<Task>() {
                        @Override
                        public int compare(Task task, Task task2) {
                            if (task.getTime().after(task2.getTime()))
                                return 1;
                            return -1;
                        }
                    });
                    sendMessage(0,"suc");
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
     * @param number
     * @param message
     */
    public void sendMessage(int number,String message)
    {
        Message msg = handler.obtainMessage();
        msg.what = number;
        msg.obj = message ;
        this.handler.sendMessage(msg);
    }

    private void createListView(){
        //get taskList
        if(dataSet == null)
            dataSet = new ArrayList<HashMap<String, Object>>();
        else{
            dataSet.clear();
            simpleAdapter.notifyDataSetChanged();
        }

        for(int i = 0;i < taskList.size();i++){
            HashMap<String,Object> dataItem = new HashMap<String, Object>();
            dataItem.put("text_person",taskList.get(i).getMember().getName()+"-"
                    +taskList.get(i).getMember().getLifecircle().getName());
            dataItem.put("text_task_time",taskList.get(i).getTime().getHours()+":"+taskList.get(i).getTime().getMinutes()+","
                    + UrlUtils.NumToDay(taskList.get(i).getTime().getDay()));
            dataItem.put("text_task_content",taskList.get(i).getContent());
            dataItem.put("text_task_reward",taskList.get(i).getReward());
            dataSet.add(dataItem);
        }
        simpleAdapter = new SimpleAdapter(parentActivity,dataSet,R.layout.item_task,
                new String[]{"text_person","text_task_time","text_task_content","text_task_reward"},
                new int[]{R.id.text_person,R.id.text_task_time,R.id.text_task_content,R.id.text_task_reward});
        taskListView.setAdapter(simpleAdapter);
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int task_cur = i;
                if(taskList.get(i).getMember().getUser().getId() == user.getId())
                    return ;
                TextView tv = (TextView)view.findViewById(R.id.text_task_content);
                if(tv.getText().length() > 3&&tv.getText().subSequence(tv.getText().length()-3,tv.getText().length()).equals("已接受"))
                    return ;
                tv.setText(tv.getText() + "-已接受");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(300);
                        }catch (InterruptedException  e){
                            e.printStackTrace();
                        }
                        boolean suc = acceptTask(task_cur);
                        if(suc){
                            sendMessage(1,Integer.toString(task_cur) );
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });
    }
}
