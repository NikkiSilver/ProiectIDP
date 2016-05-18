package com.example.andreea.finalchatandsend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button start, join, leave, send, exit;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button)findViewById(R.id.startchat);

        join = (Button)findViewById (R.id.joinchat);

        leave = (Button)findViewById(R.id.leavechat);

        send = (Button) findViewById(R.id.senddata);

        exit = (Button)findViewById(R.id.closeapp);

        //add click listener to buttons
        start.setOnClickListener(this);
        join.setOnClickListener(this);
        leave.setOnClickListener(this);
        send.setOnClickListener(this);
        exit.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.startchat:
                Toast.makeText(getBaseContext(), "Hello From Start", Toast.LENGTH_SHORT).show();
                Intent new_intent = new Intent(MainActivity.this, CreateChannel.class);
                startActivity(new_intent);
                break;
            case R.id.joinchat:
                    Intent join_intent = new Intent(MainActivity.this, JoinChannelActivity.class);
                    startActivity(join_intent);
                Toast.makeText(getBaseContext(), "Hello from Join", Toast.LENGTH_SHORT).show();
                break;
            case R.id.leavechat: {
                Intent leave_intent = new Intent(MainActivity.this, LeaveChannelsActivity.class);
                startActivity(leave_intent);
                Toast.makeText(getBaseContext(), "Hello from Leave", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.senddata: {
                Intent send_intent = new Intent(MainActivity.this, MainTransfer.class);
                startActivity(send_intent);
                Toast.makeText(getBaseContext(), "Hello from Send", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.closeapp: {
                Toast.makeText(getBaseContext(), "Goodbye", Toast.LENGTH_SHORT).show();
                finish();
                System.exit(0);
                break;
            }

        }

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
