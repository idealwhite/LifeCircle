package com.example.lifecircle.UI;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifecircle.BLL.ContactManager;
import com.example.lifecircle.BLL.PersonManager;
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

public class AddFriendActivity extends Activity {

    private EditText findFriendEdit;
    private ListView recommendFriendListView;
    private ContactManager contactManager;
    private PersonManager personManager;
    private Integer user_id;
    private List<User> recommendList;
    private ArrayList<HashMap<String,Object>> dataSet;

    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 0:
                    showFriendList();
                    break;
                case 1:

                    break;
                default:
                    showMessage(message.obj.toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        LocalDataProcess();
        InternetDataProcess();
    }

    private void showFriendList(){
        //get taskList
        if(dataSet.size() != 0)
            dataSet.clear();
        for(int i = 0;i < recommendList.size();i++){
            HashMap<String,Object> dataItem = new HashMap<String, Object>();
            dataItem.put("text_friend_name",recommendList.get(i).getName());
            dataItem.put("text_friend_account",recommendList.get(i).getAccount());
            dataSet.add(dataItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,dataSet,R.layout.item_friend,
                new String[]{"text_friend_name","text_friend_account"},
                new int[]{R.id.text_friend_name,R.id.text_friend_account});
        recommendFriendListView.setAdapter(simpleAdapter);
        recommendFriendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int cur = i;
                TextView tv = (TextView)view.findViewById(R.id.text_friend_name);
                if(tv.getText().toString().endsWith("已添加"))
                    return ;
                tv.setText(tv.getText() + "-已添加");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        // get friend with him:
                        try{
                            contactManager.addFriend(user_id,recommendList.get(cur).getId());
                            sendMessage(0,"suc");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });
    }

    private void LocalDataProcess(){
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.personManager = new PersonManager();
        this.user_id = getIntent().getExtras().getInt("user_id");
        this.contactManager = new ContactManager();
        this.recommendList = new ArrayList<User>();
        this.dataSet = new ArrayList<HashMap<String, Object>>();
        this.findFriendEdit = (EditText)findViewById(R.id.editText_friend_count);
        this.recommendFriendListView = (ListView)findViewById(R.id.listview_friend_recommend);
        this.findFriendEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction() == keyEvent.ACTION_UP &&keyCode == keyEvent.KEYCODE_ENTER){
                    //refresh the ListView and get the item this time.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //get Data from search:
                            if(setDataSetAsSearch(findFriendEdit.getText().toString()))
                                sendMessage(0, "suc");
                            else
                                sendMessage(-1,"not searched");
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
    }

    private boolean setDataSetAsSearch(String account){
        boolean suc = false;
        try {
            if(recommendList.size() != 0)
                recommendList.clear();
            User user = new User();
            JSONObject jsonObject = personManager.getUserInfoInJson(account,user_id.toString());
            if(user == null)
                suc =  false;
            else{
                //if it is my friend, annotate it.
                if(jsonObject.getBoolean("isFriend") == true)
                    user.setName(jsonObject.getString("name")+"-已添加");
                user.setId(jsonObject.getInt("id"));
                user.setAccount(jsonObject.getString("account"));
                recommendList.add(user);
                suc =  true;
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return suc;
    }

    //init data
    private void InternetDataProcess(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if(recommendList.size() != 0)
                        recommendList.clear();
                    JSONArray jsonArray = contactManager.getRecommendList(user_id);
                    for(int i = 0;i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        User webUser = new User();
                        webUser.setId(jsonObject.getInt("id"));
                        webUser.setName(jsonObject.getString("name"));
                        webUser.setAccount(jsonObject.getString("account"));
                        recommendList.add(webUser);
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

    public void onFindFriendEidtClick(View view){
        this.findFriendEdit.setText("");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
