package com.example.danbilap.project_yeobo;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by DANBI LAP on 2016-06-09.
 */
public interface Retrofit {
    @GET("/yeoboH.php") // 4
    public void create_travel(@Query("flag") int flag, @Query("u_id") String u_id,
                              @Query("travel_city") String t_city, @Query("travel_title") String t_title, @Query("travel_start") String t_start,
                              @Query("travel_finish") String t_finish,@Query("c_num") int c_num, Callback<JsonObject> callback);

    @GET("/yeoboH.php") // 6
    public void show_travel(@Query("flag") int flag, @Query("u_id") String u_id, retrofit.Callback<JsonObject> callback);

    @GET("/yeoboH.php") // 7
    public void login(@Query("flag") int flag, @Query("u_id") String id_num,@Query("u_pw") String password, retrofit.Callback<JsonObject> callback);

    @GET("/yeoboH.php") // 2
    public void join(@Query("flag") int flag, @Query("u_id") String id_num,  @Query("u_pw") String password, retrofit.Callback<JsonObject> callback);

    @GET("/yeoboH.php") // 5
    public void info_write(@Query("flag") int flag,@Query("travel_city") String t_city, @Query("city_id") int n_id, retrofit.Callback<JsonObject> callback);


    @GET("/yeoboH.php")
    public void share_write(@Query("flag") int flag,@Query("travel_number") int travel_number, @Query("share_url") String share_Url, @Query("share_imageurl") String share_ImageUrl,@Query("share_description") String share_description,
                            @Query("share_title") String share_title,@Query("c_num") int c_num,retrofit.Callback<JsonObject>callback); //공유해서 보낼 데이터
    @GET("/yeoboH.php")
    public void share_read(@Query("travel_number") int travel_number, @Query("category") String sharedCategory, @Query("flag") int flag, retrofit.Callback<JsonObject>callback);

    @GET("/yeoboH.php") // 2
    public void emailCheck(@Query("flag") int flag,@Query("email") String email, retrofit.Callback<JsonObject> callback);

    @GET("/yeoboH.php") // 9
    public void delete(@Query("flag") int flag,@Query("t_num") int t_num, retrofit.Callback<JsonObject> callback);

    @GET("/yeoboH.php") // 10
    public void getPass(@Query("flag") int flag,@Query("u_id") String u_id, retrofit.Callback<JsonObject> callback);
    @GET("/yeobo.php") //8 메모생성 db에 저장
    public void create_memo(@Query("flag") int flag, @Query("travel_number") int travel_number,@Query("memo_title") String memo_title,@Query("memo_content") String memo_content,
                            @Query("c_num") int c_num,retrofit.Callback<JsonObject> callback);
    @GET("/yeobo.php") //12 db에 있는 share 정보 불러오기
    public void show_memo(@Query("flag") int flag, @Query("travel_number") int travel_number, retrofit.Callback<JsonObject> callback);

    @GET("/yeobo.php") //13 db에 있는 share 정보 삭제
    public void delete_memo( @Query("flag") int flag,@Query("share_num") int share_num, retrofit.Callback<JsonObject>callback);
}