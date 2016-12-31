# Android Annotations
![](http://dl.dropbox.com/s/4sgv0wb6cjnatv3/AndroidAnnotations.png)

**AndroidAnnotations is an Open Source framework that speeds up Android development.**  
It takes care of the plumbing, and lets you concentrate on what's really important.  
By simplifying your code, it facilitates its maintenance.  

http://androidannotations.org/  
https://github.com/androidannotations/androidannotations/wiki

--------------------------------------------------

## Gradle

Android Plugin 버전 2.3.0부터 Annotation Processor에 대한 지원 기능이 포함되어 있다.  
이전 버전의 플러그인을 사용해야하는 경우, **[android-apt]** 플러그인 사용법을 확인.

## Here is a working sample for Android Plugin 2.3.0

#### **app.gradle**
```java
buildscript {
    repositories {
      mavenCentral()
    }
    dependencies {
        // replace with the current version of the Android plugin
        classpath 'com.android.tools.build:gradle:2.3.0'
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}
```

#### **build.gradle**
```java
apply plugin: 'com.android.application'

def AAVersion = 'XXX'
dependencies {
    annotationProcessor "org.androidannotations:androidannotations:$AAVersion"
    compile "org.androidannotations:androidannotations-api:$AAVersion"
}

android {
    compileSdkVersion 25
    buildToolsVersion "25.0.2"

    defaultConfig {
        minSdkVersion 9
        targetSdkVersion 25

        // If you have different applicationIds for buildTypes or productFlavors uncomment this block.
        //javaCompileOptions {
        //    annotationProcessorOptions {
        //        arguments = ["resourcePackageName": android.defaultConfig.applicationId]
        //    }
        //}
    }
}

dependencies {
    annotationProcessor "org.androidannotations:{plugin-name}:$AAVersion"
    compile "org.androidannotations:{plugin-name}-api:$AAVersion"
}
```

--------------------------------------------------

## Here is a working sample for Android Plugin 2.3.0 ↓

#### **app.gradle**
```java
buildscript {
    repositories {
      mavenCentral()
    }
    dependencies {
        // replace with the current version of the Android plugin
        classpath 'com.android.tools.build:gradle:1.5.0'
        // replace with the current version of the android-apt plugin
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}
```

#### **build.gradle**

```java
apply plugin: 'com.android.application'
apply plugin: 'android-apt'
def AAVersion = 'XXX' // Recent Version 4.1.0

dependencies {
    apt "org.androidannotations:androidannotations:$AAVersion"
    compile "org.androidannotations:androidannotations-api:$AAVersion"

    // for Spring api
    apt "org.androidannotations:rest-spring:$AAVersion"
    compile "org.androidannotations:rest-spring-api:$AAVersion"
    compile 'org.springframework.android:spring-android-rest-template:2.0.0.M3'
}

apt {
    arguments {
        // you should set your package name here if you are using different application IDs
        // resourcePackageName "your.package.name"

        // You can set optional annotation processing options here, like these commented options:
        // logLevel 'INFO'
        // logFile '/var/log/aa.log'
    }
}

android {
    compileSdkVersion 23
    buildToolsVersion "23.0.2"

    defaultConfig {
        minSdkVersion 9
        targetSdkVersion 23
    }
}

dependencies {
    // apt "org.androidannotations:{plugin-name}:$AAVersion"
    // compile "org.androidannotations:{plugin-name}-api:$AAVersion"
    compile "org.androidannotations:androidannotations:$AAVersion"
}
```

--------------------------------------------------

## Source code

#### Before
```java
public class BookmarksToClipboardActivity extends Activity {

  BookmarkAdapter adapter;
 
  ListView bookmarkList;
 
  EditText search;
 
  BookmarkApplication application;
 
  Animation fadeIn;
 
  ClipboardManager clipboardManager;
 
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
 
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
 
    setContentView(R.layout.bookmarks);
 
    bookmarkList = (ListView) findViewById(R.id.bookmarkList);
    search = (EditText) findViewById(R.id.search);
    application = (BookmarkApplication) getApplication();
    fadeIn = AnimationUtils.loadAnimation(this, anim.fade_in);
    clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
 
    View updateBookmarksButton1 = findViewById(R.id.updateBookmarksButton1);
    updateBookmarksButton1.setOnClickListener(new OnClickListener() {
 
      @Override
      public void onClick(View v) {
        updateBookmarksClicked();
      }
    });
 
    View updateBookmarksButton2 = findViewById(R.id.updateBookmarksButton2);
    updateBookmarksButton2.setOnClickListener(new OnClickListener() {
 
      @Override
      public void onClick(View v) {
        updateBookmarksClicked();
      }
    });
 
    bookmarkList.setOnItemClickListener(new OnItemClickListener() {
 
      @Override
      public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
        Bookmark selectedBookmark = (Bookmark) p.getAdapter().getItem(pos);
        bookmarkListItemClicked(selectedBookmark);
      }
    });
 
    initBookmarkList();
  }
 
  void initBookmarkList() {
    adapter = new BookmarkAdapter(this);
    bookmarkList.setAdapter(adapter);
  }
 
  void updateBookmarksClicked() {
    UpdateBookmarksTask task = new UpdateBookmarksTask();
 
    task.execute(search.getText().toString(), application.getUserId());
  }

  private static final String BOOKMARK_URL = //
  "http://www.bookmarks.com/bookmarks/{userId}?search={search}";


  class UpdateBookmarksTask extends AsyncTask<String, Void, Bookmarks> {
 
    @Override
    protected Bookmarks doInBackground(String... params) {
      String searchString = params[0];
      String userId = params[1];
 
      RestTemplate client = new RestTemplate();
      HashMap<String, Object> args = new HashMap<String, Object>();
      args.put("search", searchString);
      args.put("userId", userId);
      HttpHeaders httpHeaders = new HttpHeaders();
      HttpEntity<Bookmarks> request = new HttpEntity<Bookmarks>(httpHeaders);
      ResponseEntity<Bookmarks> response = client.exchange( //
          BOOKMARK_URL, HttpMethod.GET, request, Bookmarks.class, args);
      Bookmarks bookmarks = response.getBody();
 
      return bookmarks;
    }
 
    @Override
    protected void onPostExecute(Bookmarks result) {
      adapter.updateBookmarks(result);
      bookmarkList.startAnimation(fadeIn);
    }

  }
 
  void bookmarkListItemClicked(Bookmark selectedBookmark) {
    clipboardManager.setText(selectedBookmark.getUrl());
  }
 
}
```

#### After
```java
@Fullscreen
@EActivity(R.layout.bookmarks)
@WindowFeature(Window.FEATURE_NO_TITLE)
public class BookmarksToClipboardActivity extends Activity {

  BookmarkAdapter adapter;

  @ViewById
  ListView bookmarkList;
 
  @ViewById
  EditText search;

  @App
  BookmarkApplication application;

  @RestService
  BookmarkClient restClient;
 
  @AnimationRes
  Animation fadeIn;

  @SystemService
  ClipboardManager clipboardManager;
 
  @AfterViews
  void initBookmarkList() {
    adapter = new BookmarkAdapter(this);
    bookmarkList.setAdapter(adapter);
  }

  @Click({R.id.updateBookmarksButton1, R.id.updateBookmarksButton2})
  void updateBookmarksClicked() {
    searchAsync(search.getText().toString(), application.getUserId());
  }

  @Background
  void searchAsync(String searchString, String userId) {
    Bookmarks bookmarks = restClient.getBookmarks(searchString, userId);
    updateBookmarks(bookmarks);
  }
 
  @UiThread
  void updateBookmarks(Bookmarks bookmarks) {
    adapter.updateBookmarks(bookmarks);
    bookmarkList.startAnimation(fadeIn);
  }

  @ItemClick
  void bookmarkListItemClicked(Bookmark selectedBookmark) {
    clipboardManager.setText(selectedBookmark.getUrl());
  }
 
}
```

```java
@Rest("http://www.bookmarks.com")
public interface BookmarkClient {

  @Get("/bookmarks/{userId}?search={search}")
  Bookmarks getBookmarks(@Path String search, @Path String userId);
 
}
```

--------------------------------------------------
[android-apt]:https://github.com/androidannotations/androidannotations/wiki/Building-Project-Gradle/4c5c653ead0a501be54942acaed3e2cdbac6f81e
