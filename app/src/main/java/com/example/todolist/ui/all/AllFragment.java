package com.example.todolist.ui.all;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.task.TaskListFragment;
import com.example.todolist.task.TaskRecyclerAdapter;
import com.example.todolist.databinding.FragmentAllBinding;

public class AllFragment extends TaskListFragment {

    protected FragmentAllBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AllViewModel allViewModel =
                new ViewModelProvider(this).get(AllViewModel.class);

        binding = FragmentAllBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MainActivity mainActivity = (MainActivity) root.getContext();
        mainActivity.setListener(this, this, this);

        this.setRecyclerView(root);

        return root;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.setAdapter();
    }

    @Override
    protected void setRecyclerView(View root) {
        myRecyclerView = (RecyclerView) root.findViewById(R.id.allRecyclerView);
        super.setRecyclerView(root);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        openDatabase(context);
        initListFromDatabase();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        closeDatabaseObjects();
    }

    @Override
    protected void setAdapter() {
        mAdapter = new TaskRecyclerAdapter(todayThingsList);
        super.setAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}