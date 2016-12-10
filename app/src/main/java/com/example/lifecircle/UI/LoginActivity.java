package com.example.lifecircle.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lifecircle.BLL.PersonManager;
import com.example.lifecircle.Model.User;
import com.example.lifecircle.R;


public class LoginActivity extends Activity {

    private User user;
    private PersonManager personManager;
    private EditText passwdEditText;
    private EditText accountEditText;
    private boolean mAccountEditClicked;
    private boolean mPasswordEditClicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View decorView = getWindow().getDecorView();
        personManager = new PersonManager();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        accountEditText = (EditText)findViewById(R.id.account);
        passwdEditText = (EditText)findViewById(R.id.password);
        passwdEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                    try{
                    onLoginButtonClick(findViewById(R.id.login_button));
                    }catch (Throwable throwable){
                        throwable.getCause();
                    }
                    return true;
                }
                return false;
            }
        });

        ActionBar actionBar = getActionBar();
        if(actionBar != null)
            actionBar.hide();
        this.mAccountEditClicked = false;
        this.mAccountEditClicked = false;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(getIntent().hasExtra("account")){
            accountEditText.setText(getIntent().getStringExtra("account"));
            passwdEditText.setText(getIntent().getStringExtra("passwd"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null && extras.containsKey("account")) {
                accountEditText.setText(getIntent().getStringExtra("account"));
                passwdEditText.setText(getIntent().getStringExtra("passwd"));
            }
        }
    }
    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 0:
                    (findViewById(R.id.login_tip)).setVisibility(View.VISIBLE);
                    (findViewById(R.id.login_button)).setBackgroundResource(R.drawable.button_login);
                    break;
                case 1:
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("account", user.getAccount());
                    intent.putExtra("password", user.getPasswd());
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    public void onLoginButtonClick(View view) throws Throwable{
        (findViewById(R.id.login_button)).setBackgroundResource(R.drawable.button_login_clicked);
        String account = ((EditText)findViewById(R.id.account)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();
        user = new User();
        user.setAccount(account);
        user.setPasswd(password);
        /**internet thread**/
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                login();
            }
        };
        Thread loginThread = new Thread(runnable);
        loginThread.start();
    }

    public void onAccountEditClick(View view){
        if(this.mAccountEditClicked == false){
            ((EditText)findViewById(R.id.account)).setTextColor(Color.BLACK);
            ((EditText)findViewById(R.id.account)).setText("");
            this.mAccountEditClicked = true;
        }
    }

    public void onPasswordEditClick(View view){
        if(this.mPasswordEditClicked == false){
            ((EditText)findViewById(R.id.password)).setTextColor(Color.BLACK);
            ((EditText)findViewById(R.id.password)).setText("");
            this.mPasswordEditClicked = true;
        }
    }

    public void onRegisterTextClicked(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
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

    private void login(){
        try {

            if (personManager.login(user)) {
                sendMessage(1,"suc");
                this.finish();
            }
            else {
                sendMessage(0,"账号密码错误");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
