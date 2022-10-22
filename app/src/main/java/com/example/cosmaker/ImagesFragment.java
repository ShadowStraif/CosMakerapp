package com.example.cosmaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosmaker.Adapter.CosMakerAdapter;
import com.example.cosmaker.Adapter.ImagesAdapter;
import com.example.cosmaker.Adapter.InfoDetailsAdapter;
import com.example.cosmaker.Model.CosMakerModel;
import com.example.cosmaker.Model.DetailsModel;
import com.example.cosmaker.Model.ImagesModel;
import com.example.cosmaker.Utils.DataBaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImagesFragment extends Fragment {
    private ImageView image;
    private RecyclerView imagesRecyclerView;
    private ImagesAdapter imagesAdapter;
    private List<ImagesModel> imagesList;
    private DataBaseHandler db;
    private int charactersID,flag;
    private FloatingActionButton fab;
    private static final int GALLERY_REQUEST = 1;
    private Bitmap bitmap;
    public ImagesFragment() {
    }

    public static ImagesFragment newInstance() {
        return new ImagesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        charactersID = getArguments().getInt("idnf",0);
        flag =  getArguments().getInt("flag",0);
        return inflater.inflate(R.layout.image_fragment, container, false);
    }
    public void onViewCreated (View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        db = new DataBaseHandler(this.getActivity());
        db.openDatabase();
        image=getView().findViewById(R.id.gallery_image);
        fab=getView().findViewById(R.id.imageadd);
        if(flag==0) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent photoPickerIntent1 = new Intent(Intent.ACTION_GET_CONTENT);
                    photoPickerIntent1.setType("image/*");
                    //  photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);*/
                    //  startActivityForResult(Intent.createChooser(photoPickerIntent, "Complete action using"), 1);
                    photoPickerIntent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(photoPickerIntent1, GALLERY_REQUEST);


                }

            });
        }else  fab.setVisibility(View.INVISIBLE);


        imagesList = new ArrayList<>();

        imagesRecyclerView = getView().findViewById(R.id.galleryResView);
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(),3));
        imagesAdapter=new ImagesAdapter(db,this,onImageClickListener,onLongImageClickListener);
        imagesRecyclerView.setAdapter(imagesAdapter);
        imagesList=db.returnrefimagebyte(charactersID );
        Collections.reverse(imagesList);
        imagesAdapter.setImage(imagesList);
        imagesAdapter.notifyDataSetChanged();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST:

                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            try {
                                ParcelFileDescriptor parcelFileDescriptor =
                                        this.getActivity().getContentResolver().openFileDescriptor(uri, "r");
                                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                                parcelFileDescriptor.close();
                                ImagesModel img = new ImagesModel();
                                img.setImageres(bitmap);
                                img.setCharacterid(charactersID);

                                db.insertImages(img,charactersID);
                                ((Costume)getActivity()).loadFragment(ImagesFragment.newInstance());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    } else if (data.getData() != null) {
                        Uri uri1 = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri1);

                            ImagesModel img = new ImagesModel();
                            img.setImageres(bitmap);
                            img.setCharacterid(charactersID);

                            db.insertImages(img,charactersID);
                            ((Costume)getActivity()).loadFragment(ImagesFragment.newInstance());

                        } catch (IOException e) {
                            Log.i("TAG", "Some exception " + e);
                        }
                        break;
                    }

            }
    }


    ImagesAdapter.OnImageClickListener onImageClickListener = new ImagesAdapter.OnImageClickListener() {
        @Override
        public void onImageClick(ImagesModel img) {
            Intent intent = new Intent(getActivity(), PickImage.class);
            intent.putExtra("imagegal",img.getId());
            startActivity(intent);
        }
    };

        ImagesAdapter.OnLongImageClickListener onLongImageClickListener = new ImagesAdapter.OnLongImageClickListener() {
            @Override
            public void onLongClick(int position) {
               if (flag==0) {
                   final int ps = position;
                   AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                   builder.setTitle("Удалить");
                   builder.setMessage("Вы уверены, что хотите удалить изображение?");
                   builder.setPositiveButton("Ок",
                           new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   imagesAdapter.deleteImage(ps);
                               }
                           });
                   builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           imagesAdapter.notifyItemChanged(ps);
                       }
                   });
                   AlertDialog dialog = builder.create();
                   dialog.show();
               }
            }
        };

}
