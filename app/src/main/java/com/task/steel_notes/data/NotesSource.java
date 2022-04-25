package com.task.steel_notes.data;

public interface NotesSource {
    NotesSource init(NotesSourceResponse notesSourceResponse);
    NoteData getNoteData(int position);
    int size();

    void deleteNoteData(int position);
    void updateNoteData(int position, NoteData newNoteData);
    void addNoteData(NoteData newNoteData);
    void clearNoteData();
}
