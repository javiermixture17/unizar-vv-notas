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

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class updateCategoryTest {
    @Rule
    public ActivityTestRule<

            Notepadv3> activityRule = new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;
    boolean result;
    long idNuevaCategoria;
    String titulo;

    @Before
    public void setUp() {
        mNotepad = activityRule.getActivity();
        titulo = "HolaCat";

        idNuevaCategoria = mNotepad.getAdapter().createCategory(titulo);

        titulo = "HolaCat2";
    }

    @After
    public void tearDown(){
        mNotepad.getAdapter().deleteCategory(idNuevaCategoria);
    }

    @Test()
    public void test_P1(){
        result = mNotepad.getAdapter().updateCategory(idNuevaCategoria, titulo);
        Cursor salida = mNotepad.getAdapter().fetchCategory(idNuevaCategoria);
        assertTrue(result);
        assertEquals(titulo, salida.getString(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
    }


    @Test()
    public void test_P2(){
        result = mNotepad.getAdapter().updateCategory(-2, titulo);
        assertFalse(result);

    }

    @Test()
    public void test_P3(){
        result = mNotepad.getAdapter().updateCategory(idNuevaCategoria, null);
        assertFalse(result);
    }

    @Test()
    public void test_P4(){
        result = mNotepad.getAdapter().updateCategory(idNuevaCategoria, "");
        assertFalse(result);
    }
}