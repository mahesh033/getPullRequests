package com.gitpullrequesttracker.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gitpullrequesttracker.R;
import com.gitpullrequesttracker.model.Repo;
import com.gitpullrequesttracker.network.ApiClient;
import com.gitpullrequesttracker.network.ApiService;
import com.gitpullrequesttracker.ui.adapter.RepoAdapter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * MainActivity
 *
 * @author sutharsha
 */
public class MainActivity extends AppCompatActivity {

    //
    TextInputEditText userName;
    TextInputEditText repoName;
    TextInputLayout userNameLayout;
    TextInputLayout repoNameLayout;
    RecyclerView repoRecyclerView;
    AppCompatButton getRepoBtn;
    ProgressBar progressView;
    //
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = ApiClient.getClient().create(ApiService.class);

        userName = findViewById(R.id.user_id_input);
        repoName = findViewById(R.id.user_repo_input);
        userNameLayout = findViewById(R.id.user_id_input_layout);
        repoNameLayout = findViewById(R.id.user_repo_input_layout);
        repoRecyclerView = findViewById(R.id.repoRecyclerView);
        progressView = findViewById(R.id.progressView);
        getRepoBtn = findViewById(R.id.getPullRequestBtn);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        repoRecyclerView.setLayoutManager(mLayoutManager);
        repoRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getRepoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInput()) {

                    hideKeyboard();
                    progressView.setVisibility(View.VISIBLE);
                    getAllPublicRepos(userName.getText().toString(), repoName.getText().toString());
                }
            }
        });

        repoName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (isValidInput()) {
                        progressView.setVisibility(View.VISIBLE);
                        getAllPublicRepos(userName.getText().toString(), repoName.getText().toString());
                    }
                }
                return false;
            }
        });
    }

    //to hide soft keypad
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    //call api using retrofit and rxJava
    private void getAllPublicRepos(@NonNull String userName, @NonNull String repoName) {
        disposable.add(
                apiService.fetchAllRepos(userName, repoName)
                          .subscribeOn(Schedulers.io())
                          .observeOn(AndroidSchedulers.mainThread())
                          .subscribeWith(new DisposableSingleObserver<List<Repo>>() {
                              @Override
                              public void onSuccess(List<Repo> repos) {
                                  setUpRepoList(repos);
                              }

                              @Override
                              public void onError(Throwable e) {
                                  progressView.setVisibility(View.GONE);
                                  Toast.makeText(MainActivity.this, "Something went wrong! Please enter valid user name or repo name", Toast.LENGTH_LONG)
                                       .show();
                              }
                          })
        );
    }

    //setup list
    private void setUpRepoList(@NonNull List<Repo> repos) {
        userNameLayout.setVisibility(View.GONE);
        repoNameLayout.setVisibility(View.GONE);
        getRepoBtn.setVisibility(View.GONE);
        repoRecyclerView.setVisibility(View.VISIBLE);

        RepoAdapter mAdapter = new RepoAdapter(this, repos);
        repoRecyclerView.setAdapter(mAdapter);

        progressView.setVisibility(View.GONE);
    }

    //validate all required fields
    private boolean isValidInput() {
        if (TextUtils.isEmpty(userName.getText())) {
            userName.setError("Required");
            return false;
        } else if (TextUtils.isEmpty(repoName.getText())) {
            repoName.setError("Required");
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if (repoRecyclerView.getVisibility() == View.VISIBLE) {
            userNameLayout.setVisibility(View.VISIBLE);
            repoNameLayout.setVisibility(View.VISIBLE);
            getRepoBtn.setVisibility(View.VISIBLE);
            repoRecyclerView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}