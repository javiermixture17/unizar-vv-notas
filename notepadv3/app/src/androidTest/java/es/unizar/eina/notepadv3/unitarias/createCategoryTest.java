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

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class createCategoryTest {
    @Rule
    public ActivityTestRule<Notepadv3> activityRule = new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;
    long idNuevaCategoria;
    String titulo;

    @Before
    public void setUp() {
        mNotepad = activityRule.getActivity();
        titulo = "HolaCat";
    }

    @After
    public void tearDown(){
        mNotepad.getAdapter().deleteCategory(idNuevaCategoria);
    }

    @Test()
    public void test_P1(){
        idNuevaCategoria = mNotepad.getAdapter().createCategory(titulo);
        Cursor salida = mNotepad.getAdapter().fetchCategory(idNuevaCategoria);
        assertEquals(titulo, salida.getString(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
    }


    @Test()
    public void test_P2(){
        idNuevaCategoria = mNotepad.getAdapter().createCategory(null);
        assertEquals(idNuevaCategoria, -1);

    }

    @Test()
    public void test_P3(){
        idNuevaCategoria = mNotepad.getAdapter().createCategory("");
        assertEquals(idNuevaCategoria, -1);
    }
}