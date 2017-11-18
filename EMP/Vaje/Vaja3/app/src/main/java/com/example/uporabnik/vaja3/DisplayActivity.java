package com.example.uporabnik.vaja3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;
import java.util.Objects;

public class DisplayActivity extends Activity implements View.OnClickListener {
    EditText izpis;
    Button zapri;
    boolean pravilnostVnosa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        izpis = (EditText)findViewById(R.id.editIzpis);
        zapri = (Button)findViewById(R.id.buttonZapri);

        String ime = "", datum = "", spol = "", student = "";
        double visina = 0.0;
        pravilnostVnosa = true;

        final Intent intent = getIntent();

        if (intent != null){
            final Bundle bundle = intent.getExtras();
            if (bundle != null) {
                ime = bundle.getString("com.example.uporabnik.vaja3.ime");
                datum = bundle.getString("com.example.uporabnik.vaja3.datum");
                spol = bundle.getString("com.example.uporabnik.vaja3.spol");
                visina = bundle.getDouble("com.example.uporabnik.vaja3.visina");
                student = bundle.getString("com.example.uporabnik.vaja3.student");

                if (ime != null && ime.equals(""))
                    pravilnostVnosa = false;
            }
        }

        String s =  String.format(Locale.ENGLISH, "Ime: %s\nDatum rojstva: %s\nSpol: %s\nVišina: %.2fm\nŠtudent: %s",
                ime, datum, spol, visina, student);
        izpis.setText(s);

        zapri.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent retIntent = new Intent();
        Bundle bnd = new Bundle();
        bnd.putBoolean("com.example.uporabnik.vaja3.pravilnost", pravilnostVnosa);
        retIntent.putExtras(bnd);
        setResult(RESULT_OK, retIntent);
        finish();
    }
}
