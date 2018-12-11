package com.example.namp5.contentprovider_sqlite;

public class Contact {
    private String mId;
    private String mName;
    private String mPhone;

    public Contact(String id, String name, String phone) {
        mId = id;
        mName = name;
        mPhone = phone;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

}
