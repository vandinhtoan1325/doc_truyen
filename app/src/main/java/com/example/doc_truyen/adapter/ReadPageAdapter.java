package com.example.doc_truyen.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doc_truyen.R;
import com.example.doc_truyen.model.Comic;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ReadPageAdapter extends RecyclerView.Adapter<ReadPageAdapter.MyViewHolder> {

    private ArrayList<String> listComic;
    private Context context;
    private Comic comic;
    private String chapter;

    public ReadPageAdapter(ArrayList<String> movieList, Context context, Comic comic, String chapter) {
        this.listComic = movieList;
        this.context = context;
        this.comic = comic;
        this.chapter = chapter;
    }

    public void updateData(ArrayList<String> movieList, String chapter){
        this.listComic = movieList;
        this.chapter = chapter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_page, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReference().child(comic.getTrangtruyen() + "/" + chapter + "/" + listComic.get(position));
        listRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context.getApplicationContext()).load(uri).into(holder.thumbnail);
                Log.d("TAG", "onSuccess: "+uri);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listComic.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.ivPage);
        }
    }
}
