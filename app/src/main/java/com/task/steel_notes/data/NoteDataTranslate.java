package com.task.steel_notes.data;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class NoteDataTranslate {
    public  static class Fields{
        public final static String PICTURE = "picture";
        public final static String DATE = "date";
        public final static String TITLE = "title";
        public final static String DESCRIPTION = "description";
        public final static String DONE = "done";
    }

    public static NoteData documentToCardData(String id, Map<String,Object> doc){
        NoteData answer = new NoteData(
                (String) doc.get(Fields.TITLE),
                (String) doc.get(Fields.DESCRIPTION),
                PictureIndexConverter.getPictureByIndex( Math.toIntExact((Long) doc.get(Fields.PICTURE))),
                (boolean) doc.get(Fields.DONE),
                ((Timestamp) doc.get(Fields.DATE)).toDate());
        answer.setId(id);
        return answer;
    }

    public static Map<String,Object> cardDataToDocument( NoteData noteData){
        Map<String,Object> answer = new HashMap<>();
        answer.put(Fields.TITLE, noteData.getTitle());
        answer.put(Fields.DESCRIPTION, noteData.getDescription());
        answer.put(Fields.PICTURE,PictureIndexConverter.getIndexByPicture(noteData.getPicture()));
        answer.put(Fields.DONE, noteData.isDone());
        answer.put(Fields.DATE, noteData.getDate());
        return answer;
    }

}
