package com.example.andreea.finalchatandsend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Andreea on 4/6/2016.
 */
public class CreateChannel extends Activity implements View.OnClickListener, Watcher {

    Button ok_button, cancel_button;
    EditText chan_name;

   private ChatApplication chatapp = null;
    private String my_channel_name; // to check if EditText is empty or not


    Bundle new_bundle = new Bundle();  //to send the channel name to be added to the list

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        ok_button = (Button) findViewById(R.id.hostNameOk);
        cancel_button = (Button) findViewById(R.id.hostNameCancel);
        chan_name = (EditText) findViewById(R.id.hostNameChannel);

        chatapp = (ChatApplication)getApplication();
        chatapp.checkin();
        chatapp.addWatcher(this);

        ok_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);

        chatapp = (ChatApplication)getApplication();
        chatapp.checkin();
        chatapp.addWatcher(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.hostNameOk:

                my_channel_name = chan_name.getText().toString();
                System.out.println(my_channel_name+"This is my name");
                //addfoundchannel to the list
                    if(my_channel_name.matches(""))
                    Toast.makeText(getBaseContext(), "No channel name found", Toast.LENGTH_SHORT).show();
                else
                {
                    chatapp.addFoundChannel(my_channel_name);
                    chatapp.hostInitChannel();
                    System.out.println(my_channel_name + "mehmeh");
                    Intent create_chan = new Intent(CreateChannel.this, ChannelListActivity.class);

                    startActivity(create_chan);

                }
                break;
            case R.id.hostNameCancel:
                my_channel_name = chan_name.getText().toString();

                if(my_channel_name.matches("")) {
                    Intent cancel_back_intent = new Intent(CreateChannel.this, MainActivity.class);
                    startActivity(cancel_back_intent);
                }
                else
                {
                    chan_name.getText().clear();
                    Toast.makeText(getBaseContext(), "Name cleared", Toast.LENGTH_SHORT).show();

                }

                break;


        }

    }

    public void onDestroy() {
        chatapp = (ChatApplication)getApplication();
        chatapp.deleteWatcher(this);
        super.onDestroy();
    }

    private static final int UPDATE_HISTORY = 1;
    private static final int LEAVE_CHANNEL = 2;
    private static final int INIT_CHANN = 3;
    private android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_HISTORY:
                {
                    //updateHistory();
                    break;
                }
                case LEAVE_CHANNEL:
                {
                    //update channel state
                    finish();
                    break;

                }
                case INIT_CHANN:
                {
                    break;
                }

            }
        }
    };

    @Override
    public synchronized void update(ForWatcher watcher, Object obiect) {
        String text = (String)obiect;

        if(text.equals(ChatApplication.HostUseStates.HISTORY_CHANGED_EVENT.toString()))
        {
            mHandler.sendMessage(mHandler.obtainMessage(UPDATE_HISTORY));
        }
        if(text.equals(ChatApplication.HostUseStates.USE_LEAVE_CHANNEL_EVENT.toString()))
        {
            mHandler.sendMessage(mHandler.obtainMessage(LEAVE_CHANNEL));
        }
        if(text.equals(ChatApplication.HostUseStates.HOST_INIT_CHANNEL_EVENT.toString())){
            mHandler.sendMessage(mHandler.obtainMessage(INIT_CHANN));
        }


    }
}
