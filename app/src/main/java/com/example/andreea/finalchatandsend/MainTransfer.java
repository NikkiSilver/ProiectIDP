package com.example.andreea.finalchatandsend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Andreea on 4/6/2016.
 */
public class MainTransfer extends Activity implements View.OnClickListener {

    Button music_button, files_button, photos_button, back_button;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_send);

        music_button = (Button) findViewById(R.id.musicbutton);
        files_button = (Button) findViewById(R.id.filebutton);
        photos_button = (Button)findViewById(R.id.photosbutton);
        back_button = (Button) findViewById(R.id.backbutton);

        music_button.setOnClickListener(this);
        files_button.setOnClickListener(this);
        photos_button.setOnClickListener(this);
        back_button.setOnClickListener(this);



    }
    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.musicbutton:
                Toast.makeText(getBaseContext(), "Hello from Music", Toast.LENGTH_SHORT).show();
                Intent documentsIntent = new Intent(Intent.ACTION_PICK);
                documentsIntent.setType("*/*");
                startActivityForResult(documentsIntent, 2);
                break;
            case R.id.filebutton:
                Toast.makeText(getBaseContext(), "Momentan Dead", Toast.LENGTH_SHORT).show();
                break;
            case R.id.photosbutton:

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);

                Toast.makeText(getBaseContext(), "Hello from Photos", Toast.LENGTH_SHORT).show();
                break;
            case R.id.backbutton:
                Intent new_intent = new Intent(MainTransfer.this, MainActivity.class);
                startActivity(new_intent);
                break;

        }

    }
}
