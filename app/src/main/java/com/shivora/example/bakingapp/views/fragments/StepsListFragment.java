package com.kamalmrock.example.bakingapp.views.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kamalmrock.example.bakingapp.R;
import com.kamalmrock.example.bakingapp.adapters.RecipeStepsAdapter;
import com.kamalmrock.example.bakingapp.models.Recipe;
import com.kamalmrock.example.bakingapp.models.RecipeIngredient;
import com.kamalmrock.example.bakingapp.models.RecipeStep;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class StepsListFragment extends Fragment implements RecipeStepsAdapter.OnRecipeStepClickListener {

    public static final String EXTRA_FRAGMENT_RECIPE = "extra_fragment_recipe";

    public StepsListFragment(){}
    public interface OnStepChangedListener{
        void onStepChange(int stepPosition);
    }

    public static final String TAG = StepsListFragment.class.getSimpleName();
    @BindView(R.id.tv_ingredients)
    TextView tvIngredients;
    @BindView(R.id.rv_steps)
    RecyclerView rvSteps;

    RecipeStepsAdapter adapter;
    Recipe mRecipe;
    OnStepChangedListener mOnStepChangedListener;

    public static final StepsListFragment newInstance(Recipe recipe){
        StepsListFragment fragment = new StepsListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FRAGMENT_RECIPE,recipe);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnStepChangedListener = (OnStepChangedListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement OnStepChangedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps_list,container,false);
        ButterKnife.bind(this,rootView);
        mRecipe = getArguments().getParcelable(EXTRA_FRAGMENT_RECIPE);
        getActivity().setTitle(mRecipe.getName());
        for (RecipeIngredient ingredient: mRecipe.getIngredients()){
            tvIngredients.append(ingredient.getQuantity()+" "+ingredient.getMeasure()+" "+ingredient.getIngredient()+"\n");
        }

        adapter =  new RecipeStepsAdapter(mRecipe.getSteps(),this);
        rvSteps.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSteps.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onRecipeStepClicked(int position) {
        Log.i(TAG, "onRecipeStepClicked: "+position);
        mOnStepChangedListener.onStepChange(position);
    }
}
