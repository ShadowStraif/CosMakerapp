package com.example.cosmaker;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cosmaker.Adapter.InfoDetailsAdapter;
import com.example.cosmaker.Model.DetailsModel;
import com.example.cosmaker.Utils.DataBaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CostumeFragment extends Fragment implements DialogCloseNote {

    private TextView charDate,quote,progresstxt;
    private ProgressBar pr;
    private RecyclerView detailsRecyclerView;
    private InfoDetailsAdapter detailsAdapter;
    private List<DetailsModel> detailsList;
    private DataBaseHandler db;
    private  String charDateS;
    private int charactersID,flag;
    private FloatingActionButton fab;
    public CostumeFragment() {
    }

    public static CostumeFragment newInstance() {
        return new CostumeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        charactersID = getArguments().getInt("idnf",0);
        charDateS = getArguments().getString("chardate");
        flag =  getArguments().getInt("flag",0);

        return inflater.inflate(R.layout.costume_fragment, container, false);
    }

    public void onViewCreated (View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        db = new DataBaseHandler(this.getActivity());
        db.openDatabase();
        progresstxt=getView().findViewById(R.id.progress_text);
         quote = getView().findViewById(R.id.infoQuotes);
        charDate=getView().findViewById(R.id.infoStartDate);
        pr=getView().findViewById(R.id.progress);
        charDate.setText(charDate.getText().toString()+" "+charDateS);


        detailsList = new ArrayList<>();



        String quote1 = db.randomQuotes();
        quote.setText(quote1);

        detailsRecyclerView = getView().findViewById(R.id.infoDetails);
        detailsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        detailsAdapter=new InfoDetailsAdapter(db,this);
        detailsRecyclerView.setAdapter(detailsAdapter);



        detailsList=db.getAllDetailsbyID(charactersID );
        Collections.reverse(detailsList);
        detailsAdapter.setinfoDetail(detailsList);
        detailsAdapter.notifyDataSetChanged();
        progresstxt.setText("0%");
        if (detailsList.size()!=0) {
        double progressstatusfloat = Math.ceil(db.progresscounting(charactersID));
        int progressstatus = (int) (progressstatusfloat);
        pr.setProgress(progressstatus);
        String strProgress = String.valueOf(progressstatus) + " %";
        progresstxt.setText(strProgress);
        int round = 1;
        for (int i = 0; i < detailsList.size(); i++) {
            if (detailsList.get(i).getStatus() == 1) {
                round++;
            }
        }
        if (round == detailsList.size() + 1) {
            pr.setProgress(100);
            String str1Progress = String.valueOf(100) + " %";
            progresstxt.setText(str1Progress);
        }
    }
        fab = getView().findViewById(R.id.fabCostumeF);
        if(flag==0) {
            ItemTouchHelper itemTouchHelper = new
                    ItemTouchHelper(new RecyclerItemTouchHelperDetail(detailsAdapter));
            itemTouchHelper.attachToRecyclerView(detailsRecyclerView);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditDetail.newInstance(charactersID).show(getFragmentManager(), EditDetail.TAG2);
                }


            });
        }
        else fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void handleDialogCloseNote(DialogInterface dialog1){
        detailsList=db.getAllDetailsbyID(charactersID );
        Collections.reverse(detailsList);
        detailsAdapter.setinfoDetail(detailsList);
        detailsAdapter.notifyDataSetChanged();
    }



}
