package es.unizar.eina.notepadv3;

import android.content.Intent;
import android.database.MergeCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import es.unizar.eina.send.SendAbstractionImpl;

import static es.unizar.eina.notepadv3.Test.borrarNotas;
import static es.unizar.eina.notepadv3.Test.ejecucionTest;
import static es.unizar.eina.notepadv3.Test.generarNotas;
import static es.unizar.eina.notepadv3.Test.sobrecargaTest;

/**
 * Clase principal de la aplicacion Notas.
 * Gestiona la adicion, edicion y borrado de notas.
 * Asimismo, se permite el envio de notas por email y SMS.
 */
public class Notepadv3 extends AppCompatActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    private static final int ACTIVITY_CAT_CREATE=2;
    private static final int ACTIVITY_VIEW_CAT=3;
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;

    /** identificador de envio de nota por mail */
    private static final int SEND_ID_MAIL = Menu.FIRST + 3;

    /** identificador de envio de nota por SMS */
    private static final int SEND_ID_SMS = Menu.FIRST + 4;
    private static final int INSERT_CAT_ID = Menu.FIRST + 5;
    private static final int VIEW_CAT_ID = Menu.FIRST + 6;
    private static final int ORDER_TITLE_ID = Menu.FIRST + 7;
    private static final int ORDER_CAT_ID = Menu.FIRST + 8;
    private static final int EXEC_TEST_ID = Menu.FIRST + 9;
    private static final int EXEC_CREATE_NOTES_TEST_ID = Menu.FIRST + 10;
    private static final int EXEC_DELETE_NOTES_TEST_ID = Menu.FIRST + 11;
    private static final int EXEC_SOBRECARGA_TEST_ID = Menu.FIRST + 12;
    private static final int FILTER_PREDICTED = Menu.FIRST + 13;
    private static final int FILTER_ACTIVE = Menu.FIRST + 14;
    private static final int FILTER_EXPIRED = Menu.FIRST + 15;

    private static int NOTA_SELECCIONADA = 0;

    private NotesDbAdapter mDbHelper;

    private ListView mList;

    private Spinner spinner;
    private ArrayList<String> listaCategorias;
    private BiMap<Integer, String> mapaCategorias;

    public NotesDbAdapter getAdapter(){
        return this.mDbHelper;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepadv3);
        spinner = (Spinner) findViewById(R.id.filtrador);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int id_seleccionada =  mapaCategorias.inverse().get(listaCategorias.get(position));
                if(id_seleccionada == -1){
                    fillData();
                }else{
                    fillData(id_seleccionada);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        mList = (ListView)findViewById(R.id.list);
        fillData();
        fillCategories();
        registerForContextMenu(mList);
    }

    private void fillCategories(){
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
    }

    /**
     * Muestra de las notas de la base de datos por pantalla
     */
    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor notesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(notesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { NotesDbAdapter.KEY_TITLE};
        Arrays.sort(from);
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to);
        mList.setAdapter(notes);
        mList.setSelection(NOTA_SELECCIONADA);
    }

    private void fillData(int id_categoria){
        Cursor notesCursor = mDbHelper.fetchNotesFromCategory(id_categoria);
        startManagingCursor(notesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { NotesDbAdapter.KEY_TITLE };
        Arrays.sort(from);
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to);
        mList.setAdapter(notes);
    }


    /**
     * Creacion de las posibles acciones disponibles del menu de opciones
     * @param menu objeto para almacenar las opciones del menu
     * @return resultado de crear el menu de opciones
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.menu_insert);
        menu.add(Menu.NONE, INSERT_CAT_ID, Menu.NONE, R.string.menu_insert_cat);
        menu.add(Menu.NONE, VIEW_CAT_ID, Menu.NONE, R.string.menu_view_cat);
        menu.add(Menu.NONE, ORDER_TITLE_ID, Menu.NONE, R.string.menu_order_title);
        menu.add(Menu.NONE, ORDER_CAT_ID, Menu.NONE, R.string.menu_order_cat);
        menu.add(Menu.NONE, EXEC_TEST_ID, Menu.NONE, R.string.exec_tests);
        menu.add(Menu.NONE, EXEC_CREATE_NOTES_TEST_ID, Menu.NONE, R.string.exec_create_notes_tests);
        menu.add(Menu.NONE, EXEC_DELETE_NOTES_TEST_ID, Menu.NONE, R.string.exec_delete_notes_tests);
        menu.add(Menu.NONE, EXEC_SOBRECARGA_TEST_ID, Menu.NONE, R.string.exec_sobrecarga_test);
        menu.add(Menu.NONE, FILTER_PREDICTED, Menu.NONE, R.string.filter_predicted);
        menu.add(Menu.NONE, FILTER_ACTIVE, Menu.NONE, R.string.filter_active);
        menu.add(Menu.NONE, FILTER_EXPIRED, Menu.NONE, R.string.filter_expired);
        return result;
    }

    /**
     * Procesado de la opcion seleccionada
     * @param item elemento del menu seleccionado
     * @return resultado de gestionar el elemento seleccionado
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createNote();
                return true;
            case INSERT_CAT_ID:
                createCategory();
                return true;
            case VIEW_CAT_ID:
                viewCategories();
                return true;
            case ORDER_TITLE_ID:
                orderTitle();
                return true;
            case ORDER_CAT_ID:
                orderCategory();
                return true;
            case EXEC_TEST_ID:
                ejecucionTest(mDbHelper);
                return true;
            case EXEC_CREATE_NOTES_TEST_ID:
                generarNotas(1000, mDbHelper);
                fillData();
                return true;
            case EXEC_DELETE_NOTES_TEST_ID:
                borrarNotas(mDbHelper);
                fillData();
                return true;
            case EXEC_SOBRECARGA_TEST_ID:
                sobrecargaTest(mDbHelper);
                fillData();
                return true;
            case FILTER_PREDICTED:
                filterTimed(FILTER_PREDICTED);
                return true;
            case FILTER_ACTIVE:
                filterTimed(FILTER_ACTIVE);
                return true;
            case FILTER_EXPIRED:
                filterTimed(FILTER_EXPIRED);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Creacion del menu de opciones para cada nota
     * @param menu objeto para almacenar las opciones disponibles
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_delete);
        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.menu_edit);
        menu.add(Menu.NONE, SEND_ID_MAIL, Menu.NONE, R.string.send_note_email);
        menu.add(Menu.NONE, SEND_ID_SMS, Menu.NONE, R.string.send_note_sms);
    }

    /**
     * Procesado de la opcion escogida
     * @param item opcion seleccionada por el usuario
     * @return resultado de procesar la seleccion
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                NOTA_SELECCIONADA = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteNote(info.id);
                fillData();
                return true;
            case EDIT_ID:
                NOTA_SELECCIONADA = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                editNote(info.id);
                return true;
            case SEND_ID_MAIL:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                sendNote(info.id, "mail");
                return true;
            case SEND_ID_SMS:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                sendNote(info.id, "SMS");
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Creacion de una nota
     */
    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    private void createCategory(){
        Intent i = new Intent(this, CategoryEdit.class);
        startActivityForResult(i, ACTIVITY_CAT_CREATE);
    }

    private void viewCategories(){
        Intent i = new Intent(this, CategoryList.class);
        startActivityForResult(i, ACTIVITY_VIEW_CAT);
    }

    private void orderTitle(){
        NOTA_SELECCIONADA = 0;
        fillData();
    }

    private void orderCategory(){
        Cursor categories = mDbHelper.fetchAllCategories();
        Cursor[] notasOrdenadas = new Cursor[categories.getCount() + 1];
        startManagingCursor(categories);
        HashBiMap<Integer, String> mapa = HashBiMap.create();
        categories.moveToFirst();
        while(!categories.isAfterLast()){
            mapa.put(categories.getInt(categories.getColumnIndex("_id")),
                    categories.getString(categories.getColumnIndex(mDbHelper.KEY_TITLE)));
            categories.moveToNext();
        }
        List<String> lista = new ArrayList<>(mapa.values());
        Collections.sort(lista);

        int i = 0;
        for(String categoria : lista){
            Cursor notesCursor = mDbHelper.fetchNotesFromCategory(mapa.inverse().get(categoria));
            startManagingCursor(notesCursor);
            notasOrdenadas[i] = notesCursor;
            i++;
        }

        Cursor notasSinCategoria = mDbHelper.fetchNotesFromCategory(-1);
        startManagingCursor(notasSinCategoria);
        notasOrdenadas[i] = notasSinCategoria;
        MergeCursor cursorFinal = new MergeCursor(notasOrdenadas);
        String[] from = new String[] { NotesDbAdapter.KEY_TITLE};
        Arrays.sort(from);
        int[] to = new int[] { R.id.text1 };
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, cursorFinal, from, to);
        mList.setAdapter(notes);
    }

    /**
     * Edicion de una nota
     * @param id identificador de la nota a editar
     */
    private void editNote(long id) {
        Intent i = new Intent(this, NoteEdit.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    /**
     * Envio de una nota
     * @param idNota identificador de la nota a enviar
     * @param metodo metodo escogido para el envio
     */
    private void sendNote(long idNota, String metodo){
        Cursor note = mDbHelper.fetchNote(idNota);
        startManagingCursor(note);
        String titulo = note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE));
        String cuerpo = note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY));
        SendAbstractionImpl sendAbstractionImpl = new SendAbstractionImpl(this, metodo);
        sendAbstractionImpl.send(titulo, cuerpo);
    }


    private void filterTimed(int filterType){
        // Get all of the notes from the database and create the item list
        Cursor notesCursor;
        switch (filterType){
            case FILTER_PREDICTED:
                notesCursor = mDbHelper.fetchPredictedNotes();
                break;
            case FILTER_ACTIVE:
                notesCursor = mDbHelper.fetchActiveNotes();
                break;
            case FILTER_EXPIRED:
                notesCursor = mDbHelper.fetchExpiredNotes();
                break;
            default:
                notesCursor = mDbHelper.fetchAllNotes();
                break;
        }
        startManagingCursor(notesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { NotesDbAdapter.KEY_TITLE};
        Arrays.sort(from);
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to);
        mList.setAdapter(notes);
        mList.setSelection(NOTA_SELECCIONADA);
    }

    @Override
    protected void onResume(){
        super.onResume();
        fillCategories();
    }

    /**
     * Finalizacion de la actividad
     * @param requestCode codigo de peticion
     * @param resultCode codigo de resultado de ejecucion
     * @param intent actividad realizada
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}
