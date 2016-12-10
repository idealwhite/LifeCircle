package com.example.lifecircle.UI;

import android.app.Activity;
import android.content.Intent;
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
import com.example.lifecircle.BLL.LifecircleManager;
import com.example.lifecircle.Model.Lifecircle;
import com.example.lifecircle.Model.User;
import com.example.lifecircle.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddLifecircleActivity extends Activity {

    private Integer user_id;
    private EditText findLifecircleEdit;
    private ListView recommendLifecircleListView;
    private LifecircleManager lifecircleManager;
    private List<Lifecircle> recommendList;
    private ArrayList<HashMap<String,Object>> dataSet;
    private List<Integer> lifecircle_ids;

    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 0:
                    showLifecircleList();
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
        setContentView(R.layout.activity_add_lifecircle);
        LocalDataProcess();
        InternetDataProcess();
    }


    private void showLifecircleList(){
        //get taskList
        if(dataSet.size() != 0)
            dataSet.clear();
        for(int i = 0;i < recommendList.size();i++){
            HashMap<String,Object> dataItem = new HashMap<String, Object>();
            dataItem.put("text_lifecircle_name",recommendList.get(i).getName());
            dataItem.put("text_lifecircle_time","");
            dataItem.put("text_lifecircle_content",recommendList.get(i).getInfo());
            dataSet.add(dataItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,dataSet,R.layout.item_lifecircle,
                new String[]{"text_lifecircle_name","text_lifecircle_time","text_lifecircle_content"},
                new int[]{R.id.text_lifecircle_name,R.id.text_lifecircle_time,R.id.text_lifecircle_content});
        recommendLifecircleListView.setAdapter(simpleAdapter);
        recommendLifecircleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int cur = i;
                TextView tv = (TextView)view.findViewById(R.id.text_lifecircle_name);
                if(tv.getText().length() > 3 &&tv.getText().subSequence(tv.getText().length()-3,tv.getText().length()).equals("已加入"))
                    return ;
                tv.setText(tv.getText() + "-已加入");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        //join in the lifecircle:
                        try {
                            lifecircleManager.joinLifecircle(user_id.toString(), recommendList.get(cur).getId().toString());
                            sendMessage(1,"suc");
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
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        this.user_id = getIntent().getExtras().getInt("user_id");
        this.lifecircle_ids = getIntent().getIntegerArrayListExtra("lifecircle_ids");
        this.lifecircleManager = new LifecircleManager();
        this.recommendList = new ArrayList<Lifecircle>();
        this.dataSet = new ArrayList<HashMap<String, Object>>();
        findLifecircleEdit = (EditText)findViewById(R.id.editText_lifecircle_count);
        recommendLifecircleListView = (ListView)findViewById(R.id.listview_lifecircle_recommend);
        findLifecircleEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction() == keyEvent.ACTION_UP &&keyCode == keyEvent.KEYCODE_ENTER){
                    //refresh the ListView and get the item this time.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //get Data from search:
                            if(setDataSetAsSearch(findLifecircleEdit.getText().toString()))
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

    //init data
    private void InternetDataProcess(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if(recommendList.size() != 0)
                        recommendList.clear();
                    JSONArray jsonArray = lifecircleManager.getRecommendList(user_id);
                    for(int i = 0;i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Lifecircle webLifecircle = new Lifecircle();
                        webLifecircle.setId(jsonObject.getInt("id"));
                        webLifecircle.setName(jsonObject.getString("name"));
                        webLifecircle.setInfo(jsonObject.getString("info"));
                        recommendList.add(webLifecircle);
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

    private boolean hasJoin(Integer lifecircle_id){
        boolean hasJoin = false;
        for(int i = 0;i < lifecircle_ids.size();i++){
            if(lifecircle_ids.get(i).equals(lifecircle_id)){
                hasJoin = true;
                break;
            }
        }
        return hasJoin;
    }

    private boolean setDataSetAsSearch(String lifecircle_id){
        boolean suc = false;
        try {
            if(recommendList.size() != 0)
                recommendList.clear();
            JSONObject jsonObject = lifecircleManager.getLifecircleById(lifecircle_id);
            if(!jsonObject.has("id"))
                suc =  false;
            else{
                Lifecircle lifecircle = new Lifecircle();
                lifecircle.setId(jsonObject.getInt("id"));
                lifecircle.setName(jsonObject.getString("name"));
                lifecircle.setInfo(jsonObject.getString("info"));
                if(hasJoin(lifecircle.getId()))
                    lifecircle.setName(lifecircle.getName() + "-已加入");
                recommendList.add(lifecircle);
                suc =  true;
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
            suc = false;
        }
        return suc;
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

    public void onFindLifecircleEditClick(View view){
        this.findLifecircleEdit.setText("");
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
