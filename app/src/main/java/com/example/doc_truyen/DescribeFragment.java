package com.example.doc_truyen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class DescribeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TextView tvMoTa;
    private String id;
    private DatabaseReference mDatabase;

    public DescribeFragment() {
        // Required empty public constructor
    }

    public static DescribeFragment newInstance(String param1, String param2) {
        DescribeFragment fragment = new DescribeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_describe, container, false);
        tvMoTa = view.findViewById(R.id.tvMoTa);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        id = getArguments().getString("id_comic", "");
        showMota();
        return view;
    }

    private void showMota(){
        id = getArguments().getString("id_comic", "");
        System.out.println("ID: " + id);
        mDatabase.child("listComic").child(id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String MOTA = dataSnapshot.child("mota").getValue().toString();
                        tvMoTa.setText(MOTA);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

}