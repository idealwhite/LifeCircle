package com.example.lifecircle.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lifecircle.BLL.PersonManager;
import com.example.lifecircle.R;

public class RegisterActivity extends Activity {

    private EditText accountEdit;
    private EditText passwdEdit;
    private EditText nameEdit;
    private EditText phoneEdit;
    private CheckBox agreeCheck;
    private Button submitButton;
    private PersonManager personManager;
    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 0:
                    showMessage("注册成功");
                    Bundle bundle = new Bundle();
                    bundle.putString("account", accountEdit.getText().toString());
                    bundle.putString("passwd", passwdEdit.getText().toString());
                    bundle.putString("name", nameEdit.getText().toString());
                    Intent mIntent = new Intent();
                    mIntent.putExtras(bundle);
                    setResult(RESULT_OK, mIntent);
                    finish();
                    break;
                case 1:
                    showMessage(message.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        personManager = new PersonManager();
        InitView();
        SetEvent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home){
            startActivity(new Intent(this,RegisterActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void InitView(){
        if(getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        accountEdit = (EditText)findViewById(R.id.edit_register_account);
        passwdEdit = (EditText)findViewById(R.id.edit_register_passwd);
        nameEdit = (EditText)findViewById(R.id.edit_register_name);
        phoneEdit = (EditText)findViewById(R.id.edit_register_phone);
        agreeCheck = (CheckBox)findViewById(R.id.check_register);
        submitButton = (Button)findViewById(R.id.button_register);
    }

    private void SetEvent(){
        agreeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean selected) {
                if(selected){
                    submitButton.setClickable(true);
                    submitButton.setBackgroundResource(R.drawable.button_submit);
                }
                else{
                    submitButton.setClickable(false);
                    submitButton.setBackgroundResource(R.drawable.button_submit_pressed);
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton.setBackgroundResource(R.drawable.button_submit_pressed);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean suc = register();
                        if(suc)
                            sendMessage(0,"suc");
                        else
                            sendMessage(1,"注册失败");
                    }
                }).start();
            }
        });
    }

    private boolean register(){
        boolean registSuc = false;
        try{
         registSuc = personManager.register(accountEdit.getText().toString(),nameEdit.getText().toString(),
                passwdEdit.getText().toString(),phoneEdit.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return registSuc;
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

    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
