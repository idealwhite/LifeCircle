package com.example.lifecircle.UI;


import android.app.Activity;
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

import com.example.lifecircle.BLL.ContactManager;
import com.example.lifecircle.BLL.LifecircleManager;
import com.example.lifecircle.BLL.TaskManager;
import com.example.lifecircle.Model.Friend;
import com.example.lifecircle.Model.Lifecircle;
import com.example.lifecircle.Model.Task;
import com.example.lifecircle.Model.User;
import com.example.lifecircle.R;
import com.example.lifecircle.Utils.UrlUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private User user;
    private ContactManager contactManager;
    private Activity parentActivity;
    private ListView contactListView;
    private List<Friend> contactList;
    private View layout;

    public ContactFragment() {

    }

    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 0:
                    //get taskList
                    List<HashMap<String,Object>> dataSet = new ArrayList<HashMap<String, Object>>();
                    for(int i = 0;i < contactList.size();i++){
                        HashMap<String,Object> dataItem = new HashMap<String, Object>();
                        dataItem.put("text_contact_name",contactList.get(i).getUserByFriendId().getName());
                        dataItem.put("text_friend_reward",contactList.get(i).getRelationReward());
                        dataSet.add(dataItem);
                    }
                    SimpleAdapter simpleAdapter = new SimpleAdapter(parentActivity,dataSet,R.layout.item_contact,
                            new String[]{"text_contact_name","text_friend_reward"},
                            new int[]{R.id.text_contact_name,R.id.text_friend_reward});
                    contactListView.setAdapter(simpleAdapter);
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
        contactManager = new ContactManager();
        contactList = new ArrayList<Friend>();
        Bundle bundle = getArguments();
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
            layout = parentActivity.getLayoutInflater().inflate(R.layout.fragment_contact,null);

            this.contactListView = (ListView)layout.findViewById(android.R.id.list);
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

    private void InternetDataProcess(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray = contactManager.getContactList(user.getId());
                    if(contactList.size() != 0)
                        contactList.clear();
                    for(int i = 0;i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Friend friend = new Friend();
                        friend.setId(jsonObject.getInt("id"));
                        friend.setRelationReward(jsonObject.getInt("relation_reward"));
                        User friendUser = new User();
                        friendUser.setId(jsonObject.getJSONObject("friend").getInt("id"));
                        friendUser.setName(jsonObject.getJSONObject("friend").getString("name"));
                        friend.setUserByFriendId(friendUser);
                        contactList.add(friend);
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
