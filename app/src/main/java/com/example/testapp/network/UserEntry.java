package com.example.testapp.network;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.example.testapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A product entry in the list of products.
 */
public class UserEntry {
    private static final String TAG = UserEntry.class.getSimpleName();

    public final String first_name;
    public final String last_name;
    public final Uri dynamicImage;
    public final String image;
    public final String country;
    public final String city;
    public final String age;

    public UserEntry(
            String first_name, String last_name, String dynamicImage, String image, String age, String country, String city) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.dynamicImage = Uri.parse(dynamicImage);
        this.image = image;
        this.country=country;
        this.city=city;
        this.age = age;
    }

    /**
     * Loads a raw JSON at R.raw.products and converts it into a list of ProductEntry objects
     */
    public static List<UserEntry> initUserEntryList(Resources resources) {

        InputStream inputStream = resources.openRawResource(R.raw.users);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int pointer;
            while ((pointer = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, pointer);
            }
        } catch (IOException exception) {
            Log.e(TAG, "Error writing/reading from the JSON file.", exception);
        } finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                Log.e(TAG, "Error closing the input stream.", exception);
            }
        }
        String jsonProductsString = writer.toString();
        Gson gson = new Gson();
        Type productListType = new TypeToken<ArrayList<UserEntry>>() {
        }.getType();
        return gson.fromJson(jsonProductsString, productListType);
    }
}
