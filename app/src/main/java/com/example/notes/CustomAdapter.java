package com.example.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter {

    ArrayList notes;
    ArrayList footnotes;
    Context context;
    MyViewHolder vh;

    public CustomAdapter(Context context, ArrayList notes, ArrayList footnotes) {
        this.context = context;
        this.notes = notes;
        this.footnotes = footnotes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
// set the view's size, margins, paddings and layout parameters
        vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindViewHolder(vh,position);
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
// set the data in items
        holder.note.setText(notes.get(position).toString());
        holder.fn.setText(footnotes.get(position).toString());
// implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// open another activity on item click
                Intent intent = new Intent(context, note_write.class);
                intent.putExtra("noteid", position);
                context.startActivity(intent); // start Intent
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete this note ?")
                        .setMessage("Do you want to delete this note permanently ?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String temp = (String) notes.get(position);
                                notes.remove(position);
                                footnotes.remove(position);

                                String sql = "delete from ndbms where notescontent=?";
                                SQLiteStatement statement = noteslayout.sqLiteDatabase.compileStatement(sql);
                                statement.bindString(1, temp);
                                statement.execute();

                                noteslayout.customAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView note;
        TextView fn;

        public MyViewHolder(View itemView) {
            super(itemView);
            note = (TextView) itemView.findViewById(R.id.heading);
            fn = (TextView) itemView.findViewById(R.id.footnote);
        }
    }
}
