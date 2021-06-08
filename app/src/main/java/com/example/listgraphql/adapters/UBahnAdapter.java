package com.example.listgraphql.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.SearchQuery;
import com.example.listgraphql.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UBahnAdapter extends RecyclerView.Adapter<UBahnAdapter.ViewHolder> {

    private Activity activity;
    private List<SearchQuery.Station> localData;
    View loadingView;
    TextView resultsText;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textNameView;
        private final TextView textLatLonView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textNameView = (TextView) view.findViewById(R.id.name);
            textLatLonView = (TextView) view.findViewById(R.id.latlon);
            imageView = (ImageView) view.findViewById(R.id.imageStation);
        }

        public TextView getNameView() {
            return textNameView;
        }

        public TextView getLatLonView() {
            return textLatLonView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public UBahnAdapter(Activity activity, List<SearchQuery.Station> listData, View loadingView, TextView resultsText) {
        this.activity = activity;
        this.loadingView = loadingView;
        this.resultsText = resultsText;
        localData = listData;
    }

    public void clearData(){
        localData = new ArrayList<>();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
                if(loadingView == null)
                    loadingView = activity.findViewById(R.id.loading_view);
                if(resultsText == null)
                    resultsText = activity.findViewById(R.id.resutls_text);

                loadingView.setVisibility(View.VISIBLE);
                resultsText.setVisibility(View.GONE);
            }
        });
    }

    public void refreshData(List<SearchQuery.Station> listData, String msg){
        localData = listData;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
                if(msg.isEmpty()){
                    resultsText.setText(localData.size()+" "+activity.getString(R.string.results_found));
                }else{
                    resultsText.setText(msg);
                }
                resultsText.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
            }
        });
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        SearchQuery.Station item = localData.get(position);
        viewHolder.getNameView().setText(item.name());
        String latLon = activity.getText(R.string.lat) + " " + item.location().latitude()+" "+activity.getText(R.string.lon)+" "+item.location().longitude();
        viewHolder.getLatLonView().setText(latLon);
        if(item.picture() != null && item.picture().url() != null) {
            Picasso.with(activity).load(item.picture().url()).fit().centerCrop()
                    .placeholder(R.drawable.loading).into(viewHolder.getImageView());

        }else{
            Picasso.with(activity).load(R.drawable.no_image).fit().centerCrop()
                    .placeholder(R.drawable.loading).into(viewHolder.getImageView());
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localData.size();
    }
}