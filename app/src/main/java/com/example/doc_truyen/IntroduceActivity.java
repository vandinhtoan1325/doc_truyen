package com.example.doc_truyen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.doc_truyen.model.Comic;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class IntroduceActivity extends AppCompatActivity {

    private TextView tvTen;
    private TextView tvTacGia;
    private TextView tvTheLoai;
    private TextView tvView;
    private TextView rbNumber;
    private ImageView ivThumb;
    private RatingBar ratingBar;
    private Button btnDoctruyen;
    private Button btn_binh_luan;
    private Button btn_mo_ta;
    private Comic comic;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarIntro);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorF8F5F1));
        user = FirebaseAuth.getInstance().getCurrentUser();
        tvTen = findViewById(R.id.tvTenTruyen);
        tvTacGia = findViewById(R.id.tvTacGia);
        tvTheLoai = findViewById(R.id.tvTheLoai);
        tvView = findViewById(R.id.tvView);
        ivThumb = findViewById(R.id.ivThumbDetail);
        ratingBar = findViewById(R.id.rbRating);
        btnDoctruyen = findViewById(R.id.btnRead);
        btn_binh_luan = findViewById(R.id.btnbinhluan);
        btn_mo_ta = findViewById(R.id.btn_mo_ta);
        rbNumber = findViewById(R.id.rbNumber);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            comic = (Comic) extras.getSerializable("key_object_comic");
            tvTen.setText(comic.getTentruyen());
            tvTacGia.setText("Tác Giả: " + comic.getTacgia());
            tvTheLoai.setText("Thể Loại: " + comic.getTheloai());
            tvView.setText("Lượt Xem: " + comic.getView());

            ratingBar.setRating(2);
            //load thumb
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference listRef = storage.getReference().child(comic.getTrangtruyen());
            listRef.listAll().addOnSuccessListener(listResult -> {
                listRef.child(listResult.getItems().get(0).getName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(IntroduceActivity.this).load(uri).into(ivThumb);
                    }
                });
            });

            //update view
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("listComic");
            databaseReference.child(String.valueOf(comic.getId())).child("view").setValue(comic.getView() + 1);


        }
        RatiingbarTruyen(comic);
        btnDoctruyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("key_object_comic", comic);
                Intent intent = new Intent(IntroduceActivity.this, ReadActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                saveHistory(comic.getTrangtruyen());
            }
        });
        btn_binh_luan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentFragment commentFragment = new CommentFragment();

                replaceFragment(commentFragment,"id_comic", String.valueOf(comic.getId()));
            }
        });
        btn_mo_ta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DescribeFragment describeFragment = new DescribeFragment();

                replaceFragment(describeFragment,"id_comic", String.valueOf(comic.getId()));
            }
        });
        DescribeFragment describeFragment = new DescribeFragment();

        replaceFragment(describeFragment,"id_comic", String.valueOf(comic.getId()));
    }
    private void replaceFragment(Fragment fragment, String key, String value){
        Bundle args = new Bundle();
        args.putString(key, value);
        fragment.setArguments(args);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.layout_replace, fragment);
        ft.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void saveHistory(String namecomic) {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("ReadHistory").child(user.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int chek = 0;
                for (DataSnapshot item : snapshot.getChildren()) {
                    String a = item.getValue(String.class);
                    Log.d("TAG", "onDataChange: " + a);
                    if (a.equals(namecomic)) {
                        chek = 1;
                    }
                }
                if (chek == 0) {
//                    HashMap<String, Object> hashMap = new HashMap<>();
//                    hashMap.put("name", comic.getTrangtruyen());
                    reference1.push().setValue(comic.getTrangtruyen());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    public void RatiingbarTruyen(Comic comic) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("RatingTruyen").child(comic.getTrangtruyen());
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 Float count =  snapshot.getValue(float.class);
                 if (count!=null && count >0){
                     ratingBar.setRating(count);
                 }else {
                     ratingBar.setRating(0.f);
                 }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                databaseReference.child(user.getUid()).setValue(v);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Float count =0.f;
                int countNumber=0;
                for (DataSnapshot item : snapshot.getChildren()) {
                    Float itemRT = item.getValue(float.class);
                    count+=itemRT;
                    countNumber++;
                }
                rbNumber.setText(String.valueOf(count/countNumber));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}