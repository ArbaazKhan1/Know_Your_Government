package com.example.hw4_knowyourgovernment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class GovOfficialViewHolder extends RecyclerView.ViewHolder {

    TextView office;
    TextView name;
    TextView party;

    public GovOfficialViewHolder(@NonNull View itemView) {
        super(itemView);
        office = itemView.findViewById(R.id.officeTextView);
        name = itemView.findViewById(R.id.nameTextView);
        party = itemView.findViewById(R.id.partyTextView);
    }
}
