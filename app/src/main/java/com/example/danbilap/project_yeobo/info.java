package com.example.danbilap.project_yeobo;
import android.os.Parcel;
import android.os.Parcelable;

public class info implements Parcelable {
    String t_title;
    String t_addr;
    String t_tel;
    String t_img;//
    String t_img2;
    //int c_num;

//    String imgurl;

    info(String title, String addr, String tel, String img, String img2){
        this.t_title = title;
        this.t_addr = addr;
        this.t_tel = tel;
        this.t_img=img;
        this.t_img2=img2;
 //       this.imgurl=imgurl;
    }

    protected info(Parcel in) {
        t_title = in.readString();

        t_addr = in.readString();
        t_tel = in.readString();
        t_img=in.readString();
        t_img2=in.readString();
    }

    public static final Creator<info> CREATOR = new Creator<info>() {
        @Override
        public info createFromParcel(Parcel in) {
            return new info(in);
        }

        @Override
        public info[] newArray(int size) {
            return new info[size];
        }
    };

    public String getT_title(){
        return t_title;
    }
    public String getT_addr(){
        return t_addr;
    }
    public String getT_tel(){
        return t_tel;
    }
    public String getT_img(){
        return t_img;
    }
    public String getT_img2(){return t_img2;}




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(t_title);
        dest.writeString(t_addr);
        dest.writeString(t_tel);
        dest.writeString(t_img);
        dest.writeString(t_img2);
  //      dest.writeString(imgurl);
    }
}

