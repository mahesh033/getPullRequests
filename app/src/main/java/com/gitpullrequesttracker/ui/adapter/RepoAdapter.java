package com.gitpullrequesttracker.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gitpullrequesttracker.R;
import com.gitpullrequesttracker.model.Repo;

import java.util.List;

/**
 * RepoAdapter
 *
 * @author Mahesh
 */
public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {

    private Context context;
    private List<Repo> repoList;

    public class RepoViewHolder extends RecyclerView.ViewHolder {

        TextView tvId;
        TextView tvName;
        TextView tvUrl;

        public RepoViewHolder(View view) {
            super(view);
            tvId = view.findViewById(R.id.tvId);
            tvName = view.findViewById(R.id.tvName);
            tvUrl = view.findViewById(R.id.tvUrl);
        }
    }


    public RepoAdapter(Context context, List<Repo> repoList) {
        this.context = context;
        this.repoList = repoList;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.repo_list_row, parent, false);

        return new RepoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        Repo repo = repoList.get(position);

        holder.tvId.setText(repo.getId());
        holder.tvName.setText(repo.getTitle());
        holder.tvUrl.setText(repo.getHtmlUrl());

    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }
}
