package com.example.andreea.finalchatandsend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Andreea on 4/6/2016.
 */
public class Talk_Now extends Activity implements View.OnClickListener {

    Button ok_button, cancel_button;

    Bundle my_bundle = new Bundle();
    Bundle send_for_list = new Bundle();
    String chan_name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_now);

        ok_button = (Button)findViewById(R.id.hostStartOk);
        cancel_button = (Button)findViewById(R.id.hostStartCancel);

        my_bundle = this.getIntent().getExtras();
        chan_name = my_bundle.getString("nume_camera");
        System.out.println(chan_name+"!!!!!!!!!!!1");

        ok_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);
    }

        @Override
    public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.hostStartOk:
                    send_for_list.putString("nume", chan_name);
                    Intent list_intent = new Intent(Talk_Now.this, ChannelListActivity.class);
                    list_intent.putExtras(send_for_list);
                    startActivity(list_intent);
                    break;
                case R.id.hostStartCancel:
                    Intent back_intent = new Intent(Talk_Now.this, CreateChannel.class);
                    startActivity(back_intent);
                    break;

            }

    }
}
