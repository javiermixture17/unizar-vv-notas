package es.unizar.eina.notepadv3;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CategoryEdit  extends AppCompatActivity {

    /**
     * Campo de edicion del titulo de la categoria
     */
    private EditText mTitleText;

    /** Identificador de la categoria */
    private Long mRowId;

    /** Objeto para manipulacion de base de datos */
    private NotesDbAdapter mDbHelper;

    // practica 4
    private EditText mIdText;

    /**
     * Creacion de la actividad
     * @param savedInstanceState estado almacenado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.category_edit);
        setTitle(R.string.edit_category);

        mTitleText = (EditText) findViewById(R.id.title);

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
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchCategory(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));

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

        if(!title.trim().equalsIgnoreCase("")) {
            if (mRowId == null) {
                long id = mDbHelper.createCategory(title);
                if (id > 0) {
                    mRowId = id;
                }
            } else {
                mDbHelper.updateCategory(mRowId, title);
            }
        }
    }
}
