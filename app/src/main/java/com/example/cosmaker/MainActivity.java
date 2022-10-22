package com.example.cosmaker;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cosmaker.Adapter.CosMakerAdapter;
import com.example.cosmaker.Adapter.DetailsAdapter;
import com.example.cosmaker.Model.CosMakerModel;
import com.example.cosmaker.Model.DetailsModel;
import com.example.cosmaker.Utils.DataBaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements DialogCloseListener/* implements FragmentConnection*/ {

    private RecyclerView costumesRecyclerView;
    private CosMakerAdapter charactersAdapter;

    private List<CosMakerModel> charactersList;

    private DataBaseHandler db;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        db = new DataBaseHandler(this);
        db.openDatabase();


        charactersList = new ArrayList<>();
        costumesRecyclerView = findViewById(R.id.costumesRecyclerView);
        costumesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        charactersAdapter=new CosMakerAdapter(db,this,onCharacterClickListener,onCharacterLongClickListener);
        costumesRecyclerView.setAdapter(charactersAdapter);
        fab= findViewById(R.id.fab);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelperCharacter(charactersAdapter));
        itemTouchHelper.attachToRecyclerView(costumesRecyclerView);
        charactersList=db.getAllCharacters();
        Collections.reverse(charactersList);
        charactersAdapter.setCharacter(charactersList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewCostume.newInstance().show(getSupportFragmentManager(),AddNewCostume.TAG);
            }
        });


    }

    @Override
    public void handleDialogClose(DialogInterface dialog){

        charactersList=db.getAllCharacters();
        Collections.reverse(charactersList);
        charactersAdapter.setCharacter(charactersList);
        charactersAdapter.notifyDataSetChanged();


    }

    CosMakerAdapter.OnCharacterClickListener onCharacterClickListener = new CosMakerAdapter.OnCharacterClickListener() {
        @Override
        public void onCharacterClick(CosMakerModel character) {
            Intent intent = new Intent(MainActivity.this, Costume.class);
            intent.putExtra("name",character.getCharacterName());
            intent.putExtra("fandom",character.getFandom());
            intent.putExtra("startdate",character.getStartDate());
            intent.putExtra("id",character.getId());
            startActivity(intent);

        }
    };

    CosMakerAdapter.OnCharacterLongClickListener onCharacterLongClickListener = new CosMakerAdapter.OnCharacterLongClickListener(){
        @Override
        public void onCharacterLongClick(final CosMakerModel character) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Закончен");
            builder.setMessage("Вы хотите завершить работу над костюмом?");
            builder.setPositiveButton("Ок",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("idfoSupport", character.getId());
                           AddSupportInfo add = new AddSupportInfo();
                           add.setArguments(bundle);
                            add.show(getSupportFragmentManager(),AddSupportInfo.TAG7);

                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    };



}
