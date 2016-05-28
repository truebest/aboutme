package shilin.aboutme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import shilin.aboutme.R;
import shilin.aboutme.datas.Skills;
import shilin.aboutme.adapters.SkillsAdapter;
import shilin.aboutme.decorations.DividerItemDecoration;

/**
 * Created by beerko on 09.05.16.
 */
public class SkillsPageFragment extends Fragment {
    private List<Skills> skillsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SkillsAdapter sAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skills,null);

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        sAdapter = new SkillsAdapter(skillsList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sAdapter);


        prepareSkillsData();

        return view;
    }

//    Процедура осуществляет заполнение списка скилов
    private void prepareSkillsData(){
        String[] skillsNameArray = getResources().getStringArray(R.array.my_skills_langs_names);
        String[] skillsDescription = getResources().getStringArray(R.array.my_skills_langs_descriptions);
        String[] skillsStartsArray = getResources().getStringArray(R.array.my_skills_langs_stars);

        for (int i = 0; (i < skillsNameArray.length) && (i < skillsStartsArray.length) ;i++)
        {
            Skills skill = new Skills(skillsNameArray[i],Integer.valueOf(skillsStartsArray[i]),skillsDescription[i]);
            skillsList.add(skill);
        }

        skillsNameArray = getResources().getStringArray(R.array.my_skills_arh_names);
        skillsStartsArray = getResources().getStringArray(R.array.my_skills_arh_stars);
        for (int i = 0; (i < skillsNameArray.length) && (i < skillsStartsArray.length) ;i++)
        {
            Skills skill = new Skills(skillsNameArray[i],Integer.valueOf(skillsStartsArray[i])," ");
            skillsList.add(skill);
        }

        skillsNameArray = getResources().getStringArray(R.array.my_skills_others_names);
        skillsStartsArray = getResources().getStringArray(R.array.my_skills_others_stars);
        for (int i = 0; (i < skillsNameArray.length) && (i < skillsStartsArray.length) ;i++)
        {
            Skills skill = new Skills(skillsNameArray[i],Integer.valueOf(skillsStartsArray[i])," ");
            skillsList.add(skill);
        }

        sAdapter.notifyDataSetChanged();
    }

}
