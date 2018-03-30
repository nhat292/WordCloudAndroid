package com.infinitystudios.wordcloud.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.adapters.SimpleAdapter;
import com.infinitystudios.wordcloud.listeners.RecyclerViewItemListener;

import java.util.List;

/**
 * Created by Nhat on 3/29/18.
 */

public class BottomSheetFragmentList extends BottomSheetDialogFragment {

    private RecyclerView recyclerList;
    private List<String> mItems;
    private SimpleAdapter mAdapter;
    private RecyclerViewItemListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mItems = bundle.getStringArrayList("DATA");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        recyclerList = view.findViewById(R.id.recyclerList);

        setupList();

        return view;
    }

    private void setupList() {
        if (mItems != null) {
            mAdapter = new SimpleAdapter(mItems, new RecyclerViewItemListener() {
                @Override
                public void onClick(int position, int type) {
                    if (listener != null) {
                        listener.onClick(position, type);
                    }
                    dismiss();
                }
            });
            recyclerList.setAdapter(mAdapter);
        }
    }

    public void setListener(RecyclerViewItemListener listener) {
        this.listener = listener;
    }
}
