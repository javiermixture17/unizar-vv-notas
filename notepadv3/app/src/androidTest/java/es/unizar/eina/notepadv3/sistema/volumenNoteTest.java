package es.unizar.eina.notepadv3.sistema;

import android.database.Cursor;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import es.unizar.eina.notepadv3.Notepadv3;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class volumenNoteTest {
    @Rule
    public ActivityTestRule<Notepadv3> activityRule = new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;
    private static ArrayList<Long> rowIdsCreados;
    int categoriaId;
    Date fecha;



    @Before
    public void setUp() {
        mNotepad = activityRule.getActivity();
        rowIdsCreados = new ArrayList<>();
        categoriaId = -1;
        fecha = new Date();
    }

    @After
    public void tearDown(){
        /*
        for(long rowId : rowIdsCreados){
            mNotepad.getAdapter().borrarNota(rowId);
        }
        */

    }

    @Test()
    public void test_P1(){
        for(int i = 0; i < 1000; i++) {
            rowIdsCreados.add(mNotepad.getAdapter().createNote("Nota " + i, "Soy una nota", categoriaId, fecha, fecha));
        }
    }

}
