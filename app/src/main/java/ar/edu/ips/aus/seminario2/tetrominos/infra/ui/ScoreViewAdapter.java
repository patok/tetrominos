package ar.edu.ips.aus.seminario2.tetrominos.infra.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ips.aus.seminario2.tetrominos.R;
import ar.edu.ips.aus.seminario2.tetrominos.adapter.ScoreViewModel;
import ar.edu.ips.aus.seminario2.tetrominos.domain.Score;
import ar.edu.ips.aus.seminario2.tetrominos.infra.data.ScoreEntity;

public class ScoreViewAdapter extends RecyclerView.Adapter<ScoreViewAdapter.ViewHolder> {

    private final Context context;
    private List<Score> data;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.score_item);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public ScoreViewAdapter(@NonNull Context context, @NonNull ScoreViewModel model) {
        this.context = context;
        this.data = new ArrayList<>();
        final Observer<List<ScoreEntity>> scoreObserver = new Observer<>() {
            @Override
            public void onChanged(List<ScoreEntity> scores) {
                ScoreViewAdapter.this.data.addAll(scores);
                ScoreViewAdapter.this.notifyDataSetChanged();
            }
        };
        model.getHighestScores().observe((LifecycleOwner) context, scoreObserver);
    }

    /**
     * Create new views (invoked by the recyclerView)
     * @param parent
     * @param viewType
     * @return new view
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_row_item, parent, false);

        return new ViewHolder(v);
    }

    /**
     * Replace the contents of a view (invoked by the recyclerView)
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(String.format("%s : %s",
                data.get(position).getPlayerName(),
                data.get(position).getPoints()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
