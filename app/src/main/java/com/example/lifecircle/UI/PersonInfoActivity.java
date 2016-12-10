package com.example.lifecircle.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.lifecircle.R;


public class PersonInfoActivity extends Activity {

    private String creditName;
    private TextView creditText;
    private TextView userNameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        creditName = getIntent().getStringExtra("credit_name");
        creditText = (TextView)findViewById(R.id.text_credit_pa);
        creditText.setText(creditName);
        userNameText = (TextView)findViewById(R.id.text_user_name_pa);
        userNameText.setText(getIntent().getStringExtra("user_name"));

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
