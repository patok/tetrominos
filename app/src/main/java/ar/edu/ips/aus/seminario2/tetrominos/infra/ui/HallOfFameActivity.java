package ar.edu.ips.aus.seminario2.tetrominos.infra.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import ar.edu.ips.aus.seminario2.tetrominos.R;
import ar.edu.ips.aus.seminario2.tetrominos.adapter.ScoreViewModel;
import ar.edu.ips.aus.seminario2.tetrominos.infra.ScoreAdapter;

public class HallOfFameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hall_of_fame);

        RecyclerView highestScoresListView = findViewById(R.id.highest_scores);
        highestScoresListView.setLayoutManager(new LinearLayoutManager(this));

        ScoreViewModel model = new ViewModelProvider(this).get(ScoreViewModel.class);
        ScoreAdapter scoreAdapter = new ScoreAdapter(this, model);
        highestScoresListView.setAdapter(scoreAdapter);
    }
}