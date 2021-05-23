package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class noteslayout extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayList<String> footnotes = new ArrayList<>();
    static CustomAdapter customAdapter;
    static SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteslayout);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        sqLiteDatabase = this.openOrCreateDatabase("ndbms",MODE_PRIVATE,null);

        sqLiteDatabase.execSQL("create table if not exists ndbms (notescontent TEXT, date TEXT);");
//        sqLiteDatabase.execSQL("delete from ndbms");

        updateRecyclerView();

        customAdapter = new CustomAdapter(noteslayout.this, notes, footnotes);
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_note){
            Intent intent = new Intent(noteslayout.this, note_write.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateRecyclerView() {

        Cursor c = sqLiteDatabase.rawQuery("select * from ndbms",null);

        int notesIdx = c.getColumnIndex("notescontent");
        int dateIdx = c.getColumnIndex("date");

        if(c != null && c.moveToFirst()){

            notes.clear();
            footnotes.clear();

            while(c.moveToNext()) {
                String note = c.getString(notesIdx);
                String date = c.getString(dateIdx);

                notes.add(note);
                footnotes.add(date);

            }
        }
    }
}