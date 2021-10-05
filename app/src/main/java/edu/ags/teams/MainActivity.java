package edu.ags.teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RaterDialog.SaveRatingListener{

    public static final String TAG = "myDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: ");
       initListButton();
       initMapButton();
       initSettingsButton();
       initRatingButton();
       setForEditing(false);
       initToggleButton();

        this.setTitle("Mainactivity");
    }

    private void initToggleButton() {
        ToggleButton toggleButtonEdit = findViewById(R.id.toggleButtonEdit);

        toggleButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForEditing(toggleButtonEdit.isChecked());
            }
        });
    }

    private void setForEditing(boolean enabled) {
        EditText etName = findViewById(R.id.etName);
        EditText etCity = findViewById(R.id.etCity);
        Button btnRating = findViewById(R.id.btnRating);
        EditText editCell = findViewById(R.id.editCell);


        etName.setEnabled(enabled);
        etCity.setEnabled(enabled);
        btnRating.setEnabled(enabled);
        editCell.setEnabled(enabled);

        if(enabled) {
            etName.requestFocus();
            editCell.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        else
        {
            ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            editCell.setInputType(InputType.TYPE_NULL);
        }
    }


    private void initRatingButton() {
        Button btnRating = findViewById(R.id.btnRating);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                RaterDialog raterDialog = new RaterDialog();
                Log.d(TAG, "onClick: Before Show");
                raterDialog.show(fragmentManager, "Rate Team");
                Log.d(TAG, "onClick: AfterShow");
            }
        });
    }

    private void initSettingsButton() {

        ImageButton ibList = findViewById(R.id.imageButtonSettings);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the list activity
                Intent intent = new Intent(MainActivity.this, TeamSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void initMapButton() {

        ImageButton ibList = findViewById(R.id.imageButtonMap);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the list activity
                Intent intent = new Intent(MainActivity.this, TeamMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the list activity
                Intent intent = new Intent(MainActivity.this, TeamListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    @Override
    public void didFinishRaterDialog(float rating) {
        TextView textView = findViewById(R.id.txtRating);
        textView.setText(String.valueOf(rating));
    }
}