package com.example.rafdnevnjak.view.recycler.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rafdnevnjak.R;
import com.example.rafdnevnjak.model.Date;
import com.example.rafdnevnjak.view.activities.MainActivity;

public class MonthAdapter extends ListAdapter<Date, MonthAdapter.DayViewHolder> {

    private final Consumer<Date> onDateClicked;

    public MonthAdapter(@NonNull DiffUtil.ItemCallback<Date> diffCallback, Consumer<Date> onDateClicked) {
        super(diffCallback);
        this.onDateClicked = onDateClicked;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        return new DayViewHolder(view, position -> {
            Date date = getItem(position);
            onDateClicked.accept(date);
        });
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Date date = getItem(position);

        holder.bind(date);

        //Open the DailyPlanFragment when a date is selected
        holder.itemView.setOnClickListener(v -> {
            ((MainActivity)holder.itemView.getContext()).openDailyPlanMenu(date);
            ((MainActivity)holder.itemView.getContext()).setDateViewSelected(holder);
        });
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {

        private TextView dayOfWeek;
        private TextView dateOfMonth;

        public DayViewHolder(@NonNull View itemView, Consumer<Integer> onItemClicked) {
            super(itemView);

            itemView.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClicked.accept(getAdapterPosition());
                }
            });

            //Get the view components for the day and date so we can set them later
            dayOfWeek = itemView.findViewById(R.id.dayOfWeek);
            dateOfMonth = itemView.findViewById(R.id.dateOfMonth);
        }

        public void bind(Date date){
            //Set the day of week text and date text
            dayOfWeek.setText(date.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            dateOfMonth.setText(String.valueOf(date.getDate().getDayOfMonth()));
            setColor(((MainActivity)itemView.getContext()).getColorOfDayInCalendar(date));
        }

        public void setColor(int severity){
            if (severity == 0) {
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.white));
            }
            else if (severity == 1) {
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.low_severity));
            }
            else if (severity == 2) {
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.mid_severity));
            }
            else{
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.high_severity));
            }
        }
    }
}
