package com.example.doc_truyen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText tip_email, tip_password, tip_username, trip_confirmpass;
    private Button btn_register;
    private TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        tip_email = findViewById(R.id.tip_rg_email);
        tip_password = findViewById(R.id.tip_rg_password);
        btn_register = findViewById(R.id.btn_register);
        tv_login = findViewById(R.id.tv_login);
        tip_username = findViewById(R.id.tip_rg_username);
        trip_confirmpass = findViewById(R.id.tip_rg_confirm_password);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRegister();
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i1);
            }
        });
    }

    public void onClickRegister() {
        if (tip_username.getText().toString().trim() == null || tip_username.getText().toString().trim().isEmpty()) {
            tip_username.setError("Không được để trống tên!");
        } else if (tip_email.getText().toString().trim() == null || tip_email.getText().toString().trim().isEmpty()) {
            tip_email.setError("Không được để trống email!");
        } else if (tip_password.getText().toString().trim() == null || tip_password.getText().toString().trim().isEmpty()) {
            tip_password.setError("Không được để trống password!");
        } else if (trip_confirmpass.getText().toString().trim() == null || trip_confirmpass.getText().toString().trim().isEmpty()) {
            trip_confirmpass.setError("Không được để trống xác nhận password!");
        } else if (tip_password.getText().toString().trim().length() < 6) {
            tip_password.setError("Độ dài mật khẩu phải lớn hơn 5");
        } else if (trip_confirmpass.getText().toString().trim().length() < 6) {
            trip_confirmpass.setError("Độ dài mật khẩu phải lớn hơn 5");
        } else if (!trip_confirmpass.getText().toString().trim().equals(tip_password.getText().toString().trim())) {
            trip_confirmpass.setError("Mật khẩu không trùng khớp");
        } else {
            String email = tip_email.getText().toString().trim();
            String password = tip_password.getText().toString().trim();
            String name = tip_username.getText().toString();

            if (email != "" && password != "" && name != "") {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

                                    if (user != null) {
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("username", name);
                                        hashMap.put("email", email);
                                        hashMap.put("id", user.getUid());

                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "Xác thực thành công.",
                                                            Toast.LENGTH_LONG).show();
                                                    Intent i1 = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    startActivity(i1);
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Email này đã được đăng kí!",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }
    }
}