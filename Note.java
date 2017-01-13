package com.grafixartist.noteapp;

import com.google.common.base.Strings;
import com.orm.SugarRecord;

/**
 * Created by Suleiman19 on 1/21/16.
 */
public class Note extends SugarRecord {
    String title, note;
    String uri_video;
    long time;

    public Note() {
    }


    public Note(String title, String note,String uri_video , long time) {
        this.title = title;
        this.note = note;
        this.uri_video = uri_video;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUri_video() {return uri_video;}

    public void setUri_video(String uri_video) {this.uri_video = uri_video;}

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
