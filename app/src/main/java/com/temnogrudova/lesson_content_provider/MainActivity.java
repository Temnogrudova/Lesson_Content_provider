package com.temnogrudova.lesson_content_provider;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    final String LOG_TAG = "myLogs";
    final String CONTACT_NAME = "name";
    final String CONTACT_EMAIL = "email";
    final Uri CONTACT_URI = Uri
            .parse("content://com.temnogrudova.providers.AdressBook/contacts");
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    SimpleCursorAdapter mAdapter;
    Button btnInsert;
    Button btnDelete;
    Button btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String from[] = { "name", "email" };
        int to[] = { android.R.id.text1, android.R.id.text2 };
        mAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,
                null, from, to, 0);
        ListView lvContact = (ListView) findViewById(R.id.lvContact);
        // Associate the (now empty) adapter with the ListView.
        lvContact.setAdapter(mAdapter);

        mCallbacks = this;

        LoaderManager lm = getLoaderManager();
        lm.initLoader(1, null, mCallbacks);

        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(CONTACT_NAME, "name 4");
                cv.put(CONTACT_EMAIL, "email 4");
                Uri newUri = getContentResolver().insert(CONTACT_URI, cv);
                Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
            }
        });
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(CONTACT_NAME, "name 5");
                cv.put(CONTACT_EMAIL, "email 5");
                Uri uri = ContentUris.withAppendedId(CONTACT_URI, 2);
                int cnt = getContentResolver().update(uri, cv, null, null);
                Log.d(LOG_TAG, "update, count = " + cnt);
            }
        });
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = ContentUris.withAppendedId(CONTACT_URI, 3);
                int cnt = getContentResolver().delete(uri, null, null);
                Log.d(LOG_TAG, "delete, count = " + cnt);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(MainActivity.this, CONTACT_URI,
                null, null, null, null);
        // Cursor cursor = getContentResolver().query(CONTACT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 1:
                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the SimpleCursorAdapter.
                mAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }

}
