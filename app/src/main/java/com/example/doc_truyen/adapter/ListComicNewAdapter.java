package com.example.doc_truyen.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doc_truyen.MainActivity;
import com.example.doc_truyen.R;
import com.example.doc_truyen.model.Comic;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListComicNewAdapter extends RecyclerView.Adapter<ListComicNewAdapter.MyViewHolder> {

    private ArrayList<Comic> listComic;
    private Context context;
    private onClickTruyen onClickTruyen;

    public ListComicNewAdapter(ArrayList<Comic> movieList, Context context, onClickTruyen onClickTruyen) {
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

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_new_comic, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Comic comic = listComic.get(position);
        holder.name.setText(comic.getTentruyen());
        holder.tacgia.setText("Tác giả: "+comic.getTacgia());
        holder.theloai.setText("Thể loại: "+comic.getTheloai());
        holder.ratingBar.setRating((float) comic.getVote());
        Log.e("TAG", "onBindViewHolder: "+comic.getVote() );
        Log.e("TAG", "onBindViewHolder: "+comic.toString() );
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReference().child(comic.getTrangtruyen());
        listRef.listAll().addOnSuccessListener(listResult -> {
            if (listResult.getItems().size()>0){
                listRef.child("intro.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri).into(holder.thumbnail);
                    }
                });
            }
        });
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
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
        public TextView theloai;
        public TextView tacgia;
        public RatingBar ratingBar;
        public ImageView thumbnail;
        public ConstraintLayout constraintLayout;
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tenTruyenNew);
            thumbnail = view.findViewById(R.id.ivThumbNew);
            theloai = view.findViewById(R.id.tvTheLoaiNew);
            tacgia = view.findViewById(R.id.tvTacGiaNew);
            ratingBar = view.findViewById(R.id.rbRatingNew);
            constraintLayout = view.findViewById(R.id.clMainItemNew);
        }
    }

    public interface onClickTruyen {
        void clickTruyen(Comic comic);
    }
}