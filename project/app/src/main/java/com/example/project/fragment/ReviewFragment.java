package com.example.project.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);  // 메뉴 사용 설정
    }

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_review_filter, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            showCategoryFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCategoryFilterDialog() {
        String[] categories = {"전시/공연", "영화", "도서"};
        boolean[] checkedItems = new boolean[categories.length];
        List<String> selectedCategories = new ArrayList<>();

        new AlertDialog.Builder(getContext())
                .setTitle("카테고리 선택")
                .setMultiChoiceItems(categories, checkedItems, (dialog, which, isChecked) -> {
                    if (isChecked) {
                        selectedCategories.add(categories[which]);
                    } else {
                        selectedCategories.remove(categories[which]);
                    }
                })
                .setPositiveButton("적용", (dialog, which) -> {
                    applyCategoryFilter(selectedCategories);
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void applyCategoryFilter(List<String> selectedCategories) {
        new Thread(() -> {
            List<Content> all = MainActivity.db.appDao().getAllContents();
            List<Content> filtered;

            if (selectedCategories.isEmpty()) {
                filtered = all;
            } else {
                filtered = new ArrayList<>();
                for (Content c : all) {
                    if (selectedCategories.contains(c.category)) {
                        filtered.add(c);
                    }
                }
            }

            requireActivity().runOnUiThread(() -> {
                adapter.updateList(filtered);
            });
        }).start();
    }

}
