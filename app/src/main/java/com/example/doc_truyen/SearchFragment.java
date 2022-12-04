package com.example.doc_truyen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doc_truyen.adapter.ListComicAdapter;
import com.example.doc_truyen.adapter.ListComicNewAdapter;
import com.example.doc_truyen.model.Comic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements ListComicAdapter.onClickTruyen {
    SearchView searchView;
    RecyclerView rcyclerview;
    ListComicAdapter comicAdapterBangxephang;
    ArrayList<Comic> lstComic = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listComic");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for ( DataSnapshot comic: snapshot.getChildren()) {
                    Comic  comic1 = comic.getValue(Comic.class);
                     lstComic.add(comic1);
                };
                comicAdapterBangxephang = new ListComicAdapter(lstComic, requireContext(), SearchFragment.this);
                rcyclerview.setLayoutManager(new GridLayoutManager(requireContext(),2));
                rcyclerview.setAdapter(comicAdapterBangxephang);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcyclerview = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.svSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    @Override
    public void clickTruyen(Comic comic) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_object_comic", comic);
        Intent intent = new Intent(requireContext(), IntroduceActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void filter(String text) {
        ArrayList<Comic> filteredlist = new ArrayList<>();
        for (Comic item : lstComic) {
            if (item.getTentruyen().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }


        if (filteredlist.isEmpty()) {
        } else {
            comicAdapterBangxephang.filterList(filteredlist);
        }
    }
}