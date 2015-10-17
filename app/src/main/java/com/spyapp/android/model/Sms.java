package com.spyapp.android.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import static com.spyapp.android.provider.SpyContracts.Sms.Columns.DATE;
import static com.spyapp.android.provider.SpyContracts.Sms.Columns.MESSAGE;
import static com.spyapp.android.provider.SpyContracts.Sms.Columns.RECIPIENT;
import static com.spyapp.android.provider.SpyContracts.Sms.Columns.SENDER;
import static com.spyapp.android.provider.SpyContracts.Sms.Columns.TYPE;
import static com.spyapp.android.provider.SpyContracts.Sms.Columns._ID;

public class Sms implements Parcelable {

    private long id;
    private int type;
    private String sender;
    private String recipient;
    private String message;
    private long date;

    public Sms() {}

    public Sms(long id, int type, String sender,
               String recipient, String message, long date)
    {
        this.id = id;
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.date = date;
    }

    public Sms(Parcel in) {
        id = in.readLong();
        type = in.readInt();
        sender = in.readString();
        recipient = in.readString();
        message = in.readString();
        date = in.readLong();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ContentValues prepareContentValues() {
        ContentValues values = new ContentValues();
        values.put(_ID, id);
        values.put(TYPE, type);
        values.put(SENDER, sender);
        values.put(RECIPIENT, recipient);
        values.put(MESSAGE, message);
        values.put(DATE, date);
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeInt(type);
        out.writeString(sender);
        out.writeString(recipient);
        out.writeString(message);
        out.writeLong(date);
    }

    public static final Parcelable.Creator<Sms> CREATOR = new Parcelable.Creator<Sms>() {

        public Sms createFromParcel(Parcel in) {
            return new Sms(in);
        }

        public Sms[] newArray(int size) {
            return new Sms[size];
        }

    };

}