package com.example.listgraphql;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.SearchQuery;
import com.example.listgraphql.adapters.UBahnAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewBahnFragment extends Fragment {

    private static final String TAG = "RecyclerViewBahnFragment";

    protected RecyclerView mRecyclerView;
    protected UBahnAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    protected List<SearchQuery.Station> listData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listData = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_bahn_frag, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    public void search(String searchTerm, View loadingView, TextView resultText){

        if(mAdapter == null){
            mAdapter = new UBahnAdapter(getActivity(), listData, loadingView, resultText);
            mRecyclerView.setAdapter(mAdapter);
        }

        mAdapter.clearData();

        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl(Constants.SERVER)
                .build();

        apolloClient.query(new SearchQuery(searchTerm))
                .enqueue(new ApolloCall.Callback<SearchQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<SearchQuery.Data> response) {
                        Log.v("Apollo", "Launch site: " + response.getData());
                        mAdapter.refreshData(response.getData().search().stations(), "");
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("Apollo", "Error", e);
                        mAdapter.refreshData(new ArrayList<>(), e.getCause().getLocalizedMessage());
                    }
                });
    }
}