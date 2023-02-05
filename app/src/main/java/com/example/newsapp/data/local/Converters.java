package com.example.newsapp.data.local;

import androidx.room.TypeConverter;

import com.example.newsapp.models.Source;

public class Converters {

    @TypeConverter
    public String fromSource(Source source) {
        return source.getName();
    }

    @TypeConverter
    public Source toSource(String name) {
        return new Source(name);
    }
}
