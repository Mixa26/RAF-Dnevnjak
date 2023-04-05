package com.example.rafdnevnjak.view.recycler.differ;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.rafdnevnjak.model.Date;

public class DateDiffItemCallback extends DiffUtil.ItemCallback<Date> {

    @Override
    public boolean areItemsTheSame(@NonNull Date oldItem, @NonNull Date newItem) {
        return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Date oldItem, @NonNull Date newItem) {
        return oldItem.getDate().equals(newItem.getDate());
    }
}
