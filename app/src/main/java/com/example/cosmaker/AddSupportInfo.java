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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosmaker.Adapter.DetailsAdapter;
import com.example.cosmaker.Model.CosMakerModel;
import com.example.cosmaker.Model.DetailsModel;
import com.example.cosmaker.Model.SupportModel;
import com.example.cosmaker.Utils.DataBaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddSupportInfo extends BottomSheetDialogFragment {

    public static final String TAG7= "ActionBottomDialog";
    private EditText enddate,budge;
    private ImageView finalimage;
    private Button save;

    private  CalendarView calender;
     private List<SupportModel> infoList;
    public DataBaseHandler db;
    private FloatingActionButton fab;
    private static final int GALLERY_REQUEST = 1;
    private Bitmap bitmap;
    private int charactersID;
    public static AddSupportInfo newInstance(){
        return new AddSupportInfo();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_supportinfo, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        charactersID = getArguments().getInt("idfoSupport",0);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated (View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        infoList = new ArrayList<>();

        enddate=getView().findViewById(R.id.newEndDate);
        budge=getView().findViewById(R.id.newBudget);
        finalimage = getView().findViewById(R.id.imageFinal);
        fab=getView().findViewById(R.id.newFinalImgfab);
        save = getView().findViewById(R.id.SaveButton);

        calender = getView().findViewById(R.id.scalendarView);
        calender.setVisibility(View.INVISIBLE);

        enddate.setOnTouchListener(new View.OnTouchListener() {
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

                enddate.setText(" " + dayOfMonth +" . " + (month+1) + " . " + year);
                calender.setVisibility(View.INVISIBLE);
            }
        });

        save.setEnabled(false);

        db =new DataBaseHandler(getActivity());
        db.openDatabase();

        enddate.addTextChangedListener(textWatcher());
        budge.addTextChangedListener(textWatcher());


        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

            }

        });


        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String datetext = enddate.getText().toString();
                String budgettext = budge.getText().toString();
                SupportModel sup = new SupportModel();
                sup.setEddate(datetext);
                sup.setBudget(budgettext);
                sup.setSupportcharid(charactersID);
                sup.setFinalimage(bitmap);
                db.insertSupportInfo(sup,charactersID);
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
                        finalimage.setImageBitmap(bitmap);

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
                if (enddate.getText().length() == 0 || budge.getText().length() == 0) {
                    save.setEnabled(false);
                    save.setTextColor(Color.GRAY);
                } else if (enddate.getText().length() != 0 && budge.getText().length() != 0) {
                    save.setEnabled(true);
                    save.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }


            }
        };
    }
}
