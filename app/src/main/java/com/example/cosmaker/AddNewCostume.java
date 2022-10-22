package com.example.cosmaker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cosmaker.Adapter.DetailsAdapter;
import com.example.cosmaker.Model.CosMakerModel;
import com.example.cosmaker.Model.DetailsModel;
import com.example.cosmaker.Model.ImagesModel;
import com.example.cosmaker.Utils.DataBaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class AddNewCostume extends BottomSheetDialogFragment {

    public static final String TAG= "ActionBottomDialog";
    private EditText newCostumeText,newFandom,newDate;
    private ImageView cosnewimage;
    private Button newCostumeSaveButton;
    private EditText newDetailName;
    private Spinner type,level;
    private TextView typeText,levelText;
    private Button newDetailSaveButton;
    private  CalendarView calender;
    private  EditText editDate ;
    private RecyclerView detailsaddRecyclerView;
    private List<DetailsModel> detailsList;
    private DetailsAdapter detailsAdapter;
    public DataBaseHandler db;
    private FloatingActionButton fab;
    private static final int GALLERY_REQUEST = 1;
   private Bitmap bitmap;
    private int it2=0;
    private int it;
    public static AddNewCostume newInstance(){
        return new AddNewCostume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_new_costume, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated (View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        detailsList = new ArrayList<>();
       detailsaddRecyclerView = getView().findViewById(R.id.detailsRecyclerView);
       detailsaddRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
       detailsAdapter = new DetailsAdapter(this.getActivity());
       detailsaddRecyclerView.setAdapter(detailsAdapter);

        typeText=getView().findViewById(R.id.typeText);
        levelText=getView().findViewById(R.id.priorityText);
        type = getView().findViewById(R.id.typeSpin);
        level=  getView().findViewById(R.id.prioritySpin);

        fab=getView().findViewById(R.id.newReffab);

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
                //String item2 =(String)parent.getItemAtPosition(position);
                it2 = Integer.parseInt(parent.getItemAtPosition(position).toString());
                levelText.setText(String.valueOf(it2));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        cosnewimage = getView().findViewById(R.id.imagenew);
        newDetailName = getView().findViewById(R.id.newDetailName);
        newDetailSaveButton = getView().findViewById(R.id.newCostumeSaveDetailButton);
        calender = getView().findViewById(R.id.calendarView);
        editDate=  getView().findViewById(R.id.newStartDate);
        calender.setVisibility(View.INVISIBLE);
        editDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    calender.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub

                editDate.setText(" " + dayOfMonth +" . " + (month+1) + " . " + year);
                calender.setVisibility(View.INVISIBLE);
            }
        });
        newCostumeText = getView().findViewById(R.id.newCosName);
        newFandom = getView().findViewById(R.id.newFandomName);
        newDate=  editDate;
        newCostumeSaveButton = getView().findViewById(R.id.newCostumeSaveButton);
        newCostumeSaveButton.setEnabled(false);
        newDetailSaveButton.setEnabled(false);




        db =new DataBaseHandler(getActivity());
        db.openDatabase();

        newCostumeText.addTextChangedListener(textWatcher());
        newFandom.addTextChangedListener(textWatcher());
        newDate.addTextChangedListener(textWatcher());
        newDetailName.addTextChangedListener(textWatcher());


        newDetailSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String detailnametxt= newDetailName.getText().toString();
                String typetext= typeText.getText().toString();

                DetailsModel detail=new DetailsModel();
                detail.setDetailName(detailnametxt);
                detail.setStatus(0);
                detail.setDetailType(typetext);
                detail.setDetailLevel(it2);
                detailsList.add(detail);
                detailsAdapter.setDetail(detailsList);
                newDetailName.setText("");
            }
        });



        fab.setOnClickListener(new View.OnClickListener(){
         @Override
        public void onClick(View v) {
             Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
             photoPickerIntent.setType("image/*");
             startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

         }

        });


        newCostumeSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String nametext = newCostumeText.getText().toString();
                String fandomtext = newFandom.getText().toString();
                String datatext = newDate.getText().toString();
                    CosMakerModel character = new CosMakerModel();
                    character.setCharacterName(nametext);
                    character.setFandom(fandomtext);
                    character.setStartDate(datatext);
                    character.setImageres(bitmap); //передаем изображение в recyclerview
                    db.insertCharacter(character);
                    int id= db.getLastInsertCharacter();
                  for(int i = 0; i < detailsList.size(); i++) {
                      detailsList.get(i).setCharid(id);
                      db.insertDetail(detailsList.get(i),id);
                    }
                ImagesModel img = new ImagesModel();
                img.setImageres(bitmap);
                img.setCharacterid(id);
                db.insertImages(img,id);
                  db.close();
                dismiss();
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
                            cosnewimage.setImageBitmap(bitmap);

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
                if (newCostumeText.getText().length()==0 || newFandom.getText().length()==0  || newDate.getText().length()==0 || bitmap==null) {
                    newCostumeSaveButton.setEnabled(false);
                    newCostumeSaveButton.setTextColor(Color.GRAY);
                } else if( newCostumeText.getText().length()!=0 && newFandom.getText().length()!=0  && newDate.getText().length()!=0 && bitmap!=null){
                    newCostumeSaveButton.setEnabled(true);
                    newCostumeSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }
                if ( newDetailName.getText().length()==0 )
                {
                    newDetailSaveButton.setEnabled(false);
                    newDetailSaveButton.setTextColor(Color.GRAY);
                }
                  else  if( newDetailName.getText().length()!=0 ){
                    newDetailSaveButton.setEnabled(true);
                    newDetailSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }

            }
        };
    }










}
