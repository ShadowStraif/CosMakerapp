package com.example.cosmaker.Adapter;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosmaker.EditCostume;
import com.example.cosmaker.MainActivity;
import com.example.cosmaker.Model.CosMakerModel;
import com.example.cosmaker.R;
import com.example.cosmaker.Utils.DataBaseHandler;


import java.util.ArrayList;
import java.util.List;

public class CosMakerAdapter extends RecyclerView.Adapter<CosMakerAdapter.ViewHolder> {

    private List<CosMakerModel> charactersList= new ArrayList<>();
    private MainActivity activity;
    private DataBaseHandler db;
    private OnCharacterClickListener onCharacterClickListener;
    private OnCharacterLongClickListener onCharacterLongClickListener;
    public int getItemCount(){
        return charactersList.size();
    }

    public CosMakerAdapter(DataBaseHandler db,MainActivity activity,OnCharacterClickListener onCharacterClickListener,OnCharacterLongClickListener onCharacterLongClickListener)
    {
        this.db=db;
        this.activity=activity;
        this.onCharacterClickListener = onCharacterClickListener;
        this.onCharacterLongClickListener = onCharacterLongClickListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_cos_layout, parent,false);
        return new ViewHolder(itemView);

    }
    public void setCharacter(List<CosMakerModel> charactersList){
        this.charactersList=charactersList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return activity;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        ImageView characterimg;
        TextView charName,charFandom,finish;
        RelativeLayout relview;
        ViewHolder(View view){
            super(view);
            characterimg=view.findViewById(R.id.cosImage);
            charName=view.findViewById(R.id.characterName);
            charFandom=view.findViewById(R.id.characterFandom);
            relview = view.findViewById(R.id.relView);
            finish = view.findViewById(R.id.finishStatus);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public  void onClick(View v) {

                     CosMakerModel character = charactersList.get(getLayoutPosition());
                    onCharacterClickListener.onCharacterClick(character);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public   boolean onLongClick(View v) {
                    CosMakerModel character = charactersList.get(getLayoutPosition());
                    onCharacterLongClickListener.onCharacterLongClick(character);
                    return true;
                }
            });
        }
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        final CosMakerModel item = charactersList.get(position);

        holder.charName.setText(item.getCharacterName());
        holder.charFandom.setText(item.getFandom());
        holder.characterimg.setImageBitmap(item.getImageres());

        if(db.isFinished(item.getId()))
        {

            holder.finish.setText("Закончен");
        }
        else  holder.finish.setText(" ");

    }
    public void deleteItem(int position) {
        CosMakerModel item = charactersList.get(position);
        db.deleteCharacter(item.getId());
        charactersList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){

            CosMakerModel item = charactersList.get(position);
        if(!db.isFinished(item.getId())) {
            Bundle bundle = new Bundle();
            bundle.putInt("ideditcharacter", item.getId());
            EditCostume fragment = new EditCostume();
            fragment.setArguments(bundle);
            fragment.show(activity.getSupportFragmentManager(), EditCostume.TAG3);
        }
    }
    public interface OnCharacterClickListener {
        void  onCharacterClick(CosMakerModel character);
    }
    public interface OnCharacterLongClickListener {
        void  onCharacterLongClick(CosMakerModel character);
    }
    }
