package com.task.steel_notes.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.task.steel_notes.MainActivity;
import com.task.steel_notes.Navigation;
import com.task.steel_notes.R;
import com.task.steel_notes.data.NoteData;
import com.task.steel_notes.data.NotesSource;
import com.task.steel_notes.data.NotesSourceLocalImpl;
import com.task.steel_notes.data.NotesSourceRemoteImpl;
import com.task.steel_notes.data.NotesSourceResponse;
import com.task.steel_notes.observe.Observer;
import com.task.steel_notes.observe.Publisher;

public class NoteFragment extends Fragment {

    private NotesSource data;
    private NoteAdapter adapter;
    private RecyclerView recyclerView;

    private Navigation navigation;
    private Publisher publisher;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_lines);
        initRecyclerView(recyclerView, data);

        if (false) {
            data = new NotesSourceLocalImpl(getResources()).init(new NotesSourceResponse() {
                @Override
                public void initialized(NotesSource notesSource) {
                }
            });
        } else {
            data = new NotesSourceRemoteImpl().init(new NotesSourceResponse() {
                @Override
                public void initialized(NotesSource notesSource) {
                    adapter.notifyDataSetChanged();
                }
            });
        }
        adapter.setDataSource(data);
        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView, NotesSource data) {

        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);

        // работа со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        adapter = new NoteAdapter(this);
        recyclerView.setAdapter(adapter);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(700);
        defaultItemAnimator.setChangeDuration(700);
        defaultItemAnimator.setRemoveDuration(700);
        recyclerView.setItemAnimator(defaultItemAnimator);

        //  разделитель карточек
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        adapter.SetOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                navigation.addFragment(NoteUpdateFragment.newInstance(), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateState(NoteData noteData) {
                        data.addNoteData(noteData);
                        adapter.notifyItemInserted(data.size() - 1);
                    }
                });
                return true;
            case R.id.action_clear:
                data.clearNoteData();
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.note_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getMenuContextClickPosition();
        switch (item.getItemId()) {
            case R.id.action_update:
                navigation.addFragment(NoteUpdateFragment.newInstance(data.getNoteData(position)), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateState(NoteData noteData) {
                        data.updateNoteData(position, noteData);
                        adapter.notifyItemChanged(position);
                    }
                });
                return true;
            case R.id.action_delete:
                data.deleteNoteData(position);
                adapter.notifyItemRemoved(position);
                return true;
        }

        return super.onContextItemSelected(item);
    }
}