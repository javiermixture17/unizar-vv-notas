package es.unizar.eina.notepadv3;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    private Date activationDate;
    private Date expirationDate;
    private DatePicker datePicker;
    private TextView activationView;
    private TextView expirationView;
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public boolean onClick(View v) {
        switch (v.getId()){
            case R.id.activationView:
                obtenerFecha(1);
                break;
            case R.id.expirationView:
                obtenerFecha(2);
                break;
        }
        return true;
    }

    private void obtenerFecha(final int numFecha){
        Calendar c = Calendar.getInstance();
        if(numFecha == 1){
            c.setTime(activationDate);
        }else if(numFecha == 2){
            c.setTime(expirationDate);
        }
        final int mes = c.get(Calendar.MONTH);
        final int dia = c.get(Calendar.DAY_OF_MONTH);
        final int anio = c.get(Calendar.YEAR);
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                Calendar nuevaFecha = Calendar.getInstance();
                nuevaFecha.set(anio, month, dayOfMonth);
                if(numFecha == 1) {
                    activationDate = nuevaFecha.getTime();
                    activationView.setText(df.format(activationDate));
                }else if(numFecha == 2){
                    expirationDate = nuevaFecha.getTime();
                    expirationView.setText(df.format(expirationDate));
                }

            }
        },anio, mes, dia);
        recogerFecha.show();
    }

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
                saveState();
                finish();
            }else{
                mTitleText.setError("Title must not be blank");
            }
            }

        });

        activationView = (TextView) findViewById(R.id.activationView);
        expirationView = (TextView) findViewById(R.id.expirationView);

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
            spinner.setSelection(listaCategorias.indexOf(mapaCategorias.get(id_categoria)));
            // practica 4
            mIdText.setText(mRowId.toString());
            activationDate = new Date(note.getLong(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_ACTIVATION_DATE)));
            expirationDate = new Date(note.getLong(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_EXPIRATION_DATE)));
        }
        // practica 4
        else {
            mIdText.setText("***");
            activationDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(activationDate);
            c.add(Calendar.DATE, 30);
            expirationDate = c.getTime();
        }
        activationView.setText(df.format(activationDate));
        expirationView.setText(df.format(expirationDate));
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
                long id = mDbHelper.createNote(title, body, categoria_seleccionada, activationDate, expirationDate);
                if (id > 0) {
                    mRowId = id;
                }
            } else {
                mDbHelper.updateNote(mRowId, title, body, categoria_seleccionada,activationDate, expirationDate);
            }
        }
    }
}
