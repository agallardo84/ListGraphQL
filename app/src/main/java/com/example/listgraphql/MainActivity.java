package com.example.listgraphql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected EditText resultsText;
    protected EditText editTextSearch;
    RecyclerViewBahnFragment fragment;

    View loadingView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingView = findViewById(R.id.loading_view);
        progressBar = findViewById(R.id.progressBar);
        editTextSearch = findViewById(R.id.editTextSearch);

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if(fragment !=null && fragment.isVisible()) {
                        fragment.search(editTextSearch.getText().toString(), loadingView, resultsText);
                        if (editTextSearch != null){
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = new RecyclerViewBahnFragment();
            transaction.replace(R.id.result_content_fragment, fragment);
            transaction.commit();
        }

    }

}