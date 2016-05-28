package shilin.aboutme.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import shilin.aboutme.R;
import shilin.aboutme.datas.Skills;


/**
 * Created by beerko on 09.05.16.
 */
public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.MyViewHolder> {

    private List<Skills> skillsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description;
        public RatingBar rating;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.skill_name);
            description = (TextView) view.findViewById(R.id.skill_description);
            rating = (RatingBar) view.findViewById(R.id.skill_rating);
        }
    }

    public SkillsAdapter(List<Skills> skillsList) {
        this.skillsList = skillsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Skills skills = skillsList.get(position);
        holder.name.setText(skills.getName());
        holder.description.setText(skills.getDescription());
        holder.rating.setRating(skills.getStars());
    }

    @Override
    public int getItemCount() {
        return skillsList.size();
    }

}
