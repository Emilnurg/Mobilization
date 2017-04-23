package ru.s4nchez.translater.fragments;

import android.util.Log;
import android.widget.Toast;

import ru.s4nchez.translater.model.Words;

public class FragmentFavorite extends FragmentList {


    @Override
    public void setAdapter() {
        Log.d("myLog", "FragmentFavorite: " + Words.get(getContext()).getFavoriteList().size() + "");

        if (mWordAdapter == null) {
            Words.get(getContext()).updateList();
            mWordAdapter = new WordAdapter(Words.get(getContext()).getFavoriteList());
            mRecyclerView.setAdapter(mWordAdapter);
        } else {
            Words.get(getContext()).updateList();
            mWordAdapter.setWords(Words.get(getContext())
                    .getFavoriteList());
            mWordAdapter.notifyDataSetChanged();
        }
    }
}