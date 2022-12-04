package com.example.doc_truyen.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doc_truyen.R;
import com.example.doc_truyen.model.Comic;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListComicAdapter extends RecyclerView.Adapter<ListComicAdapter.MyViewHolder> {

    private ArrayList<Comic> listComic;
    private Context context;
    private onClickTruyen onClickTruyen;

    public ListComicAdapter(ArrayList<Comic> movieList, Context context, onClickTruyen onClickTruyen) {
        this.listComic = movieList;
        this.context = context;
        this.onClickTruyen = onClickTruyen;
    }

    public void updateData(ArrayList<Comic> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new diffCallBack(this.listComic, newList));
        listComic.clear();
        listComic.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
    public void filterList(ArrayList<Comic> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        listComic = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_see_more, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Comic comic = listComic.get(position);
        holder.name.setText(comic.getTentruyen());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReference().child(comic.getTrangtruyen());
        listRef.listAll().addOnSuccessListener(listResult -> {
//            if (listResult.getItems().size()>0){
                listRef.child("intro.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context.getApplicationContext()).load(uri).into(holder.thumbnail);
                        Log.d("Intro", "onSuccess: "+uri);
                    }
                });
//            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickTruyen.clickTruyen(comic);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listComic.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.titleItem);
            thumbnail = view.findViewById(R.id.ivThumb);

        }
    }

    public interface onClickTruyen {
        void clickTruyen(Comic comic);
    }
}
