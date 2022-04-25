package com.task.steel_notes.observe;

import com.task.steel_notes.data.NoteData;

public interface Observer {
    void updateState(NoteData noteData);
}
