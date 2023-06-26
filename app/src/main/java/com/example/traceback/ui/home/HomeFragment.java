package com.example.traceback.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.traceback.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ImageView emptyImage;
    TextView title, subtitle;

    int postCount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getPostcount();
//        setContentView(R.layout.activity_homefeed);

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.emptyTitle;
//        textView.setVisibility(View.VISIBLE);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        emptyImage=binding.emptyImage;
//        title=findViewById(R.id.emptyTitle);
        subtitle=binding.emptySubtitle;

        if(postCount==0){
            emptyImage.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            subtitle.setVisibility(View.VISIBLE);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getPostcount() {
        postCount=0;
    }
}