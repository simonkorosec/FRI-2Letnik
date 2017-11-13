package com.example.uporabnik.sestevalnik;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements Button.OnClickListener {

    Button btn;
    TextView vsota;
    //EditText prvaSt;
    //EditText drufaS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(this);

        vsota = (TextView) findViewById(R.id.vsota);
    }


    @Override
    public void onClick(View v) {
        EditText prva =  (EditText) findViewById(R.id.prvaSt);
        EditText druga =  (EditText) findViewById(R.id.drugaSt);

        int st1 = Integer.parseInt(prva.getText().toString());
        int st2 = Integer.parseInt(druga.getText().toString());

        st1 += st2;

        String str = Integer.toString(st1);
        vsota.setText(str);

    }
}
