package com.example.cosmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cosmaker.Adapter.CosMakerAdapter;
import com.example.cosmaker.Adapter.DetailsAdapter;
import com.example.cosmaker.Adapter.InfoDetailsAdapter;
import com.example.cosmaker.Model.CosMakerModel;
import com.example.cosmaker.Model.DetailsModel;
import com.example.cosmaker.Utils.DataBaseHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Costume extends AppCompatActivity  {

    private TextView charN,charF,charDate;
    private ImageView cosimage;
    private ProgressBar pr;
    private RecyclerView detailsRecyclerView;
    private InfoDetailsAdapter detailsAdapter;
    private List<DetailsModel> detailsList;
    private DataBaseHandler db;
    private   String charNS;
    private  String charFS;
    private  String charDateS;
    private int charactersID;
    private int flag;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.costumemenu:
                    loadFragment(CostumeFragment.newInstance());
                    return true;
                case R.id.notemenu:
                    loadFragment(NoteFragment.newInstance());

                    return true;
                case R.id.piscmenu:
                    loadFragment(ImagesFragment.newInstance());

                    return true;

            }
            return false;
        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener finalOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.costumemenu2:
                    loadFragment(CostumeFragment.newInstance());
                    return true;
                case R.id.notemenu2:
                    loadFragment(NoteFragment.newInstance());
                    return true;
                case R.id.piscmenu2:
                    loadFragment(ImagesFragment.newInstance());
                    return true;
                case R.id.supportmenu:
                    loadFragment(SupportFragment.newInstance());
                    return true;

            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();

        bundle.putInt("idnf",charactersID);
        bundle.putInt("flag",flag);
        bundle.putString("charname",charNS);
        bundle.putString("charfandom",charFS);
        bundle.putString("chardate",charDateS);
        fragment.setArguments(bundle);
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costume);
        getSupportActionBar().hide();

        db = new DataBaseHandler(this);
          db.openDatabase();
        charN = (TextView) findViewById(R.id.infoCosName);
        charF=(TextView) findViewById(R.id.infoFandom);
        cosimage=(ImageView) findViewById(R.id.infoCosImage);
        charNS=getIntent().getStringExtra("name");
        charFS=getIntent().getStringExtra("fandom");
        charDateS=getIntent().getStringExtra("startdate");
        charactersID=getIntent().getIntExtra("id",0);
        charN.setText(charN.getText().toString()+" "+charNS);
        charF.setText(charF.getText().toString()+" "+charFS);
        cosimage.setImageBitmap(db.returnimagebyte(charactersID));

        if(db.isFinished(charactersID))
        {
            flag=1;
            BottomNavigationView navigation2 = (BottomNavigationView) findViewById(R.id.bottom_navigation2);
            navigation2.setVisibility(View.VISIBLE);
            navigation2.setOnNavigationItemSelectedListener(finalOnNavigationItemSelectedListener);
            if(savedInstanceState == null) {
                loadFragment(CostumeFragment.newInstance());
            }
        }
        else
        {
            flag=0;
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
            navigation.setVisibility(View.VISIBLE);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            if(savedInstanceState == null) {
                loadFragment(CostumeFragment.newInstance());
            }
        }
    }

}
