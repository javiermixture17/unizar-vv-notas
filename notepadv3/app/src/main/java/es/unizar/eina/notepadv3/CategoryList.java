package es.unizar.eina.notepadv3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.Arrays;

public class CategoryList extends AppCompatActivity {

    private static final int ACTIVITY_CAT_EDIT=0;

    /** identificador de borrado de nota */
    private static final int DELETE_ID = Menu.FIRST;

    /** identificador de edicion de nota */
    private static final int EDIT_ID = Menu.FIRST + 1;

    /** objeto para trabajar con la base de datos */
    private NotesDbAdapter mDbHelper;

    /** objeto para gestion del sistema */
    private ListView mList;

    private static int CATEGORIA_SELECCIONADA = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);

        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        mList = (ListView)findViewById(R.id.list);
        fillData();

        registerForContextMenu(mList);
        CATEGORIA_SELECCIONADA = 0;
    }

    private void fillData() {
        // Get all of the categories from the database and create the item list
        Cursor notesCursor = mDbHelper.fetchAllCategories();
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
        mList.setSelection(CATEGORIA_SELECCIONADA);
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
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_delete_cat);
        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.menu_edit_cat);
    }

    /**
     * Procesado de la opcion escogida
     * @param item opcion seleccionada por el usuario
     * @return resultado de procesar la seleccion
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        CATEGORIA_SELECCIONADA = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteCategory(info.id);
                fillData();
                return true;
            case EDIT_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                editCategory(info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void editCategory(long id){
        Intent i = new Intent(this, CategoryEdit.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_CAT_EDIT);
    }
}
