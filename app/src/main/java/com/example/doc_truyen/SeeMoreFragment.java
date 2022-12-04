package com.example.doc_truyen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doc_truyen.adapter.ListComicAdapter;
import com.example.doc_truyen.model.Comic;
import com.example.doc_truyen.viewmodel.MainViewModel;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SeeMoreFragment extends Fragment implements ListComicAdapter.onClickTruyen {
    private RecyclerView recyclerView;
    private ListComicAdapter mAdapter;
    private MainViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        View view = inflater.inflate(R.layout.fragment_seemore, container, false);
        recyclerView = view.findViewById(R.id.rclListComic);
        mAdapter = new ListComicAdapter(new ArrayList<>(), requireContext(), this::clickTruyen);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getData();
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.listComicData.observe(requireActivity(), new Observer<ArrayList<Comic>>() {
            @Override
            public void onChanged(ArrayList<Comic> comics) {
                mAdapter.updateData(comics);
            }
        });
    }


    @Override
    public void clickTruyen(Comic comic) {
        Log.e("TAG", "clickTruyen: ");
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_object_comic", comic);
        Intent intent = new Intent(requireContext(), IntroduceActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}