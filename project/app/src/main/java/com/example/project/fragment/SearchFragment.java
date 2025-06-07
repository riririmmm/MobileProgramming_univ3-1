package com.example.project.fragment;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.SearchAdapter;
import com.example.project.SearchItem;
import com.example.project.api.BookApiService;
import com.example.project.api.CultureEventService;
import com.example.project.api.MovieApiService;
import com.example.project.model.BookDocument;
import com.example.project.model.KakaoBookResponse;
import com.example.project.model.KmdbMovieResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {

    private Spinner spinnerCategory;
    private EditText editSearch;
    private Button btnSearch;
    private RecyclerView recyclerView;

    private final List<SearchItem> searchResults = new ArrayList<>();
    private SearchAdapter searchAdapter;

    private static final String API_KEY = "KakaoAK 36146fd94f8ef0f8e3e39c1c3c3ab9a4";
    private static final String KMDB_API_KEY = "88P9S5241Y6M69G97SE4";
    private static final String CULTURE_API_KEY = "pyMVr%2BULSiFtOQIsRD%2F0zXNqReuQ%2FEKnkRWE9Uv%2FoQ6qfFtKVznzbKcyOkPpWVB5wWqvt0HdUsKFUHxJuag3Zg%3D%3D";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        spinnerCategory = view.findViewById(R.id.spinner_category);
        editSearch = view.findViewById(R.id.edit_search);
        btnSearch = view.findViewById(R.id.btn_search);
        recyclerView = view.findViewById(R.id.recycler_search_result);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(searchAdapter);

        btnSearch.setOnClickListener(v -> {
            String keyword = editSearch.getText().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            searchResults.clear();
            searchAdapter = new SearchAdapter(getContext(), searchResults, category);
            recyclerView.setAdapter(searchAdapter);

            if (category.equals("도서")) {
                searchResults.clear();
                searchBooks(keyword);
            } else if (category.equals("영화")) {
                searchResults.clear();
                searchMovies(keyword);
            } else if (category.equals("전시/공연")) {
                searchResults.clear();
                searchCultureEvents(keyword);
            } else {
                Toast.makeText(getContext(), category + " 검색 API는 아직 미구현입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void searchBooks(String keyword) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BookApiService api = retrofit.create(BookApiService.class);
        Call<KakaoBookResponse> call = api.searchBooks(API_KEY, keyword);

        call.enqueue(new Callback<KakaoBookResponse>() {
            @Override
            public void onResponse(Call<KakaoBookResponse> call, Response<KakaoBookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BookDocument> docs = response.body().documents;
                    searchResults.clear();
                    for (BookDocument doc : docs) {
                        String author;

                        if (doc.authors.length == 1)
                            author = doc.authors[0];
                        else
                            author = doc.authors[0] + " 외";
                        searchResults.add(new SearchItem(
                                doc.title.trim(),
                                doc.thumbnail,
                                "저자: " + author + "\t 출판사: " + doc.publisher,
                                doc.contents,
                                "isbn: " + doc.isbn
                        ));
                        System.out.println(doc.contents);
                    }
                    searchAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "API 응답 오류", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KakaoBookResponse> call, Throwable t) {
                Toast.makeText(getContext(), "API 요청 실패: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void searchMovies(String keyword) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.koreafilm.or.kr/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieApiService api = retrofit.create(MovieApiService.class);
        Call<KmdbMovieResponse> call = api.searchMovies("kmdb_new2", "Y", KMDB_API_KEY, keyword);

        call.enqueue(new Callback<KmdbMovieResponse>() {
            @Override
            public void onResponse(Call<KmdbMovieResponse> call, Response<KmdbMovieResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().Data.isEmpty()) {
                    List<KmdbMovieResponse.MovieResult> results = response.body().Data.get(0).Result;
                    searchResults.clear();
                    for (KmdbMovieResponse.MovieResult result : results) {
                        String imageUrl = (result.posters != null && result.posters.contains("|"))
                                ? result.posters.split("\\|")[0] : result.posters;
                        if (imageUrl != null && imageUrl.startsWith("http://")) {
                            imageUrl = imageUrl.replaceFirst("http://", "https://");
                        }

                        String rawTitle = result.title
                                .replaceAll("!HS|!HE", "")
                                .trim();
                        String decoded = Html.fromHtml(rawTitle, Html.FROM_HTML_MODE_LEGACY).toString();
                        String cleanTitle = decoded
                                .replaceAll("\\u00A0", " ")
                                .replaceAll("\\s+", " ");

                        searchResults.add(new SearchItem(
                                cleanTitle,
                                imageUrl,
                                "러닝타임: " + result.runtime + "분",
                                result.getPlotText(),
                                "장르: " + result.genre
                        ));
                        System.out.println(result.getPlotText());
                    }
                    searchAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "영화 API 응답 없음", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KmdbMovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "영화 API 요청 실패: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void searchCultureEvents(String keyword) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apis.data.go.kr/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CultureEventService service = retrofit.create(CultureEventService.class);

        Call<ResponseBody> call = service.searchCultureEvents(
                CULTURE_API_KEY,
                keyword,
                20,
                1
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String xml = response.body().string();
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        InputSource is = new InputSource(new StringReader(xml));
                        Document doc = builder.parse(is);
                        NodeList items = doc.getElementsByTagName("item");
                        searchResults.clear();

                        for (int i = 0; i < items.getLength(); i++) {

                            Element element = (Element) items.item(i);

                            String seq = getTagValue("seq", element);
                            fetchEventDetail(seq);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void fetchEventDetail(String seq) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apis.data.go.kr/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CultureEventService service = retrofit.create(CultureEventService.class);

        Call<ResponseBody> call = service.getEventDetail(CULTURE_API_KEY, seq);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String xml = response.body().string();
                        Log.d("DEBUG", "받은 XML: " + xml);
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        InputSource is = new InputSource(new StringReader(xml));
                        Document doc = builder.parse(is);
                        System.out.println(doc);

                        NodeList dbList = doc.getElementsByTagName("item");

                        if (dbList.getLength() > 0) {
                            Element element = (Element) dbList.item(0);

                            String rawTitle = getTagValue("title", element);
                            String rawPlace = getTagValue("place", element);
                            String rawImgUrl = getTagValue("imgUrl", element);
                            String rawPlaceAdrr = getTagValue("placeAddr", element);

                            // HTML 디코딩 + trim
                            String title = Html.fromHtml(rawTitle, Html.FROM_HTML_MODE_LEGACY).toString().trim();
                            String place = Html.fromHtml(rawPlace, Html.FROM_HTML_MODE_LEGACY).toString().trim();
                            String imgUrl = rawImgUrl != null ? rawImgUrl.trim() : "";
                            String placeAddr = Html.fromHtml(rawPlaceAdrr, Html.FROM_HTML_MODE_LEGACY).toString().trim();

                            // 기본값 처리
                            if (title.isEmpty()) title = "(제목 없음)";
                            if (place.isEmpty()) place = "(장소 없음)";
                            if (placeAddr.isEmpty()) placeAddr = "(주소 없음)";

                            if (imgUrl.startsWith("http://")) {
                                imgUrl = imgUrl.replaceFirst("http://", "https://");
                            }

                            // 리스트에 추가
                            searchResults.add(new SearchItem(title, imgUrl, place, placeAddr, placeAddr));
                            searchAdapter.notifyDataSetChanged();

                        } else {
                            Log.e("DEBUG", "[상세조회 실패] item 태그 없음, seq=" + seq);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private String getTagValue(String tag, Element element) {
        if (element == null) return "";
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

}