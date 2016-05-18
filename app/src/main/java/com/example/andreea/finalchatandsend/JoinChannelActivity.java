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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreea on 5/4/2016.
 */
public class JoinChannelActivity extends Activity implements View.OnClickListener, Watcher {

    ChatApplication chatapp = null;

    ListView list_view;

    //  Bundle my_bundle = new Bundle();
    String chan_name = null;
    TextView my_text;
    List<String> my_array_list = new ArrayList<String>();

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usejoindialog);

        list_view = (ListView)findViewById(R.id.useJoinChannelList);
        my_text = (TextView)findViewById(R.id.channelname);


        //my_bundle = this.getIntent().getExtras();
        // chan_name = my_bundle.getString("nume");
        back = (Button)findViewById(R.id.listbuttonback);
        back.setOnClickListener(this);
        // my_text.append(chan_name);

        chatapp = (ChatApplication)getApplication();
        //chatapp.checkin();
        //TODO: get the list made and publish it. On name clicked join channel
        List<String>my_channels = chatapp.getFoundChannels();
        for(String channel: my_channels)
        {
            my_array_list.add(channel);
        }



        //System.out.println("This is in my array list" + my_array_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_channel_data,R.id.channelname, my_array_list);
        arrayAdapter.notifyDataSetChanged();

        System.out.println("Cacat"+my_array_list);

        //nu merge sa populez list view-ul
        list_view.setAdapter(arrayAdapter);

        list_view.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = list_view.getItemAtPosition(position).toString();
                chatapp.useSetChannelName(name);
                chatapp.useJoinChannel();
                channelStatus();
                System.out.println("Channel"+name);
                Intent intent = new Intent(JoinChannelActivity.this, MessageManagerActivity.class);
                startActivity(intent);

            }
        });

        chatapp.addWatcher(this);
    }

    private void channelStatus()
    {
        AllJoynService.UseChannelState chanState = chatapp.useGetChannelState();


        switch(chanState)
        {
            case IDLE:
                Toast.makeText(JoinChannelActivity.this, "Chat is IDLE", Toast.LENGTH_SHORT).show();
            case JOINED:
                Toast.makeText(JoinChannelActivity.this, "Chat is Joined", Toast.LENGTH_SHORT).show();
        }
    }



    private static final int CHANGE_STATE = 1;
    private static final int JOIN_CHANN = 2;
    private android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE_STATE:
                    channelStatus();
                    break;
                case JOIN_CHANN:
                    //update channel state
                    break;
            }
        }
    };
    @Override
    public void update(ForWatcher watcher, Object obiect) {
            String str = (String) obiect;

        if(str.equals(ChatApplication.HostUseStates.USE_CHANNEL_STATE_CHANGED_EVENT.toString()))
        {
            mHandler.sendMessage((mHandler.obtainMessage(CHANGE_STATE)));
        }
        if(str.equals(ChatApplication.HostUseStates.USE_JOIN_CHANNEL_EVENT.toString())){
            mHandler.sendMessage(mHandler.obtainMessage(JOIN_CHANN));
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.listbuttonback:
                Intent new_intent = new Intent(JoinChannelActivity.this, MainActivity.class);
                startActivity(new_intent);
        }
    }


}
