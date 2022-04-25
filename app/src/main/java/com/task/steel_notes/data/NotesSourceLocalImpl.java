package com.task.steel_notes.data;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.task.steel_notes.R;

public class NotesSourceLocalImpl implements NotesSource {
    private List<NoteData> dataSource;
    private Resources resources;    // ресурсы приложения

    public NotesSourceLocalImpl(Resources resources) {
        dataSource = new ArrayList<>(7);
        this.resources = resources;
    }



    public NotesSource init(NotesSourceResponse notesSourceResponse) {
        // строки заголовков из ресурсов
        String[] titles = resources.getStringArray(R.array.titles);
        // строки описаний из ресурсов
        String[] descriptions = resources.getStringArray(R.array.descriptions);
        // изображения
        int[] pictures = getImageArray();
        // заполнение источника данных
        for (int i = 0; i < descriptions.length; i++) {
            dataSource.add(new NoteData(titles[i], descriptions[i], pictures[i], false,
                    Calendar.getInstance().getTime()));
        }

        if(notesSourceResponse !=null){
            notesSourceResponse.initialized(this);
        }
        return this;
    }

    // Механизм вытаскивания идентификаторов картинок
    // https://stackoverflow.com/questions/5347107/creating-integer-array-of-resource-ids
    private int[] getImageArray(){
        TypedArray pictures = resources.obtainTypedArray(R.array.pictures);
        int length = pictures.length();
        int[] answer = new int[length];
        for(int i = 0; i < length; i++){
            answer[i] = pictures.getResourceId(i, 0);
        }
        return answer;
    }


    public NoteData getNoteData(int position) {
        return dataSource.get(position);
    }

    public int size(){
        return dataSource.size();
    }

    @Override
    public void deleteNoteData(int position) {
        dataSource.remove(position);
    }

    @Override
    public void updateNoteData(int position, NoteData newNoteData) {
        dataSource.set(position, newNoteData);
    }

    @Override
    public void addNoteData(NoteData newNoteData) {
        dataSource.add(newNoteData);
    }

    @Override
    public void clearNoteData() {
        dataSource.clear();
    }
}
