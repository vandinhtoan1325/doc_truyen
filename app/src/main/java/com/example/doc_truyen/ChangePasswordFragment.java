package com.example.doc_truyen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends AppCompatActivity {
    TextView icback;
    EditText passold, passnew, passnewcf;
    Button btnconfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_password);
        icback = findViewById(R.id.icback);
        passold = findViewById(R.id.tip_rg_passold);
        passnew = findViewById(R.id.tip_rg_passnew);
        passnewcf = findViewById(R.id.tip_rg_confirm_passnew);
        btnconfirm = findViewById(R.id.btn_rg_rchangepass);

        icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(ChangePasswordFragment.this, MainActivity.class);
                startActivity(i1);
            }
        });
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!passold.getText().toString().isEmpty() && !passnew.getText().toString().isEmpty() && !passnewcf.getText().toString().isEmpty()) {
                    if (passnew.getText().toString().equals(passnewcf.getText().toString())) {
                        updatepass(passold.getText().toString(), passnew.getText().toString());
                    } else {
                        Toast.makeText(ChangePasswordFragment.this, "Xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordFragment.this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void updatepass(String passolds, String passnews) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), passolds);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(passnews).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ChangePasswordFragment.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent i1 = new Intent(ChangePasswordFragment.this, LoginActivity.class);
                                        startActivity(i1);
                                        finish();
                                    } else {
                                        Toast.makeText(ChangePasswordFragment.this, "Độ dài mật khẩu cần lớn hơn 6 kí tự", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ChangePasswordFragment.this, "Mật khẩu cũ sai", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}