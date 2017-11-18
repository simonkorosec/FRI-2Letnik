package com.example.uporabnik.vaja3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class InputActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    SeekBar drsnik;
    TextView visina;
    Button btn;
    EditText ime;
    EditText datum;
    Spinner spol;
    Switch student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        ime = (EditText)findViewById(R.id.editIme);
        datum = (EditText)findViewById(R.id.editDatum);
        spol = (Spinner)findViewById(R.id.spinnerSpol);
        student = (Switch)findViewById(R.id.switchStudent);

        drsnik = (SeekBar)findViewById(R.id.seekBarVisina);
        drsnik.setOnSeekBarChangeListener(this);

        btn = (Button)findViewById(R.id.buttonPotrdi);
        btn.setOnClickListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        visina = (TextView)findViewById(R.id.textVisina);

        double v = seekBar.getProgress() / 100.0;
        String s = String.format(Locale.ENGLISH, "%.2fm", v);
        visina.setText(s);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        double vis = drsnik.getProgress() / 100.0;

        Intent intent = new Intent(InputActivity.this, DisplayActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("com.example.uporabnik.vaja3.ime", ime.getText().toString());
        bundle.putString("com.example.uporabnik.vaja3.datum", datum.getText().toString());
        bundle.putString("com.example.uporabnik.vaja3.spol", spol.getSelectedItem().toString());
        bundle.putDouble("com.example.uporabnik.vaja3.visina", vis);
        bundle.putString("com.example.uporabnik.vaja3.student", student.isChecked() ? "DA" : "NE");

        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int reqCode, int resCode, Intent data){
        if (reqCode == 1) {
            if (resCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                if (!bundle.getBoolean("com.example.uporabnik.vaja3.pravilnost")){
                    Log.d(DisplayActivity.class.getSimpleName(), "Nepravilen vnos");
                }
            }
        }
    }

}
