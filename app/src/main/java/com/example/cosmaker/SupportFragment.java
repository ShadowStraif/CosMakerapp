package com.example.cosmaker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.cosmaker.Adapter.ImagesAdapter;
import com.example.cosmaker.Model.ImagesModel;
import com.example.cosmaker.Model.SupportModel;
import com.example.cosmaker.Utils.DataBaseHandler;
import java.util.ArrayList;
import java.util.List;

public class SupportFragment extends Fragment {
    private TextView startdate,enddate,budget;
    private List<SupportModel> infoList;
    private DataBaseHandler db;
    private int charactersID;
    private  String charDateS;
    private ImageView img;
    public SupportFragment() {
    }

    public static SupportFragment newInstance() {
        return new SupportFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        charactersID = getArguments().getInt("idnf",0);
        charDateS = getArguments().getString("chardate");

        return inflater.inflate(R.layout.support_fragment, container, false);
    }


    public void onViewCreated (View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        db = new DataBaseHandler(this.getActivity());
        db.openDatabase();
        startdate=getView().findViewById(R.id.supportStartDate);
        enddate = getView().findViewById(R.id.supportEndDate);
        budget = getView().findViewById(R.id.supportBudget);
        img= getView().findViewById(R.id.finalCosImage);
        infoList = new ArrayList<>();
        infoList=db.getSupInformation(charactersID );
        startdate.setText(startdate.getText().toString()+charDateS);
        enddate.setText(enddate.getText().toString() + infoList.get(0).getEddate());
        budget.setText(budget.getText().toString() + infoList.get(0).getBudget());
        img.setImageBitmap(infoList.get(0).getFinalimage());

    }


}
