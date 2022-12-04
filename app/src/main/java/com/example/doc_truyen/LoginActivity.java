package com.example.doc_truyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText tip_email, tip_password;
    private Button btn_login;
    private TextView tv_register;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String nameSave = sh.getString("name", "");
        String passSave = sh.getString("pass", "");

        mAuth = FirebaseAuth.getInstance();
        checkBox = findViewById(R.id.cb_savepass);
        tip_email = findViewById(R.id.tip_lg_email);
        tip_password = findViewById(R.id.tip_lg_password);
        btn_login = findViewById(R.id.btn_lg_login);
        tv_register = findViewById(R.id.tv_lg_register);
        tip_email.setText(nameSave);
        tip_password.setText(passSave);
        if (nameSave != "") {
            checkBox.setChecked(true);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogin();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i1);
            }
        });
    }


    public void onClickLogin() {
        if (tip_email.getText().toString().trim() == null || tip_email.getText().toString().trim().isEmpty()) {
            tip_email.setError("Không được để trống email!");
        } else if (tip_password.getText().toString().trim() == null || tip_password.getText().toString().trim().isEmpty()) {
            tip_password.setError("Không được để trống password!");
        } else if (tip_password.getText().toString().trim().length() < 6) {
            tip_password.setError("Độ dài mật khẩu phải lớn hơn 5");
        } else {
            String email = tip_email.getText().toString().trim();
            String password = tip_password.getText().toString().trim();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                if (checkBox.isChecked()) {
                                    myEdit.putString("name", email);
                                    myEdit.putString("pass", password);
                                } else {
                                    myEdit.putString("name", "");
                                    myEdit.putString("pass", "");
                                }
                                myEdit.commit();
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công",
                                        Toast.LENGTH_LONG).show();
                                Intent i1 = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i1);
                            } else {
                                Toast.makeText(LoginActivity.this, "Mật khẩu hoặc tài khoản sai",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }
}