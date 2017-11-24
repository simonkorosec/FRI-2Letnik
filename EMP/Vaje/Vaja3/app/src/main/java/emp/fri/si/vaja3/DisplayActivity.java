package emp.fri.si.vaja3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DisplayActivity extends Activity implements Button.OnClickListener {
    EditText editIzpis;
    Button btnZapri;
    String returnMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        editIzpis = (EditText)findViewById(R.id.editIzpis);
        btnZapri = (Button)findViewById(R.id.buttonZapri);

        btnZapri.setOnClickListener(this);
        editIzpis.setText("Ni podatkov.");
        returnMsg = "Ni podatkov!";

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (bundle.getString("si.fri.emp.vaja3.ime").length() > 0 &&
                        dateCorrect(bundle.getString("si.fri.emp.vaja3.datum"))) {
                    editIzpis.setText("");
                    editIzpis.append("Ime: " + bundle.getString("si.fri.emp.vaja3.ime") + "\n");
                    editIzpis.append("Datum rojstva: " + bundle.getString("si.fri.emp.vaja3.datum") + "\n");
                    editIzpis.append("Spol: " + bundle.getString("si.fri.emp.vaja3.spol") + "\n");
                    editIzpis.append("Višina: " +
                            Double.toString((double) bundle.getInt("si.fri.emp.vaja3.visina") / 100) + " m\n"
                    );
                    editIzpis.append("Študent: " + (bundle.getBoolean("si.fri.emp.vaja3.student") ? "DA" : "NE"));
                    returnMsg = "Podatki so veljavni.";
                }
                else {
                    returnMsg = "Podatki niso veljavni.";
                    editIzpis.setText(returnMsg);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("si.fri.emp.vaja3.msg", returnMsg);
        returnIntent.putExtras(bundle);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private boolean dateCorrect(String date) {
        try {
            new SimpleDateFormat("dd.mm.yyyy").parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
