package me.zhouzhuo810.zznote.api;

import io.reactivex.Observable;
import me.zhouzhuo810.zznote.api.entity.GetNotesEntity;
import me.zhouzhuo810.zznote.api.entity.HeartBeatEntity;
import me.zhouzhuo810.zznote.api.entity.ModifyNoteContentEntity;
import retrofit2.http.*;



/**
 * 默认分组
 */
public interface Api0 {
    /*
     * 获取便签内容()
     */
    @GET("getNote")
    Observable<GetNotesEntity> getOpenedNote();

    @GET("heartBeat")
    Observable<HeartBeatEntity> heartBeat();


    /*
     * 修改便签内容()
     */
    @FormUrlEncoded
    @POST("modifyNoteContent")
    Observable<ModifyNoteContentEntity> modifyNoteContent(
            @Field("noteId") long noteId, //便签id
            @Field("content") String content //便签内容
    );

    /*
     * 修改便签内容()
     */
    @FormUrlEncoded
    @POST("updateSelection")
    Observable<ModifyNoteContentEntity> updateSelection(
            @Field("noteId") long noteId, //便签id
            @Field("start") int start, //便签id
            @Field("end") int end //便签内容
    );
}