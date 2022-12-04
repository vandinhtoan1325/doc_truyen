package com.example.doc_truyen;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doc_truyen.adapter.ReadPageAdapter;
import com.example.doc_truyen.model.Comic;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ReadActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private TextView tvTrangtruoc;
    private TextView tvTrangsau;
    private TextView tvTap;
    private RecyclerView recyclerView;
    private Comic comic;
    FirebaseStorage storage;
    StorageReference listRef;
    Spinner spin;

    private ReadPageAdapter readPageAdapter;
    private ArrayList<String> listPage = new ArrayList<>();
    private ArrayList<String> listChapter = new ArrayList<>();
    private Integer chapterIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRead);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTrangtruoc = findViewById(R.id.tvTrangtruoc);
        tvTrangsau = findViewById(R.id.tvTrangsau);
        tvTap = findViewById(R.id.tvtap);
        recyclerView = findViewById(R.id.rclListPage);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            comic = (Comic) extras.getSerializable("key_object_comic");
            getSupportActionBar().setTitle(comic.getTentruyen());
            storage = FirebaseStorage.getInstance();
            listRef = storage.getReference().child(comic.getTrangtruyen());
            Log.e("TAG", "onCreate: " + comic.getTrangtruyen());
            listRef.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getPrefixes()) {
                    listChapter.add(item.getName());
                }
                setSpiner();
                listRef.child(listChapter.get(chapterIndex)).listAll().addOnSuccessListener(listResult1 -> {
                    for (StorageReference item : listResult1.getItems()) {
                        listPage.add(item.getName());
                    }
                    readPageAdapter = new ReadPageAdapter(listPage, this, comic, listChapter.get(chapterIndex));
                    recyclerView.setAdapter(readPageAdapter);
                });
            });

            nextChapter();

        }

        tvTrangtruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preChapter();
            }
        });
        tvTrangsau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextChapter();
            }
        });
    }

    private void preChapter() {
        if (chapterIndex > 0) {
            chapterIndex--;
            spin.setSelection(chapterIndex);
            listPage.clear();
            listRef.child(listChapter.get(chapterIndex)).listAll().addOnSuccessListener(listResult1 -> {
                for (StorageReference item : listResult1.getItems()) {
                    listPage.add(item.getName());
                }
                readPageAdapter.updateData(listPage, listChapter.get(chapterIndex));
            });
        }
    }

    private void nextChapter() {
        if (chapterIndex < listChapter.size() - 1) {
            chapterIndex++;
            spin.setSelection(chapterIndex);
            listPage.clear();
            listRef.child(listChapter.get(chapterIndex)).listAll().addOnSuccessListener(listResult1 -> {
                for (StorageReference item : listResult1.getItems()) {
                    listPage.add(item.getName());
                }
                readPageAdapter.updateData(listPage, listChapter.get(chapterIndex));
            });
        }
    }


    private void setSpiner() {
        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        ArrayList<String> listSpinner = new ArrayList<>();
        for (int index = 0; index < listChapter.size() ; index++){
            listSpinner.add("Tập: "+(index+1));
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listSpinner);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        chapterIndex = i;
        listPage.clear();
        listRef.child(listChapter.get(chapterIndex)).listAll().addOnSuccessListener(listResult1 -> {
            for (StorageReference item : listResult1.getItems()) {
                listPage.add(item.getName());
            }

            try {
                readPageAdapter.updateData(listPage, listChapter.get(chapterIndex));
            } catch (NullPointerException e){
                System.out.println("Lỗi: " + e);
            }
            readPageAdapter.updateData(listPage, listChapter.get(chapterIndex));
            readPageAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}