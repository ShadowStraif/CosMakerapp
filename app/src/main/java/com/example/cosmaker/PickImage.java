package com.example.cosmaker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cosmaker.Model.ImagesModel;
import com.example.cosmaker.Utils.DataBaseHandler;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PickImage extends AppCompatActivity {
    private ImageView mImageView;
    private DataBaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DataBaseHandler(this);
        db.openDatabase();
        setContentView(R.layout.image_detail_layout);
        getSupportActionBar().hide();
        mImageView = (ImageView) findViewById(R.id.image1);
        int id = getIntent().getIntExtra("imagegal",0);
        mImageView.setImageBitmap(db.returrefnimagebyte(id));
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(mImageView);
        pAttacher.update();

    }



}
