package emp.fri.si.vaja4;

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
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class InputActivity extends Activity implements
        Button.OnClickListener,
        SeekBar.OnSeekBarChangeListener
{
    final private static String filename = "data.txt";

    Button btnPotrdi;
    EditText editIme;
    EditText editDatum;
    Spinner spinSpol;
    SeekBar barVisina;
    TextView textVisina;
    Switch switchStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        editIme = (EditText)findViewById(R.id.editIme);
        editDatum = (EditText)findViewById(R.id.editDatum);
        spinSpol = (Spinner)findViewById(R.id.spinnerSpol);
        barVisina = (SeekBar)findViewById(R.id.seekBarVisina);
        textVisina = (TextView)findViewById(R.id.textVisina);
        switchStudent = (Switch)findViewById(R.id.switchStudent);
        btnPotrdi = (Button)findViewById(R.id.buttonPotrdi);

        loadData();

        barVisina.setOnSeekBarChangeListener(this);
        btnPotrdi.setOnClickListener(this);
    }

    private void saveState(Bundle bundle)
    {
        bundle.putString("si.fri.emp.vaja3.ime", editIme.getText().toString());
        bundle.putString("si.fri.emp.vaja3.datum", editDatum.getText().toString());
        bundle.putString("si.fri.emp.vaja3.spol", spinSpol.getSelectedItem().toString());
        bundle.putInt("si.fri.emp.vaja3.visina", barVisina.getProgress());
        bundle.putBoolean("si.fri.emp.vaja3.student", switchStudent.isChecked());

        Log.d(DisplayActivity.class.getSimpleName(),
                "Saved bundle: " + bundle.toString());
    }

    private void restoreState(Bundle bundle)
    {
        if (bundle != null) {
            Log.d(DisplayActivity.class.getSimpleName(), "Restoring bundle: " + bundle.toString());

            editIme.setText(bundle.getString("si.fri.emp.vaja3.ime"));
            editDatum.setText(bundle.getString("si.fri.emp.vaja3.datum"));
            if (bundle.getString("si.fri.emp.vaja3.spol") == "moški")
                spinSpol.setSelection(0);
            else
                spinSpol.setSelection(1);
            barVisina.setProgress(bundle.getInt("si.fri.emp.vaja3.visina"));
            textVisina.setText(Double.toString((double)bundle.getInt("si.fri.emp.vaja3.visina") / 100.0) + " m");
            switchStudent.setChecked(bundle.getBoolean("si.fri.emp.vaja3.student"));
        }
        else {
            Log.d(DisplayActivity.class.getSimpleName(), "Setting default values.");

            barVisina.setProgress(100);
            textVisina.setText("1.00 m");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        saveState(bundle);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        restoreState(bundle);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        textVisina.setText(Double.toString((double)barVisina.getProgress() / 100.0) + " m");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onClick(View view) {
        saveData();
        Intent intent = new Intent(this, DisplayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("filename", filename);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void saveData() {
        try {
            FileOutputStream stream = openFileOutput(filename, MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(editIme.getText().toString() + System.lineSeparator());
            writer.write(editDatum.getText().toString() + System.lineSeparator());
            writer.write(spinSpol.getSelectedItem().toString() + System.lineSeparator());
            double visina = (double)barVisina.getProgress() / 100.0;
            writer.write(Double.toString(visina) + System.lineSeparator());
            writer.write(switchStudent.isChecked() ? "DA" : "NE");
            writer.close();

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Podatki so bili shranjeni.", Toast.LENGTH_SHORT);
            toast.show();
        }
        catch (IOException e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Napaka pri shranjevanju!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void loadData() {
        try {
            FileInputStream stream = openFileInput(filename);
            Scanner scanner = new Scanner(stream);
            editIme.setText(scanner.nextLine());
            editDatum.setText(scanner.nextLine());
            if (scanner.nextLine().equals("moški"))
                spinSpol.setSelection(0);
            else
                spinSpol.setSelection(1);
            String lineVisina = scanner.nextLine();
            int visina = (int)(Double.parseDouble(lineVisina) * 100);
            barVisina.setProgress(visina);
            textVisina.setText(lineVisina + " m");
            switchStudent.setChecked(scanner.nextLine().equals("DA"));
            scanner.close();
        }
        catch (IOException e) {
            editIme.setText("");
            editDatum.setText("");
            spinSpol.setSelection(0);
            barVisina.setProgress(100);
            textVisina.setText("1.00 m");
            switchStudent.setChecked(false);
        }
    }
}
