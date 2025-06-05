package com.example.project.model;

import java.util.List;

public class KmdbMovieResponse {
    public List<MovieData> Data;

    public static class MovieData {
        public List<MovieResult> Result;
    }

    public static class MovieResult {
        public String title;
        public String posters;
        public String runtime;
        public Plots plots;
        public String genre;

        public String getPlotText() {
            if (plots != null && plots.plot != null && !plots.plot.isEmpty()) {
                return plots.plot.get(0).plotText; // 첫 번째 줄거리만 사용
            }
            return "";
        }

        public static class Plots {
            public List<Plot> plot;
        }

        public static class Plot {
            public String plotLang;
            public String plotText;
        }
    }

}
