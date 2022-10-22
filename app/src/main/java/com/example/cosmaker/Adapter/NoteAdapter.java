package com.example.cosmaker.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosmaker.AddNewNote;
import com.example.cosmaker.Costume;


import com.example.cosmaker.Model.DetailsModel;
import com.example.cosmaker.Model.NoteModel;
import com.example.cosmaker.NoteFragment;
import com.example.cosmaker.R;
import com.example.cosmaker.Utils.DataBaseHandler;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<NoteModel> noteList= new ArrayList<>();
    private NoteFragment activity;
    private DataBaseHandler db;


    public int getItemCount(){
        return noteList.size();
    }

    public NoteAdapter(DataBaseHandler db, NoteFragment activity)
    {
        this.db=db;
        this.activity=activity;

    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View noteview= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_layout, parent,false);
        return new NoteAdapter.ViewHolder(noteview);

    }

    public void setinfoNote(List<NoteModel> noteList){
        this.noteList=noteList;
        notifyDataSetChanged();
    }

    private boolean toBoolean(int n)
    {
        return n!=0;
    }
    public Context getContext() {
        return activity.getActivity();
    }
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position){
        db.openDatabase();
        final NoteModel item = noteList.get(position);
        holder.layoutNoteName.setText(item.getNotetext());
        holder.noteCheckBox.setChecked(toBoolean(item.getStatus()));
       holder.noteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    db.updateNoteStatus(item.getId(), 1);

                }
                else{
                    db.updateNoteStatus(item.getId(),0);
                }
            }
        });
        if(db.isFinished(item.getCharacterid())) {
            holder.noteCheckBox.setVisibility(View.INVISIBLE);
        }
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox noteCheckBox;
        TextView layoutNoteName;
        ViewHolder(View view){
            super(view);
            noteCheckBox=view.findViewById(R.id.noteCheckBox);
            layoutNoteName=view.findViewById(R.id.layoutNoteName);

        }

    }

    public void deleteItem(int position) {
        NoteModel item = noteList.get(position);
        db.deleteNote(item.getId());
        noteList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
       NoteModel item = noteList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("ideditnote", item.getId());
        bundle.putString("texteditnote", item.getNotetext());
        AddNewNote fragment = new AddNewNote();
        fragment.setArguments(bundle);
        fragment.show(activity.getActivity().getSupportFragmentManager(), AddNewNote.TAG1);
    }

}
