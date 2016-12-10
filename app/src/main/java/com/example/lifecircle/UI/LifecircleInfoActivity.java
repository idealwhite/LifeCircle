package com.example.lifecircle.UI;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifecircle.BLL.LifecircleManager;
import com.example.lifecircle.Model.Credit;
import com.example.lifecircle.Model.Member;
import com.example.lifecircle.Model.User;
import com.example.lifecircle.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LifecircleInfoActivity extends Activity {
    private TextView nameText;
    private TextView idText;
    private ListView memberListView;
    private List<Member> memberList;
    private LifecircleManager lifecircleManager;
    private ArrayList<HashMap<String,Object>> dataSet;
    private Integer user_id;
    private Integer lifecircle_id;

    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 0:
                    showMemberList();
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
        setContentView(R.layout.activity_lifecircle_info);
        InitViewAndData();
        InternetDataProcess();
    }

    private void InitViewAndData(){
        if(getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        nameText = (TextView)findViewById(R.id.text_info_name);
        idText = (TextView)findViewById(R.id.text_info_id);
        memberListView = (ListView)findViewById(R.id.listview_info_member);
        //Data
        memberList = new ArrayList<Member>();
        user_id = getIntent().getIntExtra("user_id",0);
        lifecircle_id = getIntent().getIntExtra("lifecircle_id",0);

        lifecircleManager = new LifecircleManager();
        dataSet = new ArrayList<HashMap<String, Object>>();
        nameText.setText(getIntent().getStringExtra("lifecircle_name"));
        idText.setText(String.valueOf(getIntent().getIntExtra("lifecircle_id",0)));

    }

    //init data
    private void InternetDataProcess(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if(memberList.size() != 0)
                        memberList.clear();
                    JSONArray jsonArray = lifecircleManager.getMemberList(lifecircle_id);
                    for(int i = 0;i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Member member = new Member();
                        member.setId(jsonObject.getInt("id"));
                        member.setName(jsonObject.getString("name"));
                        member.setReward(jsonObject.getInt("reward"));
                        Credit credit = new Credit();
                        credit.setCreditName(jsonObject.getJSONObject("credit").getString("name"));
                        member.setCredit(credit);
                        User user = new User();
                        user.setId(jsonObject.getJSONObject("user").getInt("id"));
                        user.setAccount(jsonObject.getJSONObject("user").getString("account"));
                        member.setUser(user);
                        memberList.add(member);
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

    private void showMemberList(){
        //get taskList
        if(dataSet.size() != 0)
            dataSet.clear();
        for(int i = 0;i < memberList.size();i++){
            HashMap<String,Object> dataItem = new HashMap<String, Object>();
            dataItem.put("text_member_name",memberList.get(i).getName());
            dataItem.put("text_member_credit",memberList.get(i).getCredit().getCreditName());
            dataItem.put("text_member_reward",memberList.get(i).getReward());
            dataItem.put("text_member_account",memberList.get(i).getUser().getAccount());
            dataSet.add(dataItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,dataSet,R.layout.item_member,
                new String[]{"text_member_name","text_member_credit","text_member_reward","text_member_account"},
                new int[]{R.id.text_member_name,R.id.text_member_credit,R.id.text_member_reward,R.id.text_member_account});
        memberListView.setAdapter(simpleAdapter);
        memberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO:

            }
        });
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
