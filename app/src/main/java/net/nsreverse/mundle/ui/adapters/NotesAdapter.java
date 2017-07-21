package net.nsreverse.mundle.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import net.nsreverse.mundle.R;
import net.nsreverse.mundle.model.Note;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Robert on 7/21/2017.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private Delegate delegate;
    private List<ParseObject> dataSource;
    private List<Note> loadedNotesDataSource;

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_note_list_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.titleTextView.setText(loadedNotesDataSource.get(position).getTitle());
        holder.subtitleTextView.setText(loadedNotesDataSource.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (loadedNotesDataSource == null) {
            return 0;
        }

        return loadedNotesDataSource.size();
    }

    public void setDataSource(Context context, List<ParseObject> dataSource) {
        if (context instanceof NotesAdapter.Delegate) {
            delegate = (NotesAdapter.Delegate)context;
        }

        this.dataSource = dataSource;
    }

    public void setLoadedNotesDataSource(List<Note> notesDataSource) {
        loadedNotesDataSource = notesDataSource;
        notifyDataSetChanged();
    }

    public interface Delegate {
        void adapterItemClicked(int position, ParseObject selectedObject);
    }

    private void itemClicked(int position) {
        if (delegate != null) delegate.adapterItemClicked(position, dataSource.get(position));
        System.out.println("A note has been clicked.");
    }

    class NoteViewHolder extends RecyclerView.ViewHolder
                         implements View.OnClickListener {

        @BindView(R.id.text_view_note_title) TextView titleTextView;
        @BindView(R.id.text_view_note_date) TextView subtitleTextView;

        NoteViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClicked(getAdapterPosition());
        }
    }
}
