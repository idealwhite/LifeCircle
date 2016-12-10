package com.example.lifecircle.UI;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lifecircle.BLL.LifecircleManager;
import com.example.lifecircle.BLL.PersonManager;
import com.example.lifecircle.Model.Lifecircle;
import com.example.lifecircle.Model.User;
import com.example.lifecircle.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private User user;
    private LifecircleFragment lifeCircleFragment;
    private TaskFragment taskFragment;
    private ContactFragment contactFragment;
    private PersonFragment personFragment;
    private View lifeCircleLayout;
    private View contactLayout;
    private View taskLayout;
    private View personLayout;
    private ImageView lifeCircleImage;
    private ImageView contactImage;
    private ImageView taskImage;
    private ImageView personImage;
    private TextView lifeCircleText;
    private TextView contactText;
    private TextView taskText;
    private TextView personText;
    private FragmentManager fragmentManager;
    private DisplayMetrics dm;
    private List<Lifecircle> lifecircleList;
    private PersonManager personManager;
    private Bundle bundle;
    private LifecircleManager lifecircleManager;

    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 0:
                    //set bundle info
                    bundle = new Bundle();
                    bundle.putInt("id",user.getId());
                    bundle.putString("name",user.getName());
                    bundle.putString("account", user.getAccount());
                    bundle.putString("passwd", user.getPasswd());
                    bundle.putLong("phone", user.getPhone());
                    // 初始化布局元素
                    initViews();
                    fragmentManager = getSupportFragmentManager();
                    // 第一次启动时选中第0个tab
                    setTabSelection(0);
                    getLifecircleList();
                    break;
                case 1:

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOverflowShowingAlways();
        dm = getResources().getDisplayMetrics();
        Bundle extras = getIntent().getExtras();
        final String account = extras.getString("account");
        lifecircleManager = new LifecircleManager();
        lifecircleList = new ArrayList<Lifecircle>();
        //get user info:
        personManager = new PersonManager();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    user = personManager.getUserInfo(account);
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

    @Override
    protected void onResume(){
        super.onResume();
        setTabSelection(0);
    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {
        //各个tab的layout
        lifeCircleLayout = findViewById(R.id.circle_layout);
        contactLayout = findViewById(R.id.contact_layout);
        taskLayout = findViewById(R.id.task_layout);
        personLayout = findViewById(R.id.person_layout);
        lifeCircleImage = (ImageView) findViewById(R.id.lifecircle_image);
        contactImage = (ImageView) findViewById(R.id.contact_image);
        taskImage = (ImageView) findViewById(R.id.task_image);
        personImage = (ImageView) findViewById(R.id.person_image);
        lifeCircleText = (TextView) findViewById(R.id.lifecircle_text);
        contactText = (TextView) findViewById(R.id.contact_text);
        taskText = (TextView) findViewById(R.id.task_text);
        personText = (TextView) findViewById(R.id.person_text);
        lifeCircleLayout.setOnClickListener(this);
        contactLayout.setOnClickListener(this);
        taskLayout.setOnClickListener(this);
        personLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circle_layout:
                // 当点击了消息tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.contact_layout:
                // 当点击了联系人tab时，选中第2个tab
                setTabSelection(1);
                break;
            case R.id.task_layout:
                // 当点击了动态tab时，选中第3个tab
                setTabSelection(2);
                break;
            case R.id.person_friend:
                setTabSelection(1);
                break;
            case R.id.person_layout:
                // 当点击了设置tab时，选中第4个tab
                setTabSelection(3);
                break;
            default:
                break;
        }
    }

    private void getLifecircleList(){
        if(lifecircleList.size() != 0)
            lifecircleList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONArray jsonArray = lifecircleManager.getLifecircleList(user.getId());
                    for(int i = 0;i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Lifecircle lifecircle = new Lifecircle();
                        lifecircle.setId(jsonObject.getInt("id"));
                        lifecircle.setName(jsonObject.getString("name"));
                        lifecircle.setInfo((jsonObject.getString("info")));
                        lifecircleList.add(lifecircle);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     *            每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        try {
            // 每次选中之前先清楚掉上次的选中状态
            clearSelection();
            // 开启一个Fragment事务
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
            switch (index) {
                case 0:
                    // 当点击了生活圈tab时，改变控件的图片和文字颜色
                    lifeCircleImage.setImageResource(R.drawable.circle);
                    lifeCircleText.setTextColor(Color.GREEN);
                    if (lifeCircleFragment == null) {
                        lifeCircleFragment = new LifecircleFragment();
                        // 如果LifeCircleFragment为空，则创建一个并添加到界面上
                        lifeCircleFragment.setArguments(bundle);
                        transaction.replace(R.id.content, lifeCircleFragment);
                    } else {
                        transaction.replace(R.id.content, lifeCircleFragment);
                    }
                    break;
                case 1:
                    // 当点击了联系人tab时，改变控件的图片和文字颜色
                    contactImage.setImageResource(R.drawable.contact);
                    contactText.setTextColor(Color.GREEN);
                    if (contactFragment == null) {
                        // 如果ContactFragment为空，则创建一个并添加到界面上
                        contactFragment = new ContactFragment();
                        contactFragment.setArguments(bundle);
                        transaction.replace(R.id.content, contactFragment);
                    } else {
                        // 如果ContactFragment不为空，则直接将它显示出来
                        transaction.replace(R.id.content, contactFragment);
                    }
                    break;
                case 2:
                    // 当点击了动态tab时，改变控件的图片和文字颜色
                    taskImage.setImageResource(R.drawable.task);
                    taskText.setTextColor(Color.GREEN);
                    if (taskFragment == null) {
                        // 如果TaskFragment为空，则创建一个并添加到界面上
                        taskFragment = new TaskFragment();
                        taskFragment.setArguments(bundle);
                        transaction.replace(R.id.content, taskFragment);
                    } else {
                        // 如果TaskFragment不为空，则直接将它显示出来
                        transaction.replace(R.id.content, taskFragment);
                    }
                    break;
                case 3:
                    // 当点击了设置tab时，改变控件的图片和文字颜色
                    personImage.setImageResource(R.drawable.person);
                    personText.setTextColor(Color.GREEN);
                    if (personFragment == null) {
                        // 如果PersonFragment为空，则创建一个并添加到界面上
                        personFragment = new PersonFragment();
                        personFragment.setArguments(bundle);
                        transaction.replace(R.id.content, personFragment);
                    } else {
                        // 如果PersonFragment不为空，则直接将它显示出来
                        transaction.replace(R.id.content, personFragment);
                    }
                    break;
                default:
                    break;
            }
            transaction.commit();
        }catch(Exception e){
            e.getCause();
        }
    }


    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        lifeCircleImage.setImageResource(R.drawable.uncircle);
        lifeCircleText.setTextColor(Color.parseColor("#82858b"));
        contactImage.setImageResource(R.drawable.uncontact);
        contactText.setTextColor(Color.parseColor("#82858b"));
        taskImage.setImageResource(R.drawable.untask);
        taskText.setTextColor(Color.parseColor("#82858b"));
        personImage.setImageResource(R.drawable.unperson);
        personText.setTextColor(Color.parseColor("#82858b"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_add_friend){
            Intent intent = new Intent(this,AddFriendActivity.class);
            intent.putExtra("user_id",user.getId());
            startActivity(intent);
        }
        else if(id == R.id.action_add_lifecircle){
            Intent intent = new Intent(this,AddLifecircleActivity.class);
            ArrayList<Integer> lifecircle_ids = new ArrayList<Integer>();
            for(int i = 0;i < lifecircleList.size();i++){
                lifecircle_ids.add(lifecircleList.get(i).getId());
            }
            intent.putIntegerArrayListExtra("lifecircle_ids",lifecircle_ids);
            intent.putExtra("user_id",user.getId());
            startActivity(intent);
        }
        else if(id == R.id.action_create_lifecircle){
            Intent intent = new Intent(this,CreateLifecircleActivity.class);
            intent.putExtra("user_id",user.getId());
            startActivity(intent);
        }
        else if(id == R.id.action_publish){
            setTabSelection(0);
        }
        else if(id == R.id.action_search){

        }

        return super.onOptionsItemSelected(item);
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
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