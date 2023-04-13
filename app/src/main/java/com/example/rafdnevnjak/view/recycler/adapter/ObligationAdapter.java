package com.example.rafdnevnjak.view.recycler.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rafdnevnjak.R;
import com.example.rafdnevnjak.model.Date;
import com.example.rafdnevnjak.model.Obligation;
import com.example.rafdnevnjak.view.activities.DetailedObligationActivity;
import com.example.rafdnevnjak.view.activities.MainActivity;
import com.example.rafdnevnjak.view.activities.ObligationActivity;
import com.example.rafdnevnjak.view.fragments.DailyPlanFragment;

public class ObligationAdapter extends ListAdapter<Obligation, ObligationAdapter.ObligationViewHolder> {

    public ObligationAdapter(@NonNull DiffUtil.ItemCallback<Obligation> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ObligationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obligation_item, parent, false);
        return new ObligationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObligationViewHolder holder, int position) {
        Obligation obligation = getItem(position);
        holder.bind(obligation);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailedObligationActivity.class);
            Obligation obligation1 = getCurrentList().get(holder.getAdapterPosition());
            intent.putExtra("obligation", getCurrentList().get(holder.getAdapterPosition()));
            intent.putExtra("title", ((MainActivity)holder.itemView.getContext()).getTitle());

            holder.itemView.getContext().startActivity(intent);
        });

        holder.getDelete().setOnClickListener(v -> {
            DailyPlanFragment.deleteObligation(((MainActivity)holder.itemView.getContext()).getTitle().toString(), obligation, ((MainActivity) holder.itemView.getContext()).findViewById(R.id.recyclerViewDailyPlan));
        });

        holder.getEdit().setOnClickListener(v -> {
            Intent intent = new Intent(((MainActivity)holder.itemView.getContext()), ObligationActivity.class);
            intent.putExtra("title", ((MainActivity)holder.itemView.getContext()).getTitle().toString());
            intent.putExtra("edit", true);
            intent.putExtra("oldObligation", obligation);
            ((MainActivity)holder.itemView.getContext()).startActivityForResult(intent, 1);
        });
    }

    public static class ObligationViewHolder extends RecyclerView.ViewHolder {
        private TextView time;
        private TextView title;
        private ImageView image;

        private ImageView delete;

        private ImageView edit;

        public ObligationViewHolder(@NonNull View itemView) {
            super(itemView);

            //Get view components of the recycler view item so we can edit them
            time = itemView.findViewById(R.id.obligationTime);
            title = itemView.findViewById(R.id.obligationTitle);
            image = itemView.findViewById(R.id.obligationSeverity);

        }

        @SuppressLint("DefaultLocale")
        public void bind(Obligation obligation){
            //Edit the view components
            time.setText(new String(String.format("%02d", obligation.getStartHour()) + ":" + String.format("%02d", obligation.getStartMinute())
                    + " - " + String.format("%02d", obligation.getEndHour()) + ":" + String.format("%02d", obligation.getEndMinute())));

            title.setText(obligation.getTitle());

            if (obligation.getObligationSeverity().equals(Obligation.ObligationSeverity.HIGH)) {
                image.setImageResource(R.drawable.task_high);
            }
            else if (obligation.getObligationSeverity().equals(Obligation.ObligationSeverity.MID)){
                image.setImageResource(R.drawable.task_mid);
            }
            else {
                image.setImageResource(R.drawable.task_low);
            }
        }

        public ImageView getDelete() {
            delete = itemView.findViewById(R.id.obligationDeleteIcon);
            return delete;
        }

        public ImageView getEdit() {
            edit = itemView.findViewById(R.id.obligationEditIcon);
            return edit;
        }
    }
}
