package com.ndunks.zbarjniexample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        String text = getIntent().getStringExtra("text");
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText( text == null ? "Null Text" : text);
        setTitle("Readed Data");
    }
}
