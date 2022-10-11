package ar.edu.ips.aus.seminario2.tetrominos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class GameOverDialog extends DialogFragment {

    private final Activity callingActivity;

    public GameOverDialog(Activity activity) {
        this.callingActivity = activity;
    }

    // TODO TP1 implement restart GameMainActivity
    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.game_over_message)
                .setPositiveButton(R.string.restart_game, (dialog, which) -> {
                    callingActivity.finish();
                })
                .setNegativeButton(R.string.exit_game, (dialog, which) -> {
                    callingActivity.finish();
                });
        return builder.create();
    }
}
