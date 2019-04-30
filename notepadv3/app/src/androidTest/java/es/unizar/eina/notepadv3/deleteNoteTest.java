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
public class deleteNoteTest {
    @Rule
    public ActivityTestRule<Notepadv3> activityRule = new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;
    boolean result;
    String titulo;
    String cuerpo;
    int categoriaId;
    Date fecha;
    long idNuevaNota;

    @Before
    public void setUp() {
        mNotepad = activityRule.getActivity();
        titulo = "Hola";
        cuerpo = "Soy una nota";
        categoriaId = -1;
        fecha = new Date();

        idNuevaNota = mNotepad.getAdapter().createNote(titulo, cuerpo, categoriaId, fecha, fecha);
    }

    @After
    public void tearDown(){
        mNotepad.getAdapter().deleteNote(idNuevaNota);
    }

    @Test()
    public void test_P1(){
        result = mNotepad.getAdapter().deleteNote(idNuevaNota);
        assertEquals(result, true);
    }


    @Test()
    public void test_P2(){
        result = mNotepad.getAdapter().deleteNote(-2);
        assertEquals(result, false);

    }
}