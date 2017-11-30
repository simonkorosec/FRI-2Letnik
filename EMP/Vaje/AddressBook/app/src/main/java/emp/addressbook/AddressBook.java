package emp.addressbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AddressBook extends AppCompatActivity {
    public static final String ROW_ID = "row_id";
    private ListView contactListView;
    private CursorAdapter contactAdapter;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressbook);

        contactListView = findViewById(R.id.listView);
        contactListView.setOnItemClickListener(viewContactListener);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent addNewContact = new Intent(AddressBook.this, AddEditContact.class);
                startActivity(addNewContact);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] from = new String[]{"name"};
        int[] to = new int[]{R.id.contactNameTextView};

        contactAdapter = new SimpleCursorAdapter(AddressBook.this,
                R.layout.contact_list_item, null, from, to, 0);
        contactListView.setAdapter(contactAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseConnector databaseConnector = new DatabaseConnector(getApplicationContext());
        databaseConnector.open();
        contactAdapter.changeCursor(databaseConnector.getAllContacts());
        databaseConnector.close();
    }

    @Override
    protected void onStop() {
        contactAdapter.changeCursor(null);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addressbook_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent addNewContact = new Intent(AddressBook.this, AddEditContact.class);
        startActivity(addNewContact);
        return super.onOptionsItemSelected(item);
    }

    OnItemClickListener viewContactListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            final Intent viewContact = new Intent(AddressBook.this, ViewContact.class);
            viewContact.putExtra(ROW_ID, arg3);
            startActivity(viewContact);
        }
    };
}

