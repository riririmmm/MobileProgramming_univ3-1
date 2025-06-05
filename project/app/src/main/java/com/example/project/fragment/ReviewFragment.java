package com.example.project.fragment;

import android.os.Bundle;
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
import com.example.project.model.ContentWithReview;

import java.util.List;

public class ReviewFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReviewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        recyclerView = view.findViewById(R.id.recycler_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // DB에서 리뷰가 등록된 콘텐츠 목록 불러오기
        List<ContentWithReview> reviewList = MainActivity.db.appDao().getReviewedContent();

        adapter = new ReviewAdapter(getContext(), reviewList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
