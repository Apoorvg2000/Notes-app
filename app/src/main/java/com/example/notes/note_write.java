package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

public class note_write extends AppCompatActivity {

    int noteid;
    EditText nt;
    String sql;
    int s;
    String n = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_write);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        noteid = getIntent().getIntExtra("noteid", -1);
        nt = findViewById(R.id.noteText);
        if (noteid != -1) {
            sql = "update ndbms set notescontent=?, date=? where notescontent=?;";
            n = noteslayout.notes.get(noteid);
            s = 1;
            nt.setText(noteslayout.notes.get(noteid));
        } else {
            sql = "insert into ndbms (notescontent, date) values (?,?);";
            s = 0;
            noteslayout.notes.add("");
            String date = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss ").format(Calendar.getInstance().getTime());
            noteslayout.footnotes.add(date);
            noteid = noteslayout.notes.size() - 1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sec_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.done_note){
            String note = nt.getText().toString();
            if(note.equals("")){
                Toast.makeText(note_write.this, "Please write something", Toast.LENGTH_SHORT).show();
            }else{
                noteslayout.notes.set(noteid, note);
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(Calendar.getInstance().getTime());
                noteslayout.footnotes.set(noteid, date);

                SQLiteStatement statement = noteslayout.sqLiteDatabase.compileStatement(sql);
                if(s == 0){
                    statement.bindString(1, note);
                    statement.bindString(2, date);
                }else if(s == 1){
                    statement.bindString(1, note);
                    statement.bindString(2, date);
                    statement.bindString(3, n);
                }

                statement.execute();
                noteslayout.customAdapter.notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}