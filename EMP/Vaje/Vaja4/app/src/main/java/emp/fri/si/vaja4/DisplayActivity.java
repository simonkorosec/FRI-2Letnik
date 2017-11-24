package emp.fri.si.vaja4;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class DisplayActivity extends Activity implements Button.OnClickListener {
    EditText editIzpis;
    Button btnIzvozi;
    Button btnZapri;
    String returnMsg;
    private String filename = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        editIzpis = (EditText)findViewById(R.id.editIzpis);
        btnIzvozi = (Button)findViewById(R.id.buttonIzvozi);
        btnZapri = (Button)findViewById(R.id.buttonZapri);

        btnIzvozi.setOnClickListener(this);
        btnZapri.setOnClickListener(this);
        editIzpis.setText("Ni podatkov.");
        returnMsg = "Ni podatkov!";

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                filename = bundle.getString("filename");
            }
        }

        printData();
    }

    @Override
    public void onClick(View view) {
        if (view == btnIzvozi) {
            int perm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (perm == PackageManager.PERMISSION_GRANTED) {
                exportData();
            }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1234);
            }
        }
        else if (view == btnZapri) {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1234) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    exportData();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Nimam pravic za izvoz!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }

    private void printData() {
        editIzpis.setText("");
        try {
            FileInputStream stream = openFileInput(filename);
            Scanner scanner = new Scanner(stream);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                editIzpis.append(line);
                if (scanner.hasNextLine())
                    editIzpis.append(System.lineSeparator());
            }
            scanner.close();
        }
        catch (IOException e) {
            editIzpis.setText("Napaka pri branju podatkov!");
        }
    }

    private void exportData() {
        String docDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();

        try {
            File dir = new File(docDir);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(docDir, filename);
            if (!file.exists())
                file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(editIzpis.getText().toString().getBytes());
            stream.close();

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Podatki so bili izvoženi.", Toast.LENGTH_SHORT);
            toast.show();
        }
        catch (IOException e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Napaka pri izvozu podatkov!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
