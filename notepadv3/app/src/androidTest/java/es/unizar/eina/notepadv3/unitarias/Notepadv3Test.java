package es.unizar.eina.notepadv3.unitarias;

import android.database.Cursor;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import es.unizar.eina.notepadv3.Notepadv3;
import es.unizar.eina.notepadv3.NotesDbAdapter;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class Notepadv3Test {
    @Rule
    public ActivityTestRule<Notepadv3> activityRule = new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;
    long idNuevaNota;

    @Before
    public void setUp() {
        mNotepad = activityRule.getActivity();
    }

    @After
    public void tearDown(){
        mNotepad.getAdapter().deleteNote(idNuevaNota);
    }

    @Test()
    public void test_P1(){
        int numeroNotas   = mNotepad.getAdapter().fetchAllNotes().getCount();
        idNuevaNota = mNotepad.getAdapter().createNote("Prueba", "", -1, new Date(), new Date());
        Cursor notasTrasInsercion = mNotepad.getAdapter().fetchAllNotes();
        assertEquals(numeroNotas + 1, notasTrasInsercion.getCount());
    }

    @Test()
    public void test_P2(){
        String titulo = "test2";
        String cuerpo = "abc";
        int categoriaId = -1;
        Date fecha = new Date();
        idNuevaNota = mNotepad.getAdapter().createNote(titulo, cuerpo, categoriaId, fecha, fecha);
        Cursor salida = mNotepad.getAdapter().fetchNote(idNuevaNota);
        assertEquals(titulo, salida.getString(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
        assertEquals(cuerpo, salida.getString(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
        assertEquals(categoriaId, salida.getInt(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_CATEGORY)));
        assertEquals(fecha.getTime(), salida.getLong(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_ACTIVATION_DATE)));
        assertEquals(fecha.getTime(), salida.getLong(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_EXPIRATION_DATE)));

    }
}