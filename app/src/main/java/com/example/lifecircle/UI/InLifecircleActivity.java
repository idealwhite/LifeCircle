package com.example.lifecircle.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.lifecircle.BLL.LifecircleManager;
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

public class InLifecircleActivity extends Activity {

    private Member member;
    private Integer user_id;
    private Integer lifecircle_id;
    private String lifecircleName;
    private TaskManager taskManager;
    private LifecircleManager lifecircleManager;
    private ListView taskListView;
    private List<Task> taskList;
    private EditText taskText;
    private List<HashMap<String,Object>> dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_lifecircle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        taskText = (EditText)findViewById(R.id.editText_in_lifecircle);
        member = new Member();
        taskManager = new TaskManager();
        lifecircleManager = new LifecircleManager();
        Bundle bundle = getIntent().getExtras();
        lifecircleName = bundle.getString("lifecircle_name");
        getActionBar().setTitle(lifecircleName);
        user_id = bundle.getInt("user_id");
        lifecircle_id = bundle.getInt("lifecircle_id");
        taskListView = (ListView)findViewById(R.id.in_lifecircle_task_list);
        taskList = new ArrayList<Task>();
        dataSet = new ArrayList<HashMap<String, Object>>();
        InternetDataProcess();
    }


    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 0:
                    showTaskList();
                    break;
                case 1:
                    InternetDataProcess();
                    break;
                default:
                    showMessage(message.obj.toString());
                    break;
            }
        }
    };


    private void showTaskList(){
        //get taskList
        if(dataSet.size() != 0)
            dataSet.clear();
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
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,dataSet,R.layout.item_task,
                new String[]{"text_person","text_task_time","text_task_content","text_task_reward"},
                new int[]{R.id.text_person,R.id.text_task_time,R.id.text_task_content,R.id.text_task_reward});
        taskListView.setAdapter(simpleAdapter);
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int task_cur = i;
                if(taskList.get(i).getMember().getUser().getId() == user_id)
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
                            sendMessage(1,new Integer(task_cur).toString() );
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_in_lifecircle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_lifecircle_info) {
            Intent intent = new Intent(this,LifecircleInfoActivity.class);
            intent.putExtra("lifecircle_id",this.lifecircle_id);
            intent.putExtra("user_id",this.user_id);
            intent.putExtra("lifecircle_name",this.lifecircleName);
            startActivity(intent);
            return true;
        }
        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //init data
    private void InternetDataProcess(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if(taskList.size() != 0)
                        taskList.clear();
                    JSONObject webMember = lifecircleManager.getMember(user_id.toString(), lifecircle_id.toString());
                    if(member == null)
                        member = new Member();
                    member.setId(webMember.getInt("id"));
                    member.setReward(webMember.getInt("reward"));
                    JSONArray jsonArray = taskManager.getLifecircleTaskList(lifecircle_id);
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
                    Collections.sort(taskList,new Comparator<Task>() {
                        @Override
                        public int compare(Task task, Task task2) {
                            if(task.getTime().after(task2.getTime()))
                                return 1;
                            return -1;
                        }
                    });
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

    private boolean acceptTask(Integer i){
        int task_id = taskList.get(i).getId();
        boolean suc = false;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = taskManager.acceptTask(user_id, task_id);
            if(jsonObject.getInt("suc") == 1)
                suc = true;
        } catch (Exception e) {
            e.getCause();
        }
        return suc;
    }

    public void onPublishTaskClick(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View alertLayout = inflater.inflate(R.layout.alert_dialog_reward, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(alertLayout);
        final NumberPicker numberPicker10 = (NumberPicker)alertLayout.findViewById(R.id.numberPicker_10);
        final NumberPicker numberPicker01 = (NumberPicker)alertLayout.findViewById(R.id.numberPicker_01);
        numberPicker01.setMaxValue(9);
        numberPicker10.setMaxValue(9);
        builder.setPositiveButton("发布",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Task task = new Task();
                task.setReward(numberPicker10.getValue()*10+numberPicker01.getValue()+5);
                task.setContent(taskText.getText().toString());
                task.setAudioPath("");
                task.setPicturePath("");
                taskText.setText("");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //if reward is less, return.
                            if(numberPicker10.getValue()*10+numberPicker01.getValue() > member.getReward())
                            {
                                sendMessage(-1,"爱心值不足");
                            }else{

                                if(taskManager.publicTask(user_id, lifecircle_id, task).getInt("suc") == 1)
                                    sendMessage(1,"suc");
                                else
                                    sendMessage(-1,"发布失败");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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

}
