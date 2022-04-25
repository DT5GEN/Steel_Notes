package com.task.steel_notes.observe;

import java.util.ArrayList;
import java.util.List;

import com.task.steel_notes.data.NoteData;

public class Publisher {
    private List<Observer> observers;

    public Publisher() {
        this.observers = new ArrayList<Observer>();
    }

    public void subscribe(Observer observer){
        observers.add(observer);
    }
    public void unsubscribe(Observer observer){
        observers.remove(observer);
    }

    public void notifyTask(NoteData noteData){
        for (Observer observer:observers){
            observer.updateState(noteData);
            unsubscribe(observer);
        }
    }

}
