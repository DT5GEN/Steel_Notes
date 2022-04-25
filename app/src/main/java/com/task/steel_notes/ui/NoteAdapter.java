package com.task.steel_notes.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.task.steel_notes.R;
import com.task.steel_notes.data.NoteData;
import com.task.steel_notes.data.NotesSource;

public class NoteAdapter
        extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final static String TAG = "SocialNetworkAdapter";

    public void setDataSource(NotesSource dataSource) {
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    private NotesSource dataSource;
    private OnItemClickListener itemClickListener;  // Слушатель будет устанавливаться извне

    private Fragment fragment;
    private int menuContextClickPosition;

    public int getMenuContextClickPosition() {
        return menuContextClickPosition;
    }

    // Передаем в конструктор источник данных
    // В нашем случае это массив, но может быть и запросом к БД
    public NoteAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    // Создать новый элемент пользовательского интерфейса
    // Запускается менеджером
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Создаем новый элемент пользовательского интерфейса
        // Через Inflater
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);
        Log.d(TAG, "onCreateViewHolder");
        // Здесь можно установить всякие параметры
        return new ViewHolder(v);
    }

    // Заменить данные в пользовательском интерфейсе
    // Вызывается менеджером
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // Получить элемент из источника данных (БД, интернет...)
        // Вынести на экран используя ViewHolder
        viewHolder.setData(dataSource.getNoteData(i));
        Log.d(TAG, "onBindViewHolder");
    }

    // Вернуть размер данных, вызывается менеджером
    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    // Сеттер слушателя нажатий
    public void SetOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // Интерфейс для обработки нажатий как в ListView
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // Этот класс хранит связь между данными и элементами View
    // Сложные данные могут потребовать несколько View на
    // один пункт списка
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private AppCompatImageView image;
        private CheckBox note;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.note_body);
            image = (AppCompatImageView) itemView.findViewById(R.id.imageView);
            note = itemView.findViewById(R.id.done);
            date = itemView.findViewById(R.id.date);

            fragment.registerForContextMenu(image);
            // Обработчик нажатий на картинке
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    menuContextClickPosition = getAdapterPosition();
                    itemView.showContextMenu();
                    return false;  // true/false  меняет расположение контекстного меню
                }
            });
        }

        public void setData(NoteData noteData) {
            title.setText(noteData.getTitle());
            date.setText(noteData.getDate().toString());
            description.setText(noteData.getDescription());
            note.setChecked(noteData.isDone());
            image.setImageResource(noteData.getPicture());
        }
    }
}
