package com.example.andreea.finalchatandsend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.R.layout.test_list_item;

public class MessageManagerActivity extends AppCompatActivity implements View.OnClickListener, Watcher {


    TextView channel_name;
    ListView chat_history;
    EditText chat_message;
    Button back_button, leave_button, send_message;
    ChatApplication chatapp = null;
    private ArrayAdapter<String> chatmessages;
    Bundle new_bundle;
    String bundlename;

    private void updateHistory()
    {
        chatmessages.clear();
        List<String> msg = chatapp.getHistory();
        for(String m:msg)
        {
            chatmessages.add(m);
        }
        //we need to notify
        chatmessages.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendmessageslayout);

        channel_name = (TextView) findViewById(R.id.selectedchannelname);
        chat_history = (ListView) findViewById(R.id.messageslist);
        chat_message = (EditText) findViewById(R.id.message_writer);

        back_button = (Button) findViewById(R.id.messagebackButton);
        leave_button = (Button) findViewById(R.id.leavechatButton);
        send_message = (Button) findViewById(R.id.sendmessage);

        back_button.setOnClickListener(this);
        leave_button.setOnClickListener(this);
        send_message.setOnClickListener(this);
        chatmessages = new ArrayAdapter<String>(this, test_list_item);
        chat_history.setAdapter(chatmessages);


        chatapp = (ChatApplication)getApplication();
        chatapp.checkin();
        //TODO: move to ChannelListActivity

        updateHistory();
        chatapp.addWatcher(this);
    }
    public void onDestroy()
    {
        chatapp = (ChatApplication)getApplication();
        chatapp.deleteWatcher(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.messagebackButton: {
                Intent new_intent = new Intent(MessageManagerActivity.this, ChannelListActivity.class);
                startActivity(new_intent);
                break;
            }
            case R.id.leavechatButton: {
                chatapp.useLeaveChannel();
                Intent new_intent2 = new Intent(MessageManagerActivity.this, MainActivity.class);
                startActivity(new_intent2);
                chatapp.deleteWatcher(this);
                //update channel state
                break;
            }
            case R.id.sendmessage:
                String message = chat_message.getText().toString();
                chatapp.newLocalUserMessage(message);
                chat_message.setText("");
                //chatapp(message);
                //chatmessages.add(message);
                System.out.println(chatmessages + "Chestii de mers");
                updateHistory();


                break;

        }
    }


    private static final int UPDATE_HISTORY = 1;
    private static final int LEAVE_CHANNEL = 2;
    private static final int OUT_BOUND = 0;
    private android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_HISTORY:
                {
                    updateHistory();
                    break;
                }
                case LEAVE_CHANNEL:
                {
                    //update channel state
                    break;
                }
                case OUT_BOUND:
                    //announce messages are being sent
                    Toast.makeText(getApplication(), "OutBoundSent", Toast.LENGTH_SHORT).show();
                    break;
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

    }
}
