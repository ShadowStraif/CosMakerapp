package com.example.cosmaker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cosmaker.Adapter.DetailsAdapter;
import com.example.cosmaker.Model.CosMakerModel;
import com.example.cosmaker.Model.DetailsModel;
import com.example.cosmaker.Utils.DataBaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EditCostume extends BottomSheetDialogFragment {
    public static final String TAG3= "ActionBottomDialog";
    private EditText CostumeText,Fandom,editDate;
    private ImageView cosimage;
    private Button CostumeSaveButton;
    private Button newImage;
    private CalendarView calender;

    public DataBaseHandler db;
    private Bitmap bitmap;
    private List<CosMakerModel> characterList;
    private int charid;
    private static final int GALLERY_REQUEST = 1;
    public static EditCostume newInstance(){
        return new EditCostume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_character_layout, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated (View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        newImage = getView().findViewById(R.id.editImageButton);
        cosimage=getView().findViewById(R.id.editImage);
        editDate=getView().findViewById(R.id.editStartDate);
        calender = getView().findViewById(R.id.editcalendarView);
        calender.setVisibility(View.INVISIBLE);
        editDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    calender.setVisibility(View.VISIBLE);
                    newImage.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {


                editDate.setText(" " + dayOfMonth +" / " + (month+1) + " / " + year);
                calender.setVisibility(View.INVISIBLE);
                newImage.setVisibility(View.VISIBLE);
            }
        });
        CostumeText = getView().findViewById(R.id.editCosName);
        Fandom = getView().findViewById(R.id.editFandomName);

        CostumeSaveButton = getView().findViewById(R.id.editCosSaveButton);
        CostumeSaveButton.setEnabled(false);
        db = new DataBaseHandler(getActivity());
        db.openDatabase();


        CostumeText.addTextChangedListener(textWatcher());
        Fandom.addTextChangedListener(textWatcher());
        editDate.addTextChangedListener(textWatcher());
        final Bundle bundle = getArguments();
        charid = bundle.getInt("ideditcharacter");
        characterList=db.getCharacterbyid(charid);
        CosMakerModel character = new CosMakerModel();
        CostumeText.setText(characterList.get(0).getCharacterName());
        Fandom.setText(characterList.get(0).getFandom());
        editDate.setText(characterList.get(0).getStartDate());
        bitmap=characterList.get(0).getImageres();
        cosimage.setImageBitmap(bitmap);


        CostumeSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String nametext = CostumeText.getText().toString();
                String fandomtext = Fandom.getText().toString();
                String datatext = editDate.getText().toString();

                CosMakerModel character = new CosMakerModel();
                character.setCharacterName(nametext);
                character.setFandom(fandomtext);
                character.setStartDate(datatext);
                character.setImageres(bitmap); //передаем изображение в recyclerview
                db.updateCharacter(charid,character);
                db.close();
                dismiss();
            }

        });

        newImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    String str = selectedImage.toString();

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        cosimage.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
    }




    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }

    }

    public TextWatcher textWatcher() {
        return new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CostumeText.getText().length() == 0 || Fandom.getText().length() == 0 || editDate.getText().length() == 0) {
                    CostumeSaveButton.setEnabled(false);
                    CostumeSaveButton.setTextColor(Color.GRAY);
                } else if (CostumeText.getText().length() != 0 && Fandom.getText().length() != 0 && editDate.getText().length() != 0) {
                    CostumeSaveButton.setEnabled(true);
                    CostumeSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }


            }
        };
    }





}
