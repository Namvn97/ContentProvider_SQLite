package com.example.namp5.contentprovider_sqlite;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class MyProvider {
    private static final String SQL_WHERE = " = ? ";
    private ContentResolver mContentResolver;

    public MyProvider(Context context) {
        mContentResolver = context.getContentResolver();
    }

    public ArrayList<Contact> getContactFormDevice() {
        ArrayList<Contact> contacts = new ArrayList<>();
        Cursor cursor = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor != null && cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                int numberOfPhones = cursor.getInt(cursor.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (numberOfPhones > 0) {
                    Cursor phoneCursor = mContentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            MainActivity.castString(
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID, SQL_WHERE),
                            new String[]{id},
                            null
                    );
                    while (phoneCursor.moveToNext()) {
                        String phone = phoneCursor.getString(phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contacts.add(new Contact(name, phone));
                    }
                    phoneCursor.close();
                }
            }
            cursor.close();
        }
        return contacts;
    }
}
