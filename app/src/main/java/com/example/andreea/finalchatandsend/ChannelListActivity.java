package com.example.andreea.finalchatandsend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
 * Created by Andreea on 4/6/2016.
 */
public class ChannelListActivity extends Activity implements View.OnClickListener, Watcher {


    ChatApplication chatapp = null;

    ListView list_view;

  //  Bundle my_bundle = new Bundle();
    String chan_name = null;
    TextView my_text;
    List<String> my_array_list = new ArrayList<String>();

    Button back;

    Bundle new_bundle;
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
        //TODO: get the list made and publish it. On name clicked start channel
        List<String>my_channels = chatapp.getFoundChannels();
        for(String channel: my_channels)
        {
            System.out.println(channel+"huehue");

           /* int last = channel.lastIndexOf('.');
            if (last<0)
            {continue;}*/
            my_array_list.add(channel);
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
                chatapp.hostSetChannelName(name);
               // chatapp.hostInitChannel();
                chatapp.hostStartChannel();
                //chatapp.useJoinChannel();
                updateMyChannel();

                System.out.println("This is my name "+name);

                Intent intent = new Intent(ChannelListActivity.this, JoinChannelActivity.class);
                startActivity(intent);


            }
        });

    }


    //used to update the state of my channel
    public void updateMyChannel(){

        AllJoynService.HostChannelState mychanstate = chatapp.hostGetChannelState();

        switch(mychanstate)
        {
            case IDLE:
                Toast.makeText(getBaseContext(), "Your room is Idle", Toast.LENGTH_SHORT).show();
                break;
            case NAMED:
                Toast.makeText(getBaseContext(), "Your room has been named", Toast.LENGTH_SHORT).show();
                break;
            case BOUND:
                Toast.makeText(getBaseContext(), "Your room is Bound", Toast.LENGTH_SHORT).show();
                break;
            case ADVERTISED:
                Toast.makeText(getBaseContext(), "Your room is advertised", Toast.LENGTH_SHORT).show();
                break;
            case CONNECTED:
                Toast.makeText(getBaseContext(), "Your room is Connected", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.listbuttonback:
                Intent new_intent = new Intent(ChannelListActivity.this, MainActivity.class);
                startActivity(new_intent);
                break;
        }
    }


    private static final int CHAT_INIT = 0;
    private static final int CHAT_START = 1;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHAT_INIT:
                {

                    // announce chann initialise
                    break;
                }
                case CHAT_START:
                {

                    updateMyChannel();
                    break;
                }

            }
        }
    };
    @Override
    public void update(ForWatcher watcher, Object obiect) {
        String qualifier = (String)obiect;

        if (qualifier.equals(ChatApplication.HostUseStates.HOST_INIT_CHANNEL_EVENT.toString())) {
            Message message = mHandler.obtainMessage(CHAT_INIT);
            mHandler.sendMessage(message);
        }

        if (qualifier.equals(ChatApplication.HostUseStates.HOST_START_CHANNEL_EVENT.toString())) {
            Message message = mHandler.obtainMessage(CHAT_START);
            mHandler.sendMessage(message);
        }

    }
}
