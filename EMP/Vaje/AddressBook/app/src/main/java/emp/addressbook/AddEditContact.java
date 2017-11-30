package emp.addressbook;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddEditContact extends AppCompatActivity {
    private long rowID;

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText streetEditText;
    private EditText cityEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addedit_contact);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        streetEditText = findViewById(R.id.streetEditText);
        cityEditText = findViewById(R.id.cityEditText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rowID = extras.getLong("row_id");
            nameEditText.setText(extras.getString("name"));
            emailEditText.setText(extras.getString("email"));
            phoneEditText.setText(extras.getString("phone"));
            streetEditText.setText(extras.getString("street"));
            cityEditText.setText(extras.getString("city"));
        }

        Button saveContactButton = findViewById(R.id.saveContactButton);
        saveContactButton.setOnClickListener(saveContactButtonClicked);
    }

    View.OnClickListener saveContactButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (nameEditText.getText().length() != 0) {
                saveContact();
                finish();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle(R.string.errorTitle);
                builder.setMessage(R.string.errorMessage);
                builder.setPositiveButton(R.string.errorButton, null);
                builder.show();
            }
        }
    };

    private void saveContact() {
        DatabaseConnector databaseConnector = new DatabaseConnector(getApplicationContext());

        if (getIntent().getExtras() == null) {
            databaseConnector.insertContact(nameEditText.getText().toString(), emailEditText
                    .getText().toString(), phoneEditText.getText().toString(), streetEditText
                    .getText().toString(), cityEditText.getText().toString());
        }
        else {
            databaseConnector.updateContact(rowID, nameEditText.getText().toString(), emailEditText
                    .getText().toString(), phoneEditText.getText().toString(), streetEditText
                    .getText().toString(), cityEditText.getText().toString());
        }
    }
}
