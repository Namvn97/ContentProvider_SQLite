package com.example.namp5.contentprovider_sqlite;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnClickItemSongListener {
    public static final int REQUEST_READ_CONTACTS = 101;
    public static final int REQUEST_CALL_PHONE = 102;
    public static final String STRING_TEL = "tel:";
    public static final String SORT_ORDER = "DISPLAY_NAME ASC";
    private ContactAdapter mAdapter;
    private String mPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        checkPermission(Manifest.permission.READ_CONTACTS, REQUEST_READ_CONTACTS);
    }

    private void initView() {
        RecyclerView recycler = findViewById(R.id.recycler_contacts);
        mAdapter = new ContactAdapter(this );
        mAdapter.setItemClickListener(this);
        DividerItemDecoration decoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recycler.addItemDecoration(decoration);
        recycler.setAdapter(mAdapter);
    }

    public void showMessage(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    private void checkPermission(String permission, int requestCode) {
        String[] permissions = new String[]{permission};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, requestCode);
            } else doFunction(permission);
        }
    }

    private void doFunction(String permission) {
        switch (permission) {
            case Manifest.permission.READ_CONTACTS:
                readContacts();
                break;
            case Manifest.permission.CALL_PHONE:
                makeCall(mPhoneNumber);
                break;
            default:
                break;
        }
    }

    public static Intent getCallIntent(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(castString(STRING_TEL, phoneNumber)));
        return callIntent;
    }

    @SuppressLint("MissingPermission")
    private void makeCall(String phoneNumber) {
        Intent intent = getCallIntent(phoneNumber);
        startActivity(intent);
    }

    private void readContacts() {
        Uri contactsTable = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Cursor cursor = getContentResolver()
                .query(contactsTable, projection,
                        null, null, SORT_ORDER);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String phone = cursor.getString(2);
            mAdapter.addContact(new Contact(id, name, phone));
        }
    }



    public static String castString(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append(string);
        }
        return builder.toString();
    }

    public void requestCallPhone(int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkPermission(Manifest.permission.CALL_PHONE, REQUEST_CALL_PHONE);
        } else {
            showMessage(findViewById(R.id.linear_main_layout),
                    getString(R.string.msg_error_call));
        }
    }

    public void requestReadContact(int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkPermission(Manifest.permission.READ_CONTACTS, REQUEST_READ_CONTACTS);
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CALL_PHONE:
                requestCallPhone(grantResults);
                break;
            case REQUEST_READ_CONTACTS:
                requestReadContact(grantResults);
                break;
        }
    }

    @Override
    public void onContactClick(Contact contact) {
        Snackbar.make(findViewById(R.id.linear_main_layout),
                contact.getName(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCallClick(int position) {
        Contact contact = mAdapter.getContact(position);
        mPhoneNumber = contact.getPhone();
        checkPermission(Manifest.permission.CALL_PHONE, REQUEST_CALL_PHONE);
    }
}
