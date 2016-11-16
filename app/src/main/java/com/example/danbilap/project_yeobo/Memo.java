package com.example.danbilap.project_yeobo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daeun on 2016-11-10.
 */
public class Memo implements Parcelable {
    int s_num;
    int t_num;
    String s_url;
    String i_url;
    String s_description;
    String s_title;
    String m_title;
    String m_content;



    Memo(int s_num, int t_num, String s_url, String i_url, String s_description, String s_title, String m_title, String m_content){


        this.s_num = s_num;
        this.t_num = t_num;
        this.s_url = s_url;
        this.i_url = i_url;
        this.s_description = s_description;
        this.s_title = s_title;
        this.m_title = m_title;
        this.m_content = m_content;

    }

    protected Memo(Parcel in) {
        s_num = in.readInt();
        t_num = in.readInt();
        s_url = in.readString();
        i_url = in.readString();
        s_description = in.readString();
        s_title = in.readString();
        m_title = in.readString();
        m_content = in.readString();
    }

    public static final Creator<Memo> CREATOR = new Creator<Memo>() {
        @Override
        public Memo createFromParcel(Parcel in) {
            return new Memo(in);
        }

        @Override
        public Memo[] newArray(int size) {
            return new Memo[size];
        }
    };

    public void setS_num(int s_num) { this.s_num = s_num; }
    public void setT_num(int t_num) {
        this.t_num = t_num;
    }
    public void setS_url(String s_url) { this.s_url = s_url; }
    public void setI_url(String i_url) { this.i_url = i_url; }
    public void setS_description(String s_description) { this.s_description = s_description; }
    public void setS_title(String s_title){ this.s_title = s_title; }
    public void setM_title(String m_title) {
        this.m_title = m_title;
    }
    public void setM_content(String m_content) {
        this.m_content = m_content;
    }

    public int getS_num() { return s_num; }
    public int getT_num() { return t_num; }
    public String getS_url() { return s_url; }
    public String getI_url() { return i_url; }
    public String getS_description() {return s_description; }
    public String getS_title() {return s_title; }
    public String getM_title(){
        return m_title;
    }
    public String getM_content(){
        return m_content;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(s_num);
        dest.writeInt(t_num);
        dest.writeString(s_url);
        dest.writeString(i_url);
        dest.writeString(s_description);
        dest.writeString(s_title);
        dest.writeString(m_title);
        dest.writeString(m_content);
    }
}