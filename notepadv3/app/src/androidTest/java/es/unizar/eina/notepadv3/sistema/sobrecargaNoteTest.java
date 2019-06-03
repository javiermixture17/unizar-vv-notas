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
public class sobrecargaNoteTest {
    @Rule
    public ActivityTestRule<Notepadv3> activityRule = new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;
    long idNuevaNota;
    String titulo;
    int categoriaId;
    Date fecha;
    private static ArrayList<Long> rowIdsCreados;

    @Before
    public void setUp() {
        mNotepad = activityRule.getActivity();
        rowIdsCreados = new ArrayList<>();
        titulo = "Hola";
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
        String str = "a";
        for(int i = 0; i <= 29000000; i = i + 1000000){
            rowIdsCreados.add(mNotepad.getAdapter().createNote(titulo, new String(new char[i]).replace("\0", "a"), categoriaId, fecha, fecha));
        }
    }
}