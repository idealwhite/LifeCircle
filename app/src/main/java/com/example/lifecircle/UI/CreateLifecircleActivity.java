package com.example.lifecircle.UI;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lifecircle.BLL.LifecircleManager;
import com.example.lifecircle.Model.Lifecircle;
import com.example.lifecircle.R;

public class CreateLifecircleActivity extends Activity {

    private EditText nameEditText;
    private EditText infoEditText;
    private Button submitButton;
    private Integer user_id;
    private LifecircleManager lifecircleManager;

    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 0:
                    //finish
                    finish();
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
        setContentView(R.layout.activity_create_lifecircle);
        //data
        user_id = getIntent().getIntExtra("user_id",0);
        lifecircleManager = new LifecircleManager();
        //view
        getActionBar().setDisplayHomeAsUpEnabled(true);
        nameEditText = (EditText)findViewById(R.id.edit_lifecircle_name);
        infoEditText = (EditText)findViewById(R.id.edit_lifecircle_info);
        submitButton = (Button)findViewById(R.id.button_create_lifecircle);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton.setBackgroundResource(R.drawable.button_submit_pressed);
                //create the lifecircle and jump to lifecircleInfo activity.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Lifecircle newLifecircle = new Lifecircle();
                        newLifecircle.setInfo(infoEditText.getText().toString());
                        newLifecircle.setName(nameEditText.getText().toString());
                        try {
                            lifecircleManager.createLifecircle(user_id.toString(), newLifecircle);
                            sendMessage(0,"suc");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
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
