package edu.ags.teams;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter {

    private static final String TAG = "myDebug";
    private ArrayList<Team> teamData;
    private View.OnClickListener onClickListener;
    private Context parentContext;


    public class TeamViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewDescription;
        private TextView textViewCity;
        private ImageButton imageButtonPhoto;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.txtDescription);
            textViewCity = itemView.findViewById(R.id.txtCity);
            imageButtonPhoto = itemView.findViewById(R.id.imgPhoto);
            itemView.setTag(this);
            itemView.setOnClickListener(onClickListener);
            Log.d(TAG, "TeamViewHolder: ");

        }

        public TextView getTextViewDescription(){return textViewDescription;}
        public TextView getTextViewCity(){return textViewCity;}
        public ImageButton getImageButtonPhoto(){return imageButtonPhoto;}
    }

    public  TeamAdapter(ArrayList<Team> arrayList, Context context){
        teamData = arrayList;
        parentContext = context;
        Log.d(TAG, "TeamAdapter: " + arrayList.size());
    }

    public void setOnClickListener(View.OnClickListener itemClickListener)
    {
        onClickListener = itemClickListener;
        Log.d(TAG, "setOnClickListener: TeamAdapter");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //retrieve and inflate the list_item xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TeamViewHolder teamViewHolder = (TeamViewHolder) holder;
        Team team = teamData.get(position);


        //Bind to the screen
        teamViewHolder.getTextViewDescription().setText(team.Description);
        teamViewHolder.getTextViewCity().setText(team.City);
        teamViewHolder.getImageButtonPhoto().setImageResource(team.ImgId);

        Log.d(TAG, "onBindViewHolder: " + team);
    }

    @Override
    public int getItemCount() {
        return teamData.size();
    }
}
