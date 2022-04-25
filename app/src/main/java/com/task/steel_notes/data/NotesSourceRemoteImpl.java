package com.task.steel_notes.data;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

public class NotesSourceRemoteImpl implements NotesSource {

    private static String NOTES_COLLECTION = "cards";
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = store.collection(NOTES_COLLECTION);
    private List<NoteData> notesData =new ArrayList<NoteData>();

    @Override
    public NotesSource init(NotesSourceResponse notesSourceResponse) {
        collectionReference.orderBy(NoteDataTranslate.Fields.DATE, Query.Direction.DESCENDING).get(Source.SERVER)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            notesData =new ArrayList<NoteData>();
                            for (QueryDocumentSnapshot docFields: task.getResult()) {
                                NoteData noteData = NoteDataTranslate.documentToCardData(docFields.getId(),
                                        docFields.getData());
                                notesData.add(noteData);
                            }
                            notesSourceResponse.initialized(NotesSourceRemoteImpl.this);
                        }
                    }
                });
        return this;
    }

    @Override
    public NoteData getNoteData(int position) {
        return notesData.get(position);
    }

    @Override
    public int size() {
        return notesData.size();
    }


    @Override
    public void clearNoteData() {
        for (NoteData noteData : notesData) { // FIXME найти более оптимальниы способ
            collectionReference.document(noteData.getId()).delete();
        }
        notesData.clear();
    }

    @Override
    public void deleteNoteData(int position) {
        collectionReference.document(notesData.get(position).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
        notesData.remove(position);
    }

    @Override
    public void updateNoteData(int position, NoteData newNoteData) {
        collectionReference.document(notesData.get(position).getId())
                .update(NoteDataTranslate.cardDataToDocument(newNoteData));
    }

    @Override
    public void addNoteData(NoteData newNoteData) {
        collectionReference.add(NoteDataTranslate.cardDataToDocument(newNoteData));
    }


}
