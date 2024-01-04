package com.example.todolist.ui.today;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.TaskDatabaseHelper;
import com.example.todolist.DateTime;
import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.task.Task;
import com.example.todolist.task.TaskListFragment;
import com.example.todolist.task.TaskRecyclerAdapter;
import com.example.todolist.databinding.FragmentTodayBinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class TodayFragment extends TaskListFragment {

    protected FragmentTodayBinding binding;
    protected ArrayList<Task> todayList = new ArrayList<Task>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TodayViewModel todayViewModel =
                new ViewModelProvider(this).get(TodayViewModel.class);
        binding = FragmentTodayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MainActivity mainActivity = (MainActivity) root.getContext();
        mainActivity.setListener(this, this, this);

        setRecyclerView(root);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTodayList();
        setAdapter();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        openDatabase(context);
        initListFromDatabase();
    }

    @Override
    protected void setRecyclerView(View root) {
        myRecyclerView = (RecyclerView) root.findViewById(R.id.todayRecyclerView);
        super.setRecyclerView(root);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        closeDatabaseObjects();
    }

    @Override
    protected void setAdapter() {
        mAdapter = new TaskRecyclerAdapter(todayList);
        super.setAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initTodayList() {
        todayList.clear();
        initListFromDatabase();
        for (Task t : todayThingsList) {
            Date date = t.getDate();
            if (DateTime.checkIsTomorrow(date)) {
                todayList.add(t);
            }
        }
        todayList.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return Long.compare(o1.getDate().getTime(), o2.getDate().getTime());
            }
        });
    }

    @Override
    public void add(String task, String description, String dateTime) {
        super.add(task, description, dateTime);
        Task newTask = new Task(cursor.getInt(0), task, description, DateTime.fromStringToDate(dateTime));
        todayList.add(newTask);
        if(mAdapter!=null) {
            mAdapter.notifyItemInserted(todayList.size() - 1);
        }
    }

    @Override
    public void remove(int position) {
        Task removableTask = todayList.get(position);
        int id = removableTask.getId();
        todayList.remove(position);
        mAdapter.notifyItemRemoved(position);
        database.delete(TaskDatabaseHelper.TABLE, "id = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public void edit(int pos, String name, String description, String date) {
        Task oldTask  = todayList.get(pos);
        int id = oldTask.getId();
        Task newTask  = new Task(id, name, description, DateTime.fromStringToDate(date));
        ArrayList<Task> taskList = todayThingsList;
        int realPosition = -1;
        for (int i = 0; i < taskList.size(); i++) {
            Task t = taskList.get(i);
            if (t.getId() == id) {
                realPosition = i;
            }
        }
        if (realPosition == -1)
            Log.d("Error", "Id wasn't found");
        todayList.set(pos, newTask);
        todayThingsList.set(realPosition, newTask);
        mAdapter.notifyItemChanged(pos);
        editDatabaseValue(name, description, date, id);
    }
}