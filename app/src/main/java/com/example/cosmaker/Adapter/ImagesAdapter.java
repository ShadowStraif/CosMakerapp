package com.example.cosmaker.Adapter;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cosmaker.ImagesFragment;
import com.example.cosmaker.Model.CosMakerModel;
import com.example.cosmaker.Model.ImagesModel;

import com.example.cosmaker.Model.NoteModel;
import com.example.cosmaker.NoteFragment;
import com.example.cosmaker.R;
import com.example.cosmaker.Utils.DataBaseHandler;

import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private List<ImagesModel> imagesList= new ArrayList<>();
    private ImagesFragment activity;
    private DataBaseHandler db;
  public OnImageClickListener onImageClickListener;
    public OnLongImageClickListener onLongImageClickListener;
    public int getItemCount(){
        return imagesList.size();
    }

    public ImagesAdapter(DataBaseHandler db, ImagesFragment activity,OnImageClickListener onImageClickListener,OnLongImageClickListener onLongImageClickListener)
    {
        this.db=db;
        this.activity=activity;
        this.onImageClickListener = onImageClickListener;
        this.onLongImageClickListener = onLongImageClickListener;

    }

    public ImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View imageview= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.images_layout, parent,false);
        return new ImagesAdapter.ViewHolder(imageview);

    }

    public void setImage(List<ImagesModel> imageList){
        this.imagesList=imageList;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(ImagesAdapter.ViewHolder holder, int position){
        db.openDatabase();
        final ImagesModel item = imagesList.get(position);
        holder.image.setImageBitmap(item.getImageres());

       }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.gallery_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public  void onClick(View v) {

                    ImagesModel img = imagesList.get(getLayoutPosition());
                    onImageClickListener.onImageClick(img);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public   boolean onLongClick(View v) {
                    final int position = getAdapterPosition();

                    onLongImageClickListener.onLongClick(position);
                    return true;
                }
            });


    }
    }

    public void deleteImage(int position) {
        ImagesModel item = imagesList.get(position);
        db.deleteImage(item.getId());
        imagesList.remove(position);
        notifyItemRemoved(position);
    }
    public interface OnImageClickListener {
        void  onImageClick(ImagesModel img);
    }
    public interface OnLongImageClickListener {
        void  onLongClick(int position);
    }
}













