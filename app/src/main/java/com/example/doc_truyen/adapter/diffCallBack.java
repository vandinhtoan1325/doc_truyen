package com.example.doc_truyen.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.example.doc_truyen.model.Comic;

import java.util.ArrayList;

public class diffCallBack extends DiffUtil.Callback {
    private ArrayList<Comic> oldList;
    private ArrayList<Comic> newList;

    diffCallBack(ArrayList<Comic> newList, ArrayList<Comic> oldList){
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }
}
