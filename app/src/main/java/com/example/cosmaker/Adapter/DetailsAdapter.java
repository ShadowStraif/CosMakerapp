package com.example.cosmaker.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cosmaker.R;
import com.example.cosmaker.Model.DetailsModel;
import com.example.cosmaker.Utils.DataBaseHandler;
import java.util.ArrayList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder>  {

    private List<DetailsModel> detailsList= new ArrayList<>();
    private DataBaseHandler db;

private FragmentActivity activity;
    public int getDetailsCount(){
        return detailsList.size();
    }


    public DetailsAdapter(FragmentActivity activity)
    {
            this.activity=activity;
     }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View detitemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_detail_layout, parent,false);
        return new ViewHolder(detitemView);

    }

    public void setDetail(List<DetailsModel> detailsList){
        this.detailsList=detailsList;
          notifyDataSetChanged();
    }
    public DetailsModel getDetail(int position){
       return  detailsList.get(position);
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{

        TextView detailName,detailType;
        ViewHolder(View view){
            super(view);
            detailName=view.findViewById(R.id.layoutaddDetailName);
            detailType=view.findViewById(R.id.layoutaddDetailType);

        }
    }

    private boolean toBoolean(int n)
    {
        return n!=0;
    }
    public int getItemCount(){
        return detailsList.size();
    }


    public void onBindViewHolder(ViewHolder holder, int position){

        DetailsModel item = detailsList.get(position);
        holder.detailName.setText(item.getDetailName());
        holder.detailType.setText(item.getDetailType());
    }







}
