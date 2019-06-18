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

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class deleteNoteTest {
    @Rule
    public ActivityTestRule<Notepadv3> activityRule = new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    boolean result;
    String titulo;
    String cuerpo;
    int categoriaId;
    Date fechaAct;
    Date fechaCad;
    long idNuevaNota;

    @Before
    public void setUp() throws ParseException {
        mNotepad = activityRule.getActivity();
        titulo = "Hola";
        cuerpo = "Soy una nota";
        categoriaId = -1;
        fechaAct = sdf.parse("02/01/2019");
        fechaCad = sdf.parse("03/01/2019");

        idNuevaNota = mNotepad.getAdapter().createNote(titulo, cuerpo, categoriaId, fechaAct, fechaCad);
    }

    @After
    public void tearDown(){
        mNotepad.getAdapter().deleteNote(idNuevaNota);
    }

    @Test()
    public void test_P1(){
        result = mNotepad.getAdapter().deleteNote(idNuevaNota);
        assertTrue(result);
    }


    @Test()
    public void test_P2(){
        result = mNotepad.getAdapter().deleteNote(-2);
        assertFalse(result);

    }
}