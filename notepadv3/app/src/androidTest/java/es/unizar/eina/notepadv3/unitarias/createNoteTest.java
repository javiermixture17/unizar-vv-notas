package es.unizar.eina.notepadv3.unitarias;

import android.database.Cursor;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import es.unizar.eina.notepadv3.Notepadv3;
import es.unizar.eina.notepadv3.NotesDbAdapter;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class createNoteTest {
    @Rule
    public ActivityTestRule<Notepadv3> activityRule = new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    long idNuevaNota;
    String titulo;
    String cuerpo;
    int categoriaId;
    Date fechaAct;
    Date fechaCad;

    @Before
    public void setUp() throws ParseException {
        mNotepad = activityRule.getActivity();
        titulo = "Hola";
        cuerpo = "Soy una nota";
        categoriaId = -1;
        fechaAct = sdf.parse("02/01/2019");
        fechaCad = sdf.parse("03/01/2019");
    }

    @After
    public void tearDown(){
        mNotepad.getAdapter().deleteNote(idNuevaNota);
    }

    @Test()
    public void test_P1(){
        idNuevaNota = mNotepad.getAdapter().createNote(titulo, cuerpo, categoriaId, fechaAct, fechaCad);
        Cursor salida = mNotepad.getAdapter().fetchNote(idNuevaNota);
        assertEquals(titulo, salida.getString(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
        assertEquals(cuerpo, salida.getString(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
        assertEquals(categoriaId, salida.getInt(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_CATEGORY)));
        assertEquals(fechaAct.getTime(), salida.getLong(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_ACTIVATION_DATE)));
        assertEquals(fechaCad.getTime(), salida.getLong(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_EXPIRATION_DATE)));
    }


    @Test()
    public void test_P2(){
        idNuevaNota = mNotepad.getAdapter().createNote(null, cuerpo, categoriaId, fechaAct, fechaCad);
        assertEquals(idNuevaNota, -1);

    }

    @Test()
    public void test_P3(){
        idNuevaNota = mNotepad.getAdapter().createNote("", cuerpo, categoriaId, fechaAct, fechaCad);
        assertEquals(idNuevaNota, -1);
    }

    @Test()
    public void test_P4(){
        mNotepad.getAdapter().createNote(titulo, null, categoriaId, fechaAct, fechaCad);
    }

    @Test()
    public void test_P5() throws ParseException {
        fechaCad = sdf.parse("01/01/2019");
        idNuevaNota= mNotepad.getAdapter().createNote(titulo, null, categoriaId, fechaAct, fechaCad);
        assertEquals(idNuevaNota, -1);
    }
}