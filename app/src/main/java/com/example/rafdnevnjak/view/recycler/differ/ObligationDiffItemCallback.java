package com.example.rafdnevnjak.view.recycler.differ;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.rafdnevnjak.model.Obligation;

public class ObligationDiffItemCallback extends DiffUtil.ItemCallback<Obligation> {

    @Override
    public boolean areItemsTheSame(@NonNull Obligation oldItem, @NonNull Obligation newItem) {
        return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Obligation oldItem, @NonNull Obligation newItem) {
        return (oldItem.getStartHour() == newItem.getStartHour() &&
                oldItem.getStartMinute() == newItem.getStartMinute() &&
                oldItem.getEndHour() == newItem.getEndHour() &&
                oldItem.getEndMinute() == newItem.getEndMinute() &&
                oldItem.getObligationSeverity().equals(newItem.getObligationSeverity()) &&
                oldItem.getTitle().equals(newItem.getTitle()) &&
                oldItem.getDescription().equals(newItem.getDescription()));
    }
}
