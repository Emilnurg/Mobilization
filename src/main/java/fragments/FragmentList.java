package ru.s4nchez.translater.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.s4nchez.translater.R;
import ru.s4nchez.translater.Utils;
import ru.s4nchez.translater.model.Word;
import ru.s4nchez.translater.model.Words;

public abstract class FragmentList extends Fragment {

    protected RecyclerView mRecyclerView;
    protected WordAdapter mWordAdapter;


    public abstract void setAdapter();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerVIew);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setAdapter();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mRecyclerView != null) {
                setAdapter();
            }
        }
    }

    private void showDeleteDialog(final Word word) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle(word.getSourceWord());
        dialog.setMessage(R.string.deleteQuestion);
        dialog.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Words.get(getActivity()).deleteWord(word);
                setAdapter();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public class WordHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        private Word mWord;

        private TextView mFrom;
        private TextView mTo;
        private TextView mCodes;
        private ImageView mFavorite;


        public WordHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mFrom = (TextView) itemView.findViewById(R.id.textViewFrom);
            mTo = (TextView) itemView.findViewById(R.id.textViewTo);
            mCodes = (TextView) itemView.findViewById(R.id.textViewCodes);
            mFavorite = (ImageView) itemView.findViewById(R.id.imageViewIcon);

            mFavorite.setOnClickListener(this);
        }

        public void bindWord(Word word) {
            mWord = word;
            mFrom.setText(word.getSourceWord());
            mTo.setText(word.getTranslatedWord());
            mCodes.setText(word.getLangCodes());
            showFavorite();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageViewIcon:
                    mWord.setFavorite(!mWord.isFavorite());
                    Words.get(getActivity()).updateWord(mWord);
                    showFavorite();
                    break;

                default:
                    Utils.copyToBuffer(getActivity(), mWord.getTranslatedWord());
                    Toast.makeText(getActivity(), R.string.copyToBuffer,
                            Toast.LENGTH_SHORT).show();

            }
        }

        private void showFavorite() {
            if (mWord.isFavorite()) {
                mFavorite.setImageResource(R.drawable.favorite_on);
            } else {
                mFavorite.setImageResource(R.drawable.favorite_off);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            showDeleteDialog(mWord);
            return true;
        }
    }

    public class WordAdapter extends RecyclerView.Adapter<WordHolder> {

        private List<Word> mWords;


        public WordAdapter(List<Word> words) {
            mWords = words;
        }

        public void setWords(List<Word> words) {
            mWords = words;
        }

        @Override
        public WordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item, parent, false);
            return new WordHolder(view);
        }

        @Override
        public void onBindViewHolder(WordHolder holder, int position) {
            Word word = mWords.get(position);
            holder.bindWord(word);
        }

        @Override
        public int getItemCount() {
            return mWords.size();
        }
    }
}