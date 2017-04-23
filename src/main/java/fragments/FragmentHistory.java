package ru.s4nchez.translater.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.s4nchez.translater.R;
import ru.s4nchez.translater.model.Word;
import ru.s4nchez.translater.model.Words;

public class FragmentHistory extends FragmentList {


    @Override
    public void setAdapter() {
        Log.d("myLog", "FragmentHistory: " + Words.get(getContext()).getList().size() + "");

        if (mWordAdapter == null) {
            mWordAdapter = new WordAdapter(Words.get(getContext()).getList());
            mRecyclerView.setAdapter(mWordAdapter);
        } else {
            Words.get(getActivity()).updateList();
            mWordAdapter.notifyDataSetChanged();
        }
    }
}
