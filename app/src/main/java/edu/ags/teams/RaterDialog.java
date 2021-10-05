package edu.ags.teams;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RaterDialog extends DialogFragment {
    public interface SaveRatingListener {
        void didFinishRaterDialog(float rating);
    }

    public RaterDialog() {


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.dialog_teamrater, container);
        getDialog().setTitle("Rate Team");

        final RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        Button btnSave = view.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                saveRating(rating);
            }
        });

        return view;
    }

    private void saveRating(float rating) {
        //Get the parent activity
        SaveRatingListener activity = (SaveRatingListener) getActivity();
        //Call the method on the parent passing the rating

        activity.didFinishRaterDialog(rating);
        //Get rid of the dialog/close it
        getDialog().dismiss();
    }
}
