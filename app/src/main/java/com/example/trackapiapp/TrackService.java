package com.example.trackapiapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TrackService {
    @GET("tracks")
    Call<List<Track>> listTracks();

    @POST("tracks")
    Call<Track> createTrack(@Body Track track);

    @PUT("tracks")
    Call<Track> updateTrack(@Body Track track);

    @DELETE("tracks/{id}")
    Call<Void> deleteTrack(@Path("id") String id);
}
