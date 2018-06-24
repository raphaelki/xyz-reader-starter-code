package com.example.xyzreader.ui.common;

import android.content.res.Resources;

import com.example.xyzreader.Constants;
import com.example.xyzreader.data.models.Article;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class ArticleUtils {

    public static String parseArticleDate(Article article) {
        Date date;
        SimpleDateFormat originalDateFormat = new SimpleDateFormat(Constants.JSON_DATE_PATTERN, getLocale());
        try {
            date = originalDateFormat.parse(article.getPublishedDate());
        } catch (ParseException exception) {
            Timber.e(exception);
            Timber.i("Could not parse date. Passing today's date for article: %s", article.getTitle());
            date = new Date();
        }
        return DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale()).format(date);
    }

    private static Locale getLocale() {
        Locale locale;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = Resources.getSystem().getConfiguration().locale;
        }
        return locale;
    }

}
