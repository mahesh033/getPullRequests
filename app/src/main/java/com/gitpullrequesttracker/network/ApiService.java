package com.gitpullrequesttracker.network;

import com.gitpullrequesttracker.model.Repo;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * ApiService
 *
 * @author Mahesh
 */
public interface ApiService {

    // Fetch all repos
    @GET("repos/{userName}/{repoName}/pulls?state=active")
    Single<List<Repo>> fetchAllRepos(@Path("userName") String userName, @Path("repoName") String repoName);
}
