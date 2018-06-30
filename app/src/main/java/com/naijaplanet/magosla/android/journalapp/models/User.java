package com.naijaplanet.magosla.android.journalapp.models;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * User Parcelable class
 */
public class User implements Parcelable{
    private final String id;
    private final String name;
    private final String email;
    private final String photoUrl;

    // This is to de-serialize the object
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>(){
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @SuppressWarnings("WeakerAccess")
    public User(Parcel in){
        id = in.readString();
        name = in.readString();
        email = in.readString();
        photoUrl = in.readString();
    }

    public User(String id, String name, String email, String photoUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(photoUrl);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @SuppressWarnings("unused")
    public String getPhotoUrl() {
        return photoUrl;
    }
}
