package edu.ags.teams;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter {
    private static final String TAG = "myDebug";
    private ArrayList<Team> teamData;
    private View.OnClickListener onClickListener;
    private Context parentContext;
    private boolean isDeleting;

    public void setDelete(boolean status) {
        isDeleting = status;
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewDescription;
        private TextView textViewCity;
        private ImageButton imageButtonPhoto;
        private CheckBox chkFavorite;
        private Button btnDelete;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.txtDescription);
            textViewCity = itemView.findViewById(R.id.txtCity);
            imageButtonPhoto = itemView.findViewById(R.id.imgPhoto);
            chkFavorite = itemView.findViewById(R.id.chkFavorite);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            itemView.setTag(this);
            itemView.setOnClickListener(onClickListener);
        }

        public TextView getTextViewDescription() { return textViewDescription; }
        public TextView getTextViewCity() {return textViewCity;}
        public ImageButton getImageButtonPhoto() {return imageButtonPhoto;}
        public CheckBox getChkFavorite() {return chkFavorite;}
        public Button getBtnDelete() {return btnDelete;}
    }

    public TeamAdapter(ArrayList<Team> arrayList, Context context){
        teamData = arrayList;
        parentContext = context;
        //Log.d(TAG, "TeamAdapter: " + arrayList.size());
    }

    public void setOnClickListener(View.OnClickListener itemClickListener)
    {
        //Log.d(TAG, "setOnClickListener: ");
        onClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // retrieve and inflate the list_item.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false );
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TeamViewHolder teamViewHolder = (TeamViewHolder) holder;

        Team team = teamData.get(position);

        // Bind to the screen
        teamViewHolder.getTextViewDescription().setText(team.getName());
        teamViewHolder.getTextViewCity().setText(team.getCity());
        teamViewHolder.getImageButtonPhoto().setImageResource(team.getImgId());
        teamViewHolder.getChkFavorite().setChecked(team.getFavorite());

        //Log.d(TAG, "onBindViewHolder: " + team);

        if(isDeleting) {
            //Log.d(TAG, "onBindViewHolder: Deleting: " + isDeleting);
            teamViewHolder.getBtnDelete().setVisibility(View.VISIBLE);
            teamViewHolder.getBtnDelete().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deleteItem(position, team.getId());
                }
            });
        }
        else
        {
            //Log.d(TAG, "onBindViewHolder: Not Deleting: " + isDeleting);
            teamViewHolder.getBtnDelete().setVisibility(View.INVISIBLE);
        }

        teamViewHolder.getChkFavorite().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG, "onCheckedChanged: " + team.getName() + ":" + b);
                team.setFavorite(b);
                WriteToTextFile();
            }
        });
    }

    private void deleteItem(int position, int id) {
        // Remove it from the teamData
        teamData.remove(position);

        // Write the file.
        // WriteToTextFile();

        TeamDataSource ds = new TeamDataSource(parentContext);
        try{
            ds.open();
            boolean result = ds.delete(id);
            Log.d(TAG, "deleteItem: Delete Team: " + id);
        }
        catch(Exception ex)
        {
            Log.d(TAG, "deleteItem: " + ex.getMessage());
        }
        // Rebind
        notifyDataSetChanged();
    }

    private void WriteToTextFile()
    {
        FileIO fileIO = new FileIO();
        Integer counter = 0;
        String[] data = new String[teamData.size()];
        for (Team t : teamData) data[counter++] = t.toString();
        fileIO.writeFile((AppCompatActivity) parentContext, data);
    }



    @Override
    public int getItemCount() {
        return teamData.size();
    }
}
