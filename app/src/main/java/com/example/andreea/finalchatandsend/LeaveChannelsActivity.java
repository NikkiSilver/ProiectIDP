package com.example.andreea.finalchatandsend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreea on 4/28/2016.
 */
public class LeaveChannelsActivity extends Activity implements View.OnClickListener, Watcher {

    ListView list_view;
    TextView my_text;
    Button list_back_button;

    List<String> my_array_list = new ArrayList<String>();
    ChatApplication chatapp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usejoindialog);

        chatapp = (ChatApplication)getApplication();


        list_view = (ListView) findViewById(R.id.useJoinChannelList);
        my_text = (TextView) findViewById(R.id.channelname);
        list_back_button = (Button)findViewById(R.id.listbuttonback);
        list_back_button.setOnClickListener(this);

        List<String>my_channels = chatapp.getFoundChannels();
        for(String channel: my_channels)
        {
            System.out.println(channel+"huehue");
          /*  int last = channel.lastIndexOf('.');
            if (last<0)
            {continue;}*/
            my_array_list.add(channel);
            System.out.println(channel + "huehue");

        }


        //System.out.println(chan_name);

        //System.out.println("This is in my array list" + my_array_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_channel_data,R.id.channelname, my_array_list);
        arrayAdapter.notifyDataSetChanged();

        //nu merge sa populez list view-ul
        list_view.setAdapter(arrayAdapter);

        list_view.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = list_view.getItemAtPosition(position).toString();
                chatapp.hostStopChannel();
                Intent intent = new Intent(LeaveChannelsActivity.this, MainActivity.class);
                startActivity(intent);
                //TODO:update channel state

            }
        });


    }

        @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.listbuttonback:
                Intent new_intent = new Intent(LeaveChannelsActivity.this, MainActivity.class);
                startActivity(new_intent);


        }
    }


    //TODO: change
    private static final int STOP_CHANN = 1;
    private android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOP_CHANN:
                {
                    //TODO:update channel state
                    break;
                }
            }
        }
    };

    @Override
    public synchronized void update(ForWatcher watcher, Object obiect) {
        String text = (String)obiect;

        if(text.equals(ChatApplication.HostUseStates.HOST_STOP_CHANNEL_EVENT.toString()))
        {
            mHandler.sendMessage(mHandler.obtainMessage(STOP_CHANN));
        }

    }
}

