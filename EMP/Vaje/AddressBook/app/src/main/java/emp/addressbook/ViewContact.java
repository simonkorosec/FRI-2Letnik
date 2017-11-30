package emp.addressbook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewContact extends AppCompatActivity {
    private long rowID; // selected contact's name
    private TextView nameTextView; // displays contact's name
    private TextView phoneTextView; // displays contact's phone
    private TextView emailTextView; // displays contact's email
    private TextView streetTextView; // displays contact's street
    private TextView cityTextView; // displays contact's city/state/zip

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contact);

        nameTextView = findViewById(R.id.nameTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        emailTextView = findViewById(R.id.emailTextView);
        streetTextView = findViewById(R.id.streetTextView);
        cityTextView = findViewById(R.id.cityTextView);

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rowID = extras.getLong(AddressBook.ROW_ID);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseConnector databaseConnector = new DatabaseConnector(getApplicationContext());
        databaseConnector.open();
        Cursor result = databaseConnector.getOneContact(rowID);
        result.moveToFirst();
        int nameIndex = result.getColumnIndex("name");
        int phoneIndex = result.getColumnIndex("phone");
        int emailIndex = result.getColumnIndex("email");
        int streetIndex = result.getColumnIndex("street");
        int cityIndex = result.getColumnIndex("city");

        nameTextView.setText(result.getString(nameIndex));
        phoneTextView.setText(result.getString(phoneIndex));
        emailTextView.setText(result.getString(emailIndex));
        streetTextView.setText(result.getString(streetIndex));
        cityTextView.setText(result.getString(cityIndex));

        result.close();
        databaseConnector.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.editItem :
                Intent addEditContact = new Intent(this, AddEditContact.class);
                addEditContact.putExtra(AddressBook.ROW_ID, rowID);
                addEditContact.putExtra("name", nameTextView.getText());
                addEditContact.putExtra("phone", phoneTextView.getText());
                addEditContact.putExtra("email", emailTextView.getText());
                addEditContact.putExtra("street", streetTextView.getText());
                addEditContact.putExtra("city", cityTextView.getText());
                startActivity(addEditContact);
                return true;
            case R.id.deleteItem :
                deleteContact();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteContact() {
        DatabaseConnector databaseConnector = new DatabaseConnector(getApplicationContext());
        databaseConnector.deleteContact(rowID);
        databaseConnector.close();
        finish();
    }
}
