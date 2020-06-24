package com.ekarantechnologies.wandi.Network;




import com.ekarantechnologies.wandi.models.Foldermusic;
import com.ekarantechnologies.wandi.models.artist;
import com.ekarantechnologies.wandi.models.Artistmusic;
import com.ekarantechnologies.wandi.models.folders;
import com.ekarantechnologies.wandi.models.music;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("getallmusic.php")
    Call<List<music>> getmusic(@Query("pageno") int page, @Query("itemcount") int items, @Query("search") String query);

    @GET("getartists.php")
    Call<List<artist>> getartists(@Query("pageno") int page, @Query("itemcount") int items, @Query("search") String query);

    @GET("getfolders.php")
    Call<List<folders>> getfolders(@Query("pageno") int page, @Query("itemcount") int items, @Query("search") String query);

    @GET("getartistmusic.php")
    Call<List<Artistmusic>> getmusicfromartist(@Query("pageno") int page, @Query("itemcount") int items, @Query("search") String query, @Query("musicid") int id);

    @GET("getfoldermusic.php")
    Call<List<Foldermusic>> getmusicfromfolder(@Query("pageno") int page, @Query("itemcount") int items, @Query("search") String query, @Query("musicid")int id);
  /*  @FormUrlEncoded
    @POST("login.php")
     Call<ResponseBody> performuserLogin(@FieldMap Map<String, String> fields);*/
}
