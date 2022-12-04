package com.example.doc_truyen;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class CommentFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LinearLayout layout_binh_luan;
    private EditText edittext_binh_luan;
    private Button btn_gui_binh_luan;
    private TextView dem_bin_luan;
    private DatabaseReference mDatabase;
    private String id;
    private FirebaseUser user;
    public CommentFragment() {
        // Required empty public constructor
    }

    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
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
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        layout_binh_luan = view.findViewById(R.id.layout_binh_luan);
        edittext_binh_luan = view.findViewById(R.id.edittext_binh_luan);
        btn_gui_binh_luan = view.findViewById(R.id.btn_gui_binh_luan);
        dem_bin_luan = view.findViewById(R.id.dem_bin_luan);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        id = getArguments().getString("id_comic", "");
        user = FirebaseAuth.getInstance().getCurrentUser();

        btnSendCommentOnClick();
        showComments();
        return view;
    }
    private void showComments(){

        mDatabase.child("listComic").child(id).child("comments").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            collectComments((Map<String,Object>) dataSnapshot.getValue());
                        } else {
                            dem_bin_luan.setText("0 bình luận");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void collectComments(Map<String,Object> comments) {

        int COMMENT_COUNT = 0;
        for (Map.Entry<String, Object> entry : comments.entrySet()){
            Map singleUser = (Map) entry.getValue();
            COMMENT_COUNT++;
            dem_bin_luan.setText(COMMENT_COUNT + " bình luận");
            String comment_ = singleUser.get("comment").toString();
            String userid_ = singleUser.get("userID").toString();
            String time_ =  singleUser.get("time").toString();

            mDatabase.child("Users").child(userid_).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        if (task.getResult().exists()){

                            DataSnapshot dataSnapshot = task.getResult();
                            String name = String.valueOf(dataSnapshot.child("username").getValue());
                            try {
                                setLayoutComment(name, time_, comment_);
                            } catch (NullPointerException e){
                            }
                        }
                    }
                }
            });

        }
    }
    private void btnSendCommentOnClick(){
        btn_gui_binh_luan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String COMMENT =  edittext_binh_luan.getText().toString().trim();
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss\ndd/MM/yyyy");
                Date date = new Date();
                String time = dateFormat.format(date);
                if (!COMMENT.matches("")){
                    addComment(user.getUid(), COMMENT, time);
                };
                reload();
            }
        });
    }
    private void reload(){

        edittext_binh_luan.setText("");
        layout_binh_luan.removeAllViewsInLayout();
      //  showComments();
    }
    private void addComment(String userID, String comment, String time){
        Comments comments = new Comments(userID, comment, time);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss_dd_MM_yyyy");
        Date date = new Date();
        String time_ = dateFormat.format(date);
        mDatabase.child("listComic").child(id).child("comments").child(userID +"_"+ time_).setValue(comments).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                } else {
                }
            }
        });
    }
    private void setLayoutComment(String userid, String time, String comment){
        LinearLayout parent = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins( dpToPx(10), 0, dpToPx(10), dpToPx(20));
        parent.setLayoutParams(params);
        parent.setOrientation(LinearLayout.VERTICAL);

        LinearLayout layout2 = new LinearLayout(getActivity());

        layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout2.setOrientation(LinearLayout.HORIZONTAL);


        TextView USER_NAME = new TextView(getActivity());
        TextView COMMENT_TIME = new TextView(getActivity());
        TextView COMMENT = new TextView(getActivity());
        View view = new View(getActivity());

        USER_NAME.setText(userid);
        USER_NAME.setTextSize(18);
        USER_NAME.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.arial));
        USER_NAME.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        COMMENT_TIME.setText(time);

        COMMENT.setText(comment);
        COMMENT.setTextColor(getResources().getColor(R.color.black));
        COMMENT.setTextSize(18);

        view.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,3));
        view.setBackgroundResource(R.color.black);

        layout2.addView(USER_NAME);
        layout2.addView(COMMENT_TIME);

        parent.addView(layout2);
        parent.addView(COMMENT);
        parent.addView(view);

        layout_binh_luan.addView(parent);

    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public class Comments {
        public String userID, comment, time;

        public Comments(){

        }

        public Comments(String userID, String comment, String time) {
            this.userID = userID;
            this.comment = comment;
            this.time = time;
        }
    }
}