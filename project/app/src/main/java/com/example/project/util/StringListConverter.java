package com.example.project.util;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class StringListConverter {
    @TypeConverter
    public static String fromList(List<String> list) {
        return list == null ? null : String.join(",", list);
    }

    @TypeConverter
    public static List<String> toList(String value) {
        return value == null || value.isEmpty() ? null : Arrays.asList(value.split(","));
    }
}
