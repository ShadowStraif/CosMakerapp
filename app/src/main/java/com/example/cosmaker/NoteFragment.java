package com.example.cosmaker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosmaker.Adapter.NoteAdapter;
import com.example.cosmaker.Model.NoteModel;
import com.example.cosmaker.Utils.DataBaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteFragment extends Fragment implements DialogCloseNote {

     private FloatingActionButton fab;
    private int charid,flag;
    private DataBaseHandler db;
    private RecyclerView noteRecyclerView;
    private NoteAdapter noteAdapter;
    private List<NoteModel> noteList;

    public NoteFragment() {
    }

    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        charid = getArguments().getInt("idnf",0);
        flag =  getArguments().getInt("flag",0);
        return inflater.inflate(R.layout.note_fragment, container, false);
    }


    public void onViewCreated (View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        db = new DataBaseHandler(this.getActivity());
        db.openDatabase();
        noteList=new ArrayList<>();
        noteRecyclerView = getView().findViewById(R.id.noteResView);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        noteAdapter=new NoteAdapter(db,this);
        noteRecyclerView.setAdapter(noteAdapter);

       noteList=db.getAllNotess(charid);
        Collections.reverse(noteList);
        noteAdapter.setinfoNote(noteList);

        fab=getView().findViewById(R.id.fabNote);
        if(flag==0) {
            ItemTouchHelper itemTouchHelper = new
                    ItemTouchHelper(new RecyclerItemTouchHelperNote(noteAdapter));
            itemTouchHelper.attachToRecyclerView(noteRecyclerView);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AddNewNote.newInstance(charid).show(getFragmentManager(), AddNewNote.TAG1);


                }


            });
        }
        else  fab.setVisibility(View.INVISIBLE);

    }

    @Override
    public void handleDialogCloseNote(DialogInterface dialog1){

        noteList=db.getAllNotess(charid);
        Collections.reverse(noteList);
        noteAdapter.setinfoNote(noteList);
        noteAdapter.notifyDataSetChanged();
    }



}
