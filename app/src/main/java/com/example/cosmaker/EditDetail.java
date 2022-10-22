package com.example.cosmaker;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cosmaker.Adapter.DetailsAdapter;
import com.example.cosmaker.Model.DetailsModel;
import com.example.cosmaker.Model.NoteModel;
import com.example.cosmaker.Utils.DataBaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class EditDetail  extends BottomSheetDialogFragment {
    public static final String TAG2= "ActionBottomDialog";
    private List<DetailsModel> detailsList;
    private DetailsAdapter detailsAdapter;
    public DataBaseHandler db;
    private EditText detailname;
    private Spinner type,level;
    private TextView typeText,levelText;
    private Button editDetailSaveButton;
    private static int id;
    private int it2;
    public static EditDetail newInstance(int charid) {
        id = charid;
        return new EditDetail();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_detail_layout, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editDetailSaveButton=getView().findViewById(R.id.editSaveDetailButton);
        detailname=getView().findViewById(R.id.editDetailName);
        typeText=getView().findViewById(R.id.edittypeText);
        levelText=getView().findViewById(R.id.editpriorityText);
        type = getView().findViewById(R.id.edittypeSpin);
        level=  getView().findViewById(R.id.editprioritySpin);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item =(String)parent.getItemAtPosition(position);
                typeText.setText(item);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                it2 = Integer.parseInt(parent.getItemAtPosition(position).toString());
                levelText.setText(String.valueOf(it2));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        db = new DataBaseHandler(getActivity());
        db.openDatabase();
        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String detailn = bundle.getString("editnamedetail");
            String typen = bundle.getString("edittypedetail");
           int leveln = bundle.getInt("editleveldetail",0);
            detailname.setText(detailn);
           typeText.setText(typen);
            levelText.setText(String.valueOf(leveln));
            ArrayAdapter adapter = (ArrayAdapter) type.getAdapter();
            int position = adapter.getPosition(typen);
            type.setSelection(position);

            ArrayAdapter adapter1 = (ArrayAdapter) level.getAdapter();
            int position1 = adapter1.getPosition(leveln);
            level.setSelection(position1);

            if (detailn.length() > 0)
                editDetailSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        }


        final boolean finalIsUpdate = isUpdate;

        editDetailSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String detailnametxt= detailname.getText().toString();
                String typetext= typeText.getText().toString();
                String leveltext= levelText.getText().toString();
                if(finalIsUpdate){
                    db.updateDetail(bundle.getInt("editiddetail"), detailnametxt,typetext,it2);
                }
                else
                {
                    DetailsModel detail=new DetailsModel();
                    detail.setDetailName(detailnametxt);
                    detail.setStatus(0);
                    detail.setDetailType(typetext);
                    detail.setDetailLevel(it2);
                    detail.setCharid(id);
                    db.insertDetail(detail, id);

                }
                ((Costume) getActivity()).loadFragment(CostumeFragment.newInstance());
                dismiss();
            }

        });







        detailname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    editDetailSaveButton.setEnabled(false);
                    editDetailSaveButton.setTextColor(Color.GRAY);
                } else {
                    editDetailSaveButton.setEnabled(true);
                    editDetailSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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