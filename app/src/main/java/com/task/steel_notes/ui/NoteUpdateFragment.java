package com.task.steel_notes.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

import com.task.steel_notes.MainActivity;
import com.task.steel_notes.R;
import com.task.steel_notes.data.NoteData;
import com.task.steel_notes.data.PictureIndexConverter;
import com.task.steel_notes.observe.Publisher;

public class NoteUpdateFragment extends Fragment {

    private static final String ARG_CARD_DATA = "Param_CardData";
    private TextInputEditText title;
    private TextInputEditText description;
    private DatePicker datePicker;


    private NoteData noteData;
    private Publisher publisher;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        publisher = ((MainActivity) context).getPublisher();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        publisher = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_note, container, false);
        initView(view);
        if (noteData != null) {
            populateView();
        }
        return view;
    }

    private void populateView() {
        title.setText(noteData.getTitle());
        description.setText(noteData.getDescription());
        initDatePicker(noteData.getDate());
    }

    private void initDatePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);
    }

    // Получение даты из DatePicker
    private Date getDateFromDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.datePicker.getYear());
        cal.set(Calendar.MONTH, this.datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return cal.getTime();
    }


    private void initView(View view) {
        title = view.findViewById(R.id.inputTitle);
        description = view.findViewById(R.id.inputDescription);
        datePicker = view.findViewById(R.id.inputDate);
    }


    // Для редактирования данных
    public static NoteUpdateFragment newInstance(NoteData noteData) {
        NoteUpdateFragment fragment = new NoteUpdateFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD_DATA, noteData);
        fragment.setArguments(args);
        return fragment;
    }

    // Для добавления новых данных
    public static NoteUpdateFragment newInstance() {
        NoteUpdateFragment fragment = new NoteUpdateFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteData = getArguments().getParcelable(ARG_CARD_DATA);
        }
    }

    private NoteData collectCardData() {
        String title = this.title.getText().toString();
        String description = this.description.getText().toString();
        Date date = getDateFromDatePicker();
        //int picture;
        //boolean like;

//        String text2 = " Test Text";
//        Spanned args = Html.fromHtml(title,Html.FROM_HTML_MODE_COMPACT);
//        SpannableString spannableString = new SpannableString(text2);
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(title);
//        SpannedString spannedString = new SpannedString(spannableStringBuilder);
//        spannableStringBuilder.setSpan(new BulletSpan(20, ContextCompat.getColor(requireContext(), R.color.colorAccent), 15), 3, 9 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        if (noteData != null) {
            noteData.setTitle(title);
            noteData.setDescription(description);
            noteData.setDate(date);
            return noteData;
        } else {
            int picture =PictureIndexConverter.getPictureByIndex(PictureIndexConverter.randomPictureIndex());
            return new NoteData(title,description,picture,false,date);
        }
    }


    // Здесь соберём данные из views
    @Override
    public void onStop() {
        super.onStop();
        noteData = collectCardData();
    }

    // Здесь передадим данные в паблишер
    @Override
    public void onDestroy() {
        Log.d("mylogs","onDestroy()");
        super.onDestroy();
        publisher.notifyTask(noteData);
    }


}
