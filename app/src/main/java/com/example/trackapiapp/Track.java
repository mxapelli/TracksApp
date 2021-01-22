package com.example.trackapiapp;

public class Track {
    String id;
    String singer;
    String title;

    public Track(String title, String singer, String id) {
        this.id = id;
        this.singer = singer;
        this.title = title;
    }

    public Track(String singer, String title) {
        this.singer = singer;
        this.title = title;
    }

    @Override
    public String toString() {
        return this.singer + " " + this.title;
    }
}