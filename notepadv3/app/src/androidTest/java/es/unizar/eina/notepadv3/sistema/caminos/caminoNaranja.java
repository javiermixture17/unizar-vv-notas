package es.unizar.eina.notepadv3.sistema.caminos;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import es.unizar.eina.notepadv3.Notepadv3;

import static es.unizar.eina.notepadv3.espresso.EspressoUtils.atras;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.borrarNota;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.confirmar;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.editarNota;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.rellenarNota;


public class caminoNaranja {

    @Rule
    public ActivityTestRule<Notepadv3> mActivityRule =
            new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;

    @Before
    public void setUp() throws ParseException {
        System.out.println("EJECUTANDO SETUP");
        mNotepad = mActivityRule.getActivity();
        mNotepad.getAdapter().setTest();
        mNotepad.getAdapter().setFakeDate("05/01/2019");
        mNotepad.getAdapter().cleanNotes();
        mNotepad.getAdapter().cleanCategories();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        mNotepad.getAdapter().createNote("Nota test 1", "Esta es la nota de test", -1, sdf.parse("02/01/2019"), sdf.parse("04/01/2019"));
        for (int i = 0; i < 5; i++){
            mNotepad.getAdapter().createCategory("Categoría " + i);
        }
        System.out.println("FIN SETUP");
    }

    @After
    public void tearDown() {
        mNotepad.getAdapter().cleanNotes();
        mNotepad.getAdapter().cleanCategories();
    }

    @Test
    public void ejecutarCamino() {
        borrarNota("Nota test 1");
        //rellenarNota("Nota test 2");
        //confirmar();
        //editarNota("Nota test 2", "Nota test 2 editada");
        //atras();

    }

}
