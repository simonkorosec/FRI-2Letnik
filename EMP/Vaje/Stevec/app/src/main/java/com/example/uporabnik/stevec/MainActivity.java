package com.example.uporabnik.stevec;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements Button.OnClickListener{

    TextView text;
    Button btn;
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView)findViewById(R.id.text);
        btn  = (Button)findViewById(R.id.button);

        btn.setOnClickListener(this);
        text.setText("0");

        counter = 0;
    }

    @Override
    public void onClick(View view) {
        counter = inc(counter);
        text.setText(Integer.toString(counter));
    }

    public static int inc(int value) {
        if (value < 20)
            return ++value;
        else
            return value;
    }
}
