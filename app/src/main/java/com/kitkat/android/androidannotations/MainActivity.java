package com.kitkat.android.androidannotations;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.converter.StringHttpMessageConverter;

// setContentView(R.layout.activity_main);
// 반드시, AndroidManifest,xml Component 선언 시, <activity android:name=".MainActivity_"/>
@EActivity(R.layout.activity_main)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class MainActivity extends AppCompatActivity {
    @ViewById
    TextView textViewBody;

    @ViewById
    Button buttonConnect;

    @RestService
    GoogleService googleService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        textViewBody.setTextColor(Color.BLACK);
    }

    @Click(R.id.buttonConnect)
    public void connect() {
        runBackground();
    }

    // AsyncTask.doInBackground() → SubThread
    @Background
    public void runBackground() {
        String body = googleService.get();
        writeOnUi(body);
    }

    // AsyncTask.onProgressUpdate() → MainThread
    @UiThread // import org.androidannotations.annotations.UiThread;
    public void writeOnUi(String str) {
        textViewBody.setText(str);
    }
}

// Rest Annotation 은 Top Level 만 가능
// 단일 Class Level 만 가능
@Rest(rootUrl = "http://www.google.com", converters = {StringHttpMessageConverter.class})
interface GoogleService {
    @Get("/") // Query String
    String get();
}
