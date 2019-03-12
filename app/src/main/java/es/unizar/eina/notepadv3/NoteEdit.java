package es.unizar.eina.notepadv3;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Clase para la edicion de notas
 */
public class NoteEdit extends AppCompatActivity {

    /**
     * Campo de edicion del titulo de la nota
     */
    private EditText mTitleText;

    /**
     * Campo de edicion del cuerpo de la nota
     */
    private EditText mBodyText;

    /** Identificador de la nota */
    private Long mRowId;

    /** Objeto para manipulacion de base de datos */
    private NotesDbAdapter mDbHelper;

    // practica 4
    private EditText mIdText;
    private Spinner spinner;
    private ArrayList<String> listaCategorias;
    private BiMap<Integer, String> mapaCategorias;

    /**
     * Creacion de la actividad
     * @param savedInstanceState estado almacenado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.note_edit);
        setTitle(R.string.edit_note);

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);

        // practica 4
        mIdText = (EditText) findViewById(R.id.id);
        mIdText.setEnabled(false);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = (extras != null) ? extras.getLong(NotesDbAdapter.KEY_ROWID)
                    : null;
        }
        spinner = (Spinner) findViewById(R.id.selector_categoria);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            if(!mTitleText.getText().toString().trim().equalsIgnoreCase("")) {
                setResult(RESULT_OK);
                finish();
            }else{
                mTitleText.setError("Title must not be blank");
            }
            }

        });
    }

    /**
     * Rellenado de los campos con los valores actuales en la base de datos
     */
    private void populateFields() {

        Cursor categories = mDbHelper.fetchAllCategories();
        startManagingCursor(categories);

        mapaCategorias = HashBiMap.create();
        mapaCategorias.put(-1, "None");
        categories.moveToFirst();
        while(!categories.isAfterLast()) {
            mapaCategorias.put(categories.getInt(categories.getColumnIndex("_id")),
                    categories.getString(categories.getColumnIndex(mDbHelper.KEY_TITLE)));
            categories.moveToNext();
        }
        listaCategorias = new ArrayList<>(mapaCategorias.values());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, listaCategorias);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter(spinnerArrayAdapter);

        if (mRowId != null) {
            Cursor note = mDbHelper.fetchNote(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
            mBodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
            int id_categoria = note.getInt(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_CATEGORY));
            System.out.println("IDC cargado:" + id_categoria);
            spinner.setSelection(listaCategorias.indexOf(mapaCategorias.get(id_categoria)));
            // practica 4
            mIdText.setText(mRowId.toString());
        }
        // practica 4
        else {
            mIdText.setText("***");
        }
    }

    /**
     * Guardado de la modificacion de la nota
     * @param outState estado de salida
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
    }

    /**
     * Pausado de la actividad de edicion de una nota
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    /**
     * Reanudacion de la actividad de edicion de una nota
     */
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    /**
     * Guardado en base de datos de las modificaciones realizadas sobre la nota
     */
    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        int categoria_seleccionada = mapaCategorias.inverse().get(spinner.getSelectedItem());
        if(!title.trim().equalsIgnoreCase("")) {
            if (mRowId == null) {
                long id = mDbHelper.createNote(title, body, categoria_seleccionada);
                if (id > 0) {
                    mRowId = id;
                }
            } else {
                mDbHelper.updateNote(mRowId, title, body, categoria_seleccionada);
            }
        }
    }
}
