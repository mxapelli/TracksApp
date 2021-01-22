package com.example.trackapiapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends  AppCompatActivity implements AddTrackDialog.NoticeDialogListener, EditTrackDialog.EditDialogListener{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public TextInputLayout nameInput, singerInput;
    public static TrackService service;
    public View view;
    public static FragmentManager fragmentManager;
    Context context;
    public static String clickedSong, clickedArtist, clickedID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        view = findViewById(android.R.id.content).getRootView();
        fragmentManager = getSupportFragmentManager();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        context = getApplicationContext();
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/dsaApp/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(TrackService.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Call<List<Track>> tracks = service.listTracks();
        tracks.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                mAdapter = new MyAdapter(response.body());
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable throwable) {

            }
        });
    }
    public void reloadAction(View v) {
        Call<List<Track>> tracks = service.listTracks();
        tracks.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                mAdapter = new MyAdapter(response.body());
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable throwable) {

            }
        });
    }

    public void addPopUpOpener(View v) {
        AddTrackDialog d = new AddTrackDialog();
        d.show(getSupportFragmentManager(),"newdialog");

    }

    @Override
    public void onDialogSongAdded(DialogFragment dialog) {
        Toast.makeText(context,"Canción añadida",Toast.LENGTH_LONG).show();
    }

    @Override
    public void errorAddingSong(DialogFragment dialog) {
        Toast.makeText(context,"No se pudo añadir la cancion",Toast.LENGTH_LONG).show();
    }

    public static void ClickedSong() {
        EditTrackDialog d = new EditTrackDialog();
        d.show(fragmentManager,"newdialog");
    }

    @Override
    public void onDialogSongEdited(DialogFragment dialog) {
        Toast.makeText(context,"Canción editada",Toast.LENGTH_LONG).show();
    }

    @Override
    public void errorEditingSong(DialogFragment dialog) {
        Toast.makeText(context,"No se pudo editar la cancion",Toast.LENGTH_LONG).show();
    }

    @Override
    public void deletedSong(DialogFragment dialog) {
        Toast.makeText(context,"Cancion borrada",Toast.LENGTH_LONG).show();
    }
}