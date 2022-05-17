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
    private boolean mainList;
    private final OnLessonListener onLessonListener;

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
                                (parent.getContext()), parent, false), onLessonListener);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mainList) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).title);
            holder.description.setText(mValues.get(position).task);
            holder.progress.setText(mValues.get(position).progress);
            holder.imageView.setImageResource(mValues.get(position).imageView);
        }else{
            holder.taskItem = valForTask.get(position);
            holder.mIdView.setText(valForTask.get(position).title);
            holder.description.setText(valForTask.get(position).task);
            holder.progress.setText(valForTask.get(position).progress);
            holder.imageView.setImageResource(valForTask.get(position).imageView);
        }
    }

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
        public final TextView description;
        public final TextView progress;
        public PlaceholderContent.PlaceHolderItem mItem;
        public PlaceHolderContent2.PlaceHolderItem2 taskItem;
        public int position;
        OnLessonListener onLessonListener;
        public final ImageView imageView;
        public ViewHolder(FragmentItemBinding binding, OnLessonListener onLessonListener) {
            super(binding.getRoot());
            mIdView = binding.title;
            description = binding.discription;
            progress = binding.progress;
            this.onLessonListener = onLessonListener;
            imageView = binding.imageOfLessons;
            itemView.setOnClickListener(this);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + description.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            position = getLayoutPosition();
            onLessonListener.onLessonClick(getLayoutPosition());
            MainFlag.setPosition(getLayoutPosition());
        }
    }
    public interface OnLessonListener{
        void onLessonClick(int position);
    }
}
