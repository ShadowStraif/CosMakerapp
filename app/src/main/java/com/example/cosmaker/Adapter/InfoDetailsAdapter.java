package com.example.cosmaker.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosmaker.Costume;
import com.example.cosmaker.CostumeFragment;
import com.example.cosmaker.EditDetail;
import com.example.cosmaker.MainActivity;
import com.example.cosmaker.Model.DetailsModel;
import com.example.cosmaker.NoteFragment;
import com.example.cosmaker.R;
import com.example.cosmaker.Utils.DataBaseHandler;

import java.util.ArrayList;
import java.util.List;

public class InfoDetailsAdapter extends RecyclerView.Adapter<InfoDetailsAdapter.ViewHolder> {

    private List<DetailsModel> detailsList= new ArrayList<>();
    private DataBaseHandler db;

    private CostumeFragment activity;

    public int getinfoDetailsCount(){
        return detailsList.size();
    }


    public InfoDetailsAdapter (DataBaseHandler db, CostumeFragment activity)
    {
        this.db=db;
        this.activity=activity;

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View detailView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.details_layout, parent,false);
        return new InfoDetailsAdapter.ViewHolder(detailView);

    }

    public void setinfoDetail(List<DetailsModel> detailsList){
        this.detailsList=detailsList;
        notifyDataSetChanged();
    }


    private boolean toBoolean(int n)
    {
        return n!=0;
    }
    public int getItemCount(){
        return detailsList.size();
    }
    public Context getContext() {
        return activity.getActivity();
    }

    public void onBindViewHolder(InfoDetailsAdapter.ViewHolder holder, int position){
         db.openDatabase();


        final DetailsModel item = detailsList.get(position);
        holder.detailName.setText(item.getDetailName());
        holder.detailType.setText(item.getDetailType());
        holder.detaillevel.setText(String.valueOf(item.getDetailLevel()));
        holder.status.setText("");
        holder.status.setChecked(toBoolean(item.getStatus()));
        holder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    db.updateStatus(item.getId(), 1);
                    ((Costume) activity.getActivity()).loadFragment(CostumeFragment.newInstance());
                }
                else{
                    db.updateStatus(item.getId(),0);
                    ((Costume) activity.getActivity()).loadFragment(CostumeFragment.newInstance());
                }

            }
        });
        if(db.isFinished(item.getCharid())) {
            holder.status.setVisibility(View.INVISIBLE);
            holder.status.setChecked(toBoolean(1));
            db.updateStatus(item.getId(), 1);
        }


    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox status;
        TextView detailName,detailType,detaillevel;
        ViewHolder(View view){
            super(view);
            status=view.findViewById(R.id.detailCheckBox);
            detailName=view.findViewById(R.id.layoutDetailName);
            detailType=view.findViewById(R.id.layoutDetailType);
            detaillevel=view.findViewById(R.id.layoutDetailLevel);

        }

    }

    public void deleteItem(int position) {
        DetailsModel item = detailsList.get(position);
        db.deleteDetail(item.getId());
        detailsList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        DetailsModel item = detailsList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("editiddetail", item.getId());
        bundle.putString("editnamedetail", item.getDetailName());
        bundle.putString("edittypedetail", item.getDetailType());
        bundle.putInt("editleveldetail", item.getDetailLevel());
        EditDetail fragment = new EditDetail();
        fragment.setArguments(bundle);
        fragment.show(activity.getActivity().getSupportFragmentManager(), EditDetail.TAG2);
    }


}
