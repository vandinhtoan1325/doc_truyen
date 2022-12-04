package com.example.doc_truyen.viewmodel;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doc_truyen.model.Comic;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Copyright © 2022 Asian Mobile Co.,Ltd
 * Created by am_taitqs on 14-Nov-22.
 */
public class MainViewModel extends ViewModel {

    private ArrayList<Comic> listComic = new ArrayList<>();
    public MutableLiveData<ArrayList<Comic>> _listComicData = new MutableLiveData<>();
    public LiveData<ArrayList<Comic>> listComicData = _listComicData;

    private ArrayList<String> listSlide = new ArrayList<>();
    public MutableLiveData<ArrayList<String>> _listSlideData = new MutableLiveData<>();
    public LiveData<ArrayList<String>> listSlideData = _listSlideData;

    private ArrayList<Comic> listBxh = new ArrayList<>();
    public MutableLiveData<ArrayList<Comic>> _listBxhData = new MutableLiveData<>();
    public LiveData<ArrayList<Comic>> listBxhData = _listBxhData;

    private ArrayList<Comic> listTruyenDeXuat = new ArrayList<>();
    public MutableLiveData<ArrayList<Comic>> _listTruyenDeXuatData = new MutableLiveData<>();
    public LiveData<ArrayList<Comic>> listTruyenDeXuatData = _listTruyenDeXuatData;

    private ArrayList<Comic> listTruyenMoi = new ArrayList<>();
    public MutableLiveData<ArrayList<Comic>> _listTruyenMoiData = new MutableLiveData<>();
    public LiveData<ArrayList<Comic>> listTruyenMoiData = _listTruyenMoiData;

    private ArrayList<Comic> listTruyenHay = new ArrayList<>();
    public MutableLiveData<ArrayList<Comic>> _listTruyenHayData = new MutableLiveData<>();
    public LiveData<ArrayList<Comic>> listTruyenHayData = _listTruyenHayData;

    public Boolean loadSlide = false;

    public void loadData(Context context) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("listComic");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComic.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Comic comic = item.getValue(Comic.class);
                    listComic.add(comic);
                }
//                uploadData();
//                loadThumbSlide();
                loadListBxh();
                loadListTruyenHot();
                loadListTruyenMoi();
                _listComicData.setValue(listComic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadListTruyenMoi() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                listTruyenMoi.clear();
              listTruyenMoi.addAll(listComic);

                Collections.sort(listTruyenMoi, new Comparator<Comic>() {
                    @Override
                    public int compare(Comic comic, Comic t1) {
                        return comic.getId() + t1.getId();
                    }
                });
                listTruyenMoi.subList(0,5);
                _listTruyenMoiData.postValue(listTruyenMoi);
            }
        }).start();
    }
    private void loadListTruyenHot() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                listTruyenDeXuat.clear();
                listTruyenDeXuat.addAll(listComic);
                Collections.sort(listTruyenDeXuat, new Comparator<Comic>() {
                    @Override
                    public int compare(Comic comic, Comic t1) {
                        return comic.getVote() - t1.getVote();
                    }
                });
                _listTruyenDeXuatData.postValue(listTruyenDeXuat);
            }
        }).start();
    }

    private void loadListBxh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                listBxh.clear();
                listBxh.addAll(listComic);
                Collections.sort(listBxh, new Comparator<Comic>() {
                    @Override
                    public int compare(Comic comic, Comic t1) {
                        return comic.getView() - t1.getView();
                    }
                });
                listBxh.subList(0,5);
                _listBxhData.postValue(listBxh);
            }
        }).start();
    }

    private void loadThumbSlide() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                listSlide.clear();
                for (int index = 0; index < 1; index++) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference listRef = storage.getReference().child(listComic.get(index).getTrangtruyen());
                    listRef.listAll().addOnSuccessListener(listResult -> {
                        if (listResult.getItems().size() > 0) {
                            listRef.child("intro.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    listSlide.add(uri.toString());
                                    if (listSlide.size() >= 0) {
                                        _listSlideData.postValue(listSlide);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }).start();
    }

    public ArrayList<Comic> getListComic() {
        return listComic;
    }


    private void uploadData() {
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference = firebaseDatabase.getReference("listComic");
//
//        Comic comic = new Comic(6, "One Piece là câu truyện kể về Luffy và các thuyền viên của mình. Khi còn nhỏ, Luffy ước mơ trở thành Vua Hải Tặc. Cuộc sống của cậu bé thay đổi khi cậu vô tình có được sức mạnh có thể co dãn như cao su, nhưng đổi lại, cậu không bao giờ có thể bơi được nữa. Giờ đây, Luffy cùng những người bạn hải tặc của mình ra khơi tìm kiếm kho báu One Piece, kho báu vĩ đại nhất trên thế giới. Trong One Piece, mỗi nhân vật trong đều mang một nét cá tính đặc sắc kết hợp cùng các tình huống kịch tính, lối dẫn truyện hấp dẫn chứa đầy các bước ngoặt bất ngờ và cũng vô cùng hài hước đã biến One Piece trở thành một trong những bộ truyện nổi tiếng nhất không thể bỏ qua. Hãy đọc One Piece để hòa mình vào một thế giới của những hải tặc rộng lớn, đầy màu sắc, sống động và thú vị, cùng đắm chìm với những nhân vật yêu tự do, trên hành trình đi tìm ước mơ của mình.", "Duyệt Động văn hóa", "Vua Hải Tặc", "Anime", "https://i.truyenvua.com/ebook/190x247/dao-hai-tac_1552224567.jpg", "TruyenDaoHaiTac", 0, 0);
//        databaseReference.child(String.valueOf(comic.getId())).setValue(comic, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                Log.e("TAG", "onComplete: ");
//            }
//        });
    }

}
