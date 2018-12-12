package com.example.namp5.contentprovider_sqlite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by namp5 on 12/7/2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<Contact> mContacts;
    private LayoutInflater mInflater;
    private OnClickItemSongListener mContactListener;

    public ContactAdapter(Context context , ArrayList<Contact> contacts) {
        this.mContacts = contacts;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view, mContactListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return (mContacts != null) ? mContacts.size() : 0;
    }

    public void setItemClickListener(OnClickItemSongListener listener) {
        mContactListener = listener;
    }

    public void addContact(Contact contact) {
        mContacts.add(contact);
        notifyItemInserted(mContacts.size() - 1);
    }

    public Contact getContact(int position) {
        return mContacts.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mAvatar;
        private TextView mName;
        private TextView mPhone;
        private ImageView mCall;
        private OnClickItemSongListener mListener;
        private Contact mContact;

        public ViewHolder(View itemView, OnClickItemSongListener listener) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.text_avatar);
            mName = itemView.findViewById(R.id.text_name);
            mPhone = itemView.findViewById(R.id.text_phone);
            mCall = itemView.findViewById(R.id.image_call);
            mCall.setOnClickListener(this);
            itemView.setOnClickListener(this);
            mListener = listener;
        }

        public void bindData(Contact contact) {
            mContact = contact;
            mName.setText(contact.getName());
            mPhone.setText(contact.getPhone());
            mAvatar.setText(String.valueOf(contact.getName().charAt(0)));
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image_call:
                    mListener.onCallClick(getAdapterPosition());
                    break;
                default:
                    mListener.onContactClick(mContact);
                    break;
            }
        }
    }

    interface OnClickItemSongListener {
        void onContactClick(Contact contact);
        void onCallClick(int position);
    }
}
