package com.example.doc_truyen;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.doc_truyen.adapter.MyViewPager;
import com.example.doc_truyen.viewmodel.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//import com.google.doc_truyen.auth.FirebaseAuth;
//import com.google.doc_truyen.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.xemThemTruyen {

    private MainViewModel viewModel;
    private DrawerLayout drawerLayout;
    private ViewPager2 ViewPager;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_CATEGORY = 1;
    private static final int FRAGMENT_SETTING = 2;
    private static final int FRAGMENT_SUPPORT = 3;
    private static final int FRAGMENT_MY_PROFILE = 4;
    private static final int FRAGMENT_CHANGE_PASSWORD = 5;

    private int mCurrentFragment = FRAGMENT_HOME;

    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    private ImageView img_avatar;
    private TextView textView_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        initUi();
        observeViewModel();
        viewModel.loadData(this);
    }

    private void observeViewModel() {
    }

    private void initUi() {
        navigationView = findViewById(R.id.navigation_view);
        img_avatar = navigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        textView_email = navigationView.getHeaderView(0).findViewById(R.id.text_view_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black212121));
        toggle.syncState();
        ViewPager = findViewById(R.id.viewPagerMain);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.idNavigationBottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationView.setNavigationItemSelectedListener(this);
        showUserInformation();
        MyViewPager viewPager = new MyViewPager(this);
        ViewPager.setAdapter(viewPager);
        ViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.topTruyen).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.TheLoai).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.timkiem).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.user).setChecked(true);
                        break;
                }
            }
        });
        ViewPager.setUserInputEnabled(false);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.home) {
//            if (mCurrentFragment != FRAGMENT_HOME) {
//                replaceFragment(new HomeFragment());
//                mCurrentFragment = FRAGMENT_HOME;
//            }
//        } else if (id == R.id.library) {
//            if (mCurrentFragment != FRAGMENT_ADD_COMICS) {
//                replaceFragment(new NewComicsFragment());
//                mCurrentFragment = FRAGMENT_ADD_COMICS;
//            }
//        }
//
//        else if (id == R.id.setting){
//            if(mCurrentFragment != FRAGMENT_SETTING){
//                replaceFragment(new SettingFragment());
//                mCurrentFragment = FRAGMENT_SETTING;
//            }
//        }else if (id == R.id.support){
//            if(mCurrentFragment != FRAGMENT_SUPPORT){
//                replaceFragment(new SupportFragment());
//                mCurrentFragment = FRAGMENT_SUPPORT;
//            }
//        }else if (id == R.id.my_profile){
//            if(mCurrentFragment != FRAGMENT_MY_PROFILE){
//                replaceFragment(new MyProfileFragment());
//                mCurrentFragment = FRAGMENT_MY_PROFILE;
//            }
//        }else
        if (id == R.id.change_password) {
            Intent i1 = new Intent(MainActivity.this, ChangePasswordFragment.class);
            startActivity(i1);

        }
        if (id == R.id.log_out) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "Đăng xuất thành công.",
                    Toast.LENGTH_LONG).show();
            Intent i1 = new Intent(this, LoginActivity.class);
            startActivity(i1);
            finish();
        }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }


        private void showUserInformation () {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                return;
            }
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            textView_email.setText(email);
            Glide.with(this).load(photoUrl).error(R.drawable.icons8_user_100).into(img_avatar);
        }

        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    ViewPager.setCurrentItem(0);
                      getSupportActionBar().setTitle("Trang Chủ");
                    break;
                case R.id.topTruyen:
                    ViewPager.setCurrentItem(1);
                    getSupportActionBar().setTitle("Truyện");
                    break;
                case R.id.TheLoai:
                    ViewPager.setCurrentItem(2);
                    getSupportActionBar().setTitle("Thể Loại");
                    break;
                case R.id.timkiem:
                    ViewPager.setCurrentItem(3);
                    getSupportActionBar().setTitle("Tìm Kiếm");
                    break;
                case R.id.user:
                    ViewPager.setCurrentItem(4);
                    getSupportActionBar().setTitle("Trang Cá Nhân");
                    break;
            }
            return false;
        };

        @Override
        public void xemthem () {
            ViewPager.setCurrentItem(1);
        }
    }