package es.unizar.eina.notepadv3;

import android.database.Cursor;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class createNoteTest {
    @Rule
    public ActivityTestRule<Notepadv3> activityRule = new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;
    long idNuevaNota;
    String titulo;
    String cuerpo;
    int categoriaId;
    Date fecha;

    @Before
    public void setUp() {
        mNotepad = activityRule.getActivity();
        titulo = "Hola";
        cuerpo = "Soy una nota";
        categoriaId = -1;
        fecha = new Date();
    }

    @After
    public void tearDown(){
        mNotepad.getAdapter().deleteNote(idNuevaNota);
    }

    @Test()
    public void test_P1(){
        idNuevaNota = mNotepad.getAdapter().createNote(titulo, cuerpo, categoriaId, fecha, fecha);
        Cursor salida = mNotepad.getAdapter().fetchNote(idNuevaNota);
        assertEquals(titulo, salida.getString(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
        assertEquals(cuerpo, salida.getString(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
        assertEquals(categoriaId, salida.getInt(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_CATEGORY)));
        assertEquals(fecha.getTime(), salida.getLong(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_ACTIVATION_DATE)));
        assertEquals(fecha.getTime(), salida.getLong(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_EXPIRATION_DATE)));
    }


    @Test()
    public void test_P2(){
        idNuevaNota = mNotepad.getAdapter().createNote(null, cuerpo, categoriaId, fecha, fecha);
        assertEquals(idNuevaNota, -1);

    }

    @Test()
    public void test_P3(){
        idNuevaNota = mNotepad.getAdapter().createNote("", cuerpo, categoriaId, fecha, fecha);
        assertEquals(idNuevaNota, -1);
    }

    @Test()
    public void test_P4(){
        mNotepad.getAdapter().createNote(titulo, null, categoriaId, fecha, fecha);
    }
}