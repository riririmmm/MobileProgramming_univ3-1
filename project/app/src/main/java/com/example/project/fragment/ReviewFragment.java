package com.example.project.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.ReviewAdapter;
import com.example.project.model.Content;
import com.example.project.model.ContentWithReview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ReviewFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReviewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        recyclerView = view.findViewById(R.id.recycler_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ReviewAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // 💡 DB 접근을 백그라운드 스레드에서 수행
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Content> reviewList = MainActivity.db.appDao().getAllContents();

            Log.d("리뷰탭", "불러온 콘텐츠 수: " + reviewList.size());
            for (Content c : reviewList) {
                Log.d("리뷰탭", "제목: " + c.title + ", 별점: " + c.rating + ", 이미지: " + c.imageUrl);
            }

            requireActivity().runOnUiThread(() -> {
                adapter.updateList(reviewList);
            });
        });

        return view;
    }
}
