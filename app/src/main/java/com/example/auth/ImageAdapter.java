package com.example.auth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.view.PointerIconCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import android.database.Cursor;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<ImagesUpload> mUpload;


    public ImageAdapter(Context context, List<ImagesUpload> upload)
    {
        mContext = context;
        mUpload = upload;
    }




    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        ImagesUpload uploadCurrent = mUpload.get(position);
        holder.text_view_name.setText(uploadCurrent.getmName());
        Picasso.with(mContext).load(uploadCurrent.getmImageURL()).fit().centerCrop().into(holder.image_view_upload);

    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView text_view_name;
        public ImageView image_view_upload;


        public ImageViewHolder(View itemView)
        {
            super(itemView);

            text_view_name = itemView.findViewById(R.id.text_view_name);
            image_view_upload = itemView.findViewById(R.id.image_view_upload);

        }



    }




}
