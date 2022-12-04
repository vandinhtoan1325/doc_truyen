package com.example.doc_truyen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doc_truyen.adapter.ListComicAdapter;
import com.example.doc_truyen.adapter.ListComicBxhAdapter;
import com.example.doc_truyen.adapter.ListComicNewAdapter;
import com.example.doc_truyen.adapter.SliderAdapter;
import com.example.doc_truyen.model.Comic;
import com.example.doc_truyen.viewmodel.MainViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ListComicAdapter.onClickTruyen, ListComicBxhAdapter.onClickTruyenBxh {
    private Button btn_new_comics, btn_trending_comics, btn_detective_comics, btn_fairy_tables, btn_funny_comics;
    private ImageView image_view_conan;
    private ProgressBar progress_1;
    private ProgressBar progress_2;
    private ProgressBar progress_3;
    SliderView sliderView;
    int[] images = {R.drawable.doraemon,
            R.drawable.conan,
            R.drawable.rocket,
            R.drawable.pokemon,
            R.drawable.songoku,
            R.drawable.bakugan};


    private TextView xem_them1;
    private MainViewModel viewModel;
   // private SliderAdapter sliderAdapter;

    private RecyclerView rclBangxephang;
    private ListComicBxhAdapter comicAdapterBangxephang;
    private RecyclerView rclTruyenMoi;
    private ListComicNewAdapter comicAdapterTruyenMoi;
    private RecyclerView rclTruyenHay;
    private ListComicBxhAdapter comicAdapterTruyenHay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sliderView = view.findViewById(R.id.imgae_slider);
        progress_1 = view.findViewById(R.id.progress_1);
        progress_2 = view.findViewById(R.id.progress_2);
        progress_3 = view.findViewById(R.id.progress_3);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        SliderAdapter sliderAdapter = new SliderAdapter(images);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        return view;
    }



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rclBangxephang = view.findViewById(R.id.rclBangxephang);
        comicAdapterBangxephang = new ListComicBxhAdapter(new ArrayList<>(), requireContext(), this::clickTruyenBxh);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.HORIZONTAL,true);
        rclBangxephang.setLayoutManager(mLayoutManager);
        rclBangxephang.setAdapter(comicAdapterBangxephang);


        rclTruyenMoi = view.findViewById(R.id.rclTruyenMoi);
        comicAdapterTruyenMoi = new ListComicNewAdapter(new ArrayList<>(), getActivity().getApplicationContext(), this::clickTruyen);
        RecyclerView.LayoutManager mLayoutManager3 = new GridLayoutManager(getActivity(),2,GridLayoutManager.HORIZONTAL,true);
        rclTruyenMoi.setLayoutManager(mLayoutManager3);
        rclTruyenMoi.setAdapter(comicAdapterTruyenMoi);

        rclTruyenHay = view.findViewById(R.id.rclTruyenHay);
        comicAdapterTruyenHay = new ListComicBxhAdapter(new ArrayList<>(), requireContext(), this::clickTruyenBxh);
        RecyclerView.LayoutManager mLayoutManager2 = new GridLayoutManager(getActivity(),2,GridLayoutManager.HORIZONTAL,true);
        rclTruyenHay.setLayoutManager(mLayoutManager2);
        rclTruyenHay.setAdapter(comicAdapterTruyenHay);

        observeViewModel();
    }

    private void observeViewModel() {
//        viewModel.listSlideData.observe(requireActivity(), new Observer<ArrayList<String>>() {
//            @Override
//            public void onChanged(ArrayList<String> comics) {
//                if (!viewModel.loadSlide) {
//                    sliderAdapter = new SliderAdapter(comics, getContext()  , HomeFragment.this);
//                    sliderView.setSliderAdapter(sliderAdapter);
//                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
//                    sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
//                    sliderView.startAutoCycle();
//                    viewModel.loadSlide = true;
//                }
//            }
//        });

        viewModel.listBxhData.observe(getActivity(), new Observer<ArrayList<Comic>>() {
            @Override
            public void onChanged(ArrayList<Comic> comics) {
                comicAdapterBangxephang.filterList(comics);
                progress_1.setVisibility(View.INVISIBLE);
            }
        });


        viewModel.listTruyenMoiData.observe(getActivity(), new Observer<ArrayList<Comic>>() {
            @Override
            public void onChanged(ArrayList<Comic> comics) {
                comicAdapterTruyenMoi.updateData(comics);
                progress_2.setVisibility(View.INVISIBLE);
            }
        });

        viewModel.listTruyenDeXuatData.observe(getActivity(), new Observer<ArrayList<Comic>>() {
            @Override
            public void onChanged(ArrayList<Comic> comics) {
                comicAdapterTruyenHay.filterList(comics);
                progress_3.setVisibility(View.INVISIBLE);
            }
        });
    }

//    @Override
//    public void clickSlide(Integer pos) {
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("key_object_comic", viewModel.getListComic().get(pos));
//        Intent intent = new Intent(requireActivity(), IntroduceActivity.class);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }

    @Override
    public void clickTruyen(Comic comic) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_object_comic", comic);
        Intent intent = new Intent(requireContext(), IntroduceActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadSlide = false;
    }

    @Override
    public void clickTruyenBxh(Comic comic) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_object_comic", comic);
        Intent intent = new Intent(requireContext(), IntroduceActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public interface xemThemTruyen {
        void xemthem();
    }
}


