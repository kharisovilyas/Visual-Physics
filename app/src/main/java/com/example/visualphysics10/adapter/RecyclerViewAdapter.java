package com.example.visualphysics10.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualphysics10.databinding.FragmentItemBinding;
import com.example.visualphysics10.placeholder.PlaceholderContent;
import com.example.visualphysics10.placeholder2.PlaceHolderContent2;
import com.example.visualphysics10.ui.MainFlag;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public List<PlaceholderContent.PlaceHolderItem> mValues;
    public List<PlaceHolderContent2.PlaceHolderItem2> valForTask;
    public boolean mainList;
    private final OnLessonListener onLessonListener;
    View view;

    //TODO: Our Recycler adapter for Placeholder and for PlaceHolder2, created with navigation library

    public RecyclerViewAdapter(List<PlaceholderContent.PlaceHolderItem> items, OnLessonListener onLessonListener) {
        mValues = items;
        mainList = true;
        this.onLessonListener = onLessonListener;
    }
    public RecyclerViewAdapter(List<PlaceHolderContent2.PlaceHolderItem2> items, OnLessonListener onLessonListener, String tag) {
        valForTask = items;
        mainList = false;
        this.onLessonListener = onLessonListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from
                                (parent.getContext()), parent, false), onLessonListener, mainList);

    }

    //insert fields of the ViewHolder class into the recycler
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mainList) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).title);
            holder.imageView.setImageResource(mValues.get(position).imageView);
        }else{
            holder.taskItem = valForTask.get(position);
            holder.mIdView.setText(valForTask.get(position).title);
            holder.imageView.setImageResource(valForTask.get(position).imageView);
        }
    }

    //how amount item (lesson) in Recycler
    @Override
    public int getItemCount() {
        if (mainList) return mValues.size();
        else return valForTask.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mIdView;
        public PlaceholderContent.PlaceHolderItem mItem;
        public PlaceHolderContent2.PlaceHolderItem2 taskItem;
        public int position;
        OnLessonListener onLessonListener;
        public final ImageView imageView;
        public boolean mainList;

        //fields initialization
        public ViewHolder(FragmentItemBinding binding, OnLessonListener onLessonListener, boolean mainList) {
            super(binding.getRoot());
            mIdView = binding.title;
            this.onLessonListener = onLessonListener;
            this.mainList = mainList;
            imageView = binding.imageOfLessons;
            itemView.setOnClickListener(this);
        }

        //position initialization using custom interface and transmits to abstractClass for work with FragmentInfo and FragmentTest
        @Override
        public void onClick(View v) {
            position = getLayoutPosition();
            onLessonListener.onLessonClick(getLayoutPosition());
            if(mainList) MainFlag.setPosition(getLayoutPosition());
        }
    }

    //interface transition from the list to the desired fragment
    public interface OnLessonListener{
        void onLessonClick(int position);
    }
}
