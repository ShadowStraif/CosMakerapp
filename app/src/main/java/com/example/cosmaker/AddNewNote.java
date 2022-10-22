package com.example.cosmaker;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosmaker.Adapter.NoteAdapter;
import com.example.cosmaker.Model.NoteModel;
import com.example.cosmaker.Utils.DataBaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class AddNewNote extends BottomSheetDialogFragment  {
    public static final String TAG1 = "ActionBottomDialog";
    private EditText newNoteText;
    private Button newNoteSaveButton;
    public DataBaseHandler db;
    private static int id;
    private RecyclerView noteRecyclerView;
    private NoteAdapter noteAdapter;
    private List<NoteModel> noteList;

    public static AddNewNote newInstance(int charid) {
        id = charid;
        return new AddNewNote();
    }
    public interface EditNoteDialogListener {
        void onFinishEditDialog(String inputText);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_new_detail, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newNoteText = getView().findViewById(R.id.newNoteText);
        newNoteSaveButton = getView().findViewById(R.id.newNoteButton);
        newNoteSaveButton.setEnabled(false);
        db = new DataBaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String note = bundle.getString("texteditnote");
            newNoteText.setText(note);
            if (note.length() > 0)
                newNoteSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        }
        newNoteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newNoteSaveButton.setEnabled(false);
                    newNoteSaveButton.setTextColor(Color.GRAY);
                } else {
                    newNoteSaveButton.setEnabled(true);
                    newNoteSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final boolean finalIsUpdate = isUpdate;
        newNoteSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newNoteText.getText().toString();
                if(finalIsUpdate){
                    db.updateNote(bundle.getInt("ideditnote"), text);
                }
                else {
                    NoteModel note = new NoteModel();
                    note.setNotetext(text);
                    note.setStatus(0);
                    note.setCharacterid(id);
                    db.insertNote(note, id);

                }
                ((Costume) getActivity()).loadFragment(NoteFragment.newInstance());
                dismiss();

            }

        });
    }

    @Override
    public void onDismiss(DialogInterface dialog1){
        Fragment activity = getParentFragment();
        if(activity instanceof DialogCloseNote){
            ((DialogCloseNote)activity).handleDialogCloseNote(dialog1);
        }

    }



}


