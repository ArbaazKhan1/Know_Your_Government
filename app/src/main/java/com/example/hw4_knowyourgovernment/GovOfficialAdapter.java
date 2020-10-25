package com.example.hw4_knowyourgovernment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GovOfficialAdapter extends RecyclerView.Adapter<GovOfficialViewHolder> {
    private static final String TAG = "GovOfficialAdapter";
    private List<GovOfficial> govOfficialList;
    private MainActivity mainActivity;

    public GovOfficialAdapter(List<GovOfficial> govOfficialList, MainActivity mainActivity) {
        this.govOfficialList = govOfficialList;
        this.mainActivity = mainActivity;
    }

    @Override
    public GovOfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gov_official_list, parent, false);

        itemView.setOnClickListener(mainActivity);
        //itemView.setOnLongClickListener(mainActivity);

        return new GovOfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GovOfficialViewHolder holder, int position) {
        GovOfficial govOfficial = govOfficialList.get(position);

        holder.office.setText(govOfficial.getOffice());
        holder.name.setText(govOfficial.getName());
        holder.party.setText("("+govOfficial.getParty()+")");
    }

    @Override
    public int getItemCount() {
        return govOfficialList.size();
    }
}
