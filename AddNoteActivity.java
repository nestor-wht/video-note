package com.grafixartist.noteapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.jmolsmobile.landscapevideocapture.configuration.CaptureConfiguration;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import java.util.List;

import static android.R.attr.width;
import static java.security.AccessController.getContext;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    FloatingActionButton fab;

    EditText etTitle, etDesc;

    String title, note, uri_video;
    long time;

    boolean editingNote;

    private final String KEY_FILENAME         = "com.jmolsmobile.outputfilename";
    private String filename      = null;

    View mBottomLayout;
    View mVideoLayout;
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Button captureBtn = (Button) findViewById(R.id.btn_capturevideo);
        captureBtn.setOnClickListener(this);

        if (savedInstanceState != null) {filename = savedInstanceState.getString(KEY_FILENAME);}

        toolbar = (Toolbar) findViewById(R.id.addnote_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_clear_24dp);

        getSupportActionBar().setTitle("Add new note");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        etTitle = (EditText) findViewById(R.id.addnote_title);
        etDesc = (EditText) findViewById(R.id.addnote_desc);

        fab = (FloatingActionButton) findViewById(R.id.addnote_fab);


        //  handle intent

//        editingNote = getIntent() != null;
        editingNote = getIntent().getBooleanExtra("isEditing", false);
        if (editingNote) {
            title = getIntent().getStringExtra("note_title");
            note = getIntent().getStringExtra("note");
            uri_video = getIntent().getStringExtra("uri_video");
            time = getIntent().getLongExtra("note_time", 0);

            etTitle.setText(title);
            etDesc.setText(note);
            mVideoView.setVideoPath(uri_video);

        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add note to DB

                String newTitle = etTitle.getText().toString();
                String newDesc = etDesc.getText().toString();
                String newUri = filename;

                long newTime = System.currentTimeMillis();


                /**
                 * TODO: Check if note exists before saving
                 */
                if (!editingNote) {
                    Log.d("Note", "saving");
                    Note note = new Note(newTitle, newDesc,newUri, newTime);
                    note.save();
                } else {
                    Log.d("Note", "updating");

//                    List<Note> notes = Note.findWithQuery(Note.class, "where title = ?", title);
                    List<Note> notes = Note.find(Note.class, "title = ?", title);
                    if (notes.size() > 0) {

                        Note note = notes.get(0);
                        Log.d("got note", "note: " + note.title);
                        note.title = newTitle;
                        note.note = newDesc;
                        note.time = newTime;
                        note.uri_video = newUri;

                        note.save();

                    }

                }

                finish();


            }
        });


        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_capturevideo) {
            startVideoCaptureActivity();
        }

    }

    private void startVideoCaptureActivity() {

        final String filename = etDesc.getEditableText().toString();

        final Intent intent = new Intent(this, VideoCaptureActivity.class);
        intent.putExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME, filename);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            filename = data.getStringExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME);

        } else if (resultCode == Activity.RESULT_CANCELED) {
            filename = null;

        } else if (resultCode == VideoCaptureActivity.RESULT_ERROR) {
            filename = null;

        }
        playVideo();

        super.onActivityResult(requestCode, resultCode, data);
    }

    //videoIntent.setDataAndType(Uri.parse(filename), "video/*");


    public void playVideo(){


            if (filename == null){
                return;
            } else{
                mVideoView.setVideoPath(filename);

            }
    }
}









/*
    public void playVideo(){

        List<Note> notes = Note.find(Note.class, "title = ?", title);
        if (notes.size() > 0) {
            Note note = notes.get(0);
             String uri = note.uri_video;
            if (filename == null){return;} else{
                mVideoView.setMediaController(mMediaController);
                mVideoView.setVideoPath(uri);

            }


        }




    }
    */

