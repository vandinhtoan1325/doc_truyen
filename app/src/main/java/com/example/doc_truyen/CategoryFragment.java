package com.example.doc_truyen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doc_truyen.adapter.ListComicAdapter;
import com.example.doc_truyen.model.Comic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CategoryFragment extends Fragment implements AdapterView.OnItemSelectedListener, ListComicAdapter.onClickTruyen {
    Spinner spinner;
    RecyclerView recyclerView;
    ListComicAdapter comicAdapterBangxephang;
    ArrayList<Comic> lstComic = new ArrayList<>();
    String[] courses = {"Hành Động", "Hoạt Hình",
            "Hài Hước", "Kinh Dị",
            "Truyện Teen", "Thám Hiểm", "Kiếm Hiệp", "Hài Hước", "Anime"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = view.findViewById(R.id.coursesspinner);
        recyclerView = view.findViewById(R.id.recyclerViewCategrory);
        spinner.setOnItemSelectedListener(CategoryFragment.this);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listComic");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstComic.clear();
                for (DataSnapshot comic : snapshot.getChildren()) {
                    Comic comic1 = comic.getValue(Comic.class);
                    lstComic.add(comic1);
                }
                ;
                comicAdapterBangxephang = new ListComicAdapter(lstComic, requireContext(), CategoryFragment.this);
                recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
                recyclerView.setAdapter(comicAdapterBangxephang);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        ArrayAdapter ad = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, courses);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listComic");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstComic.clear();
                for (DataSnapshot comic : snapshot.getChildren()) {
                    Comic comic1 = comic.getValue(Comic.class);
                    if (comic1.getTheloai().equalsIgnoreCase(courses[i])) {
                        lstComic.add(comic1);
                    }
                }
                ;
                comicAdapterBangxephang = new ListComicAdapter(lstComic, requireContext(), CategoryFragment.this);
                recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
                recyclerView.setAdapter(comicAdapterBangxephang);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void clickTruyen(Comic comic) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_object_comic", comic);
        Intent intent = new Intent(requireContext(), IntroduceActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}