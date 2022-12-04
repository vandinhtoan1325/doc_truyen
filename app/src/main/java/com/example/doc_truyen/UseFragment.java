package com.example.doc_truyen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doc_truyen.adapter.ListComicNewAdapter;
import com.example.doc_truyen.model.Comic;
import com.example.doc_truyen.model.ReadHistory;
import com.example.doc_truyen.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UseFragment extends Fragment implements ListComicNewAdapter.onClickTruyen {
    TextView txttk,dsdoc;
    RecyclerView rycview;
    private FirebaseAuth mAuth;
    List<Comic> lstComic = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_use, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txttk = view.findViewById(R.id.txttk);
        dsdoc = view.findViewById(R.id.dsdoc);
        mAuth = FirebaseAuth.getInstance();
        rycview = view.findViewById(R.id.historyRead);
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                txttk.setText(users.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("ReadHistory").child(user.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstComic.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    String a = item.getValue(String.class);
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("listComic");
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot item : snapshot.getChildren()) {
                                Comic comic = item.getValue(Comic.class);
                                if (comic.getTrangtruyen().equals(a))
                                    lstComic.add(comic);
                            }
                            if (lstComic!=null){
                                dsdoc.setText(lstComic.size()+"");
                                ListComicNewAdapter comicAdapterTruyenMoi = new ListComicNewAdapter((ArrayList<Comic>) lstComic, requireActivity(), UseFragment.this::clickTruyen);
                                rycview.setAdapter(comicAdapterTruyenMoi);
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }


    @Override
    public void clickTruyen(Comic comic) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_object_comic", comic);
        Intent intent = new Intent(getActivity(), ReadActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}