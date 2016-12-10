package com.example.lifecircle.UI;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lifecircle.BLL.PersonManager;
import com.example.lifecircle.Model.Credit;
import com.example.lifecircle.Model.User;
import com.example.lifecircle.R;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {

    private User user;
    private PersonManager personManager;
    private Credit credit;
    private Activity parentActivity;
    private TextView publishTaskText;
    private TextView doneTaskText;
    private TextView creditText;
    private Integer publish_num;
    private Integer accept_num;
    private View layout;
    private View personCard;

    public PersonFragment() {
    }

    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 0:
                    layout = parentActivity.getLayoutInflater().inflate(R.layout.fragment_person,null);
                    ((TextView)layout.findViewById(R.id.text_user_credit)).
                            setText(credit.getReward() + ": " + credit.getCreditName());
                    publishTaskText.setText(publish_num.toString());
                    doneTaskText.setText(accept_num.toString());
                    creditText.setText(credit.getCreditName());
                    break;
                case 1:

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentActivity = getActivity();
        personManager = new PersonManager();
        Bundle bundle = getArguments();
        this.user = new User();
        this.credit = new Credit();
        this.user.setId(bundle.getInt("id"));
        this.user.setPasswd(bundle.getString("passwd"));
        this.user.setPhone(bundle.getLong("phone"));
        this.user.setName(bundle.getString("name"));
        this.user.setAccount(bundle.getString("account"));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.InternetDataProcess();
        ((TextView)getActivity().findViewById(R.id.text_user_name)).setText(this.user.getName());
        this.publishTaskText = (TextView)parentActivity.findViewById(R.id.task_published);
        this.doneTaskText = (TextView)parentActivity.findViewById(R.id.text_task_done);
        this.creditText = (TextView)parentActivity.findViewById(R.id.text_user_credit);
        this.personCard = parentActivity.findViewById(R.id.person_card);
        personCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parentActivity,PersonInfoActivity.class);
                intent.putExtra("credit_name",credit.getCreditName());
                intent.putExtra("user_name",user.getName());
                startActivity(intent);
            }
        });
    }

    private void InternetDataProcess(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    credit = personManager.getUserCredit(user.getId());
                    publish_num = personManager.getPublishNum(user.getId());
                    accept_num = personManager.getAcceptNum(user.getId());
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
