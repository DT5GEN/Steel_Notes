package com.task.steel_notes.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class NoteData implements Parcelable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;          // id
    private String title;       // заголовок
    private String description; // описание
    private int picture;        // изображение
    private boolean done;       // флажок
    private Date date;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    protected NoteData(Parcel in) {
        title = in.readString();
        description = in.readString();
        picture = in.readInt();
        done = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(picture);
        dest.writeByte((byte) (done ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NoteData> CREATOR = new Creator<NoteData>() {
        @Override
        public NoteData createFromParcel(Parcel in) {
            return new NoteData(in);
        }

        @Override
        public NoteData[] newArray(int size) {
            return new NoteData[size];
        }
    };

    public void setTitle(String title) {
        this.title = title;
    }



    public Date getDate() {
        return date;
    }


    public NoteData(String title, String description, int picture, boolean done, Date date){
        this.title = title;
        this.description=description;
        this.picture=picture;
        this.done = done;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }

    public int getPicture() {
        return picture;
    }

    public boolean isDone() {
        return done;
    }
}