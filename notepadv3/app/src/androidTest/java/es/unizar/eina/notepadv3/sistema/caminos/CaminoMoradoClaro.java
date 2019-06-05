package es.unizar.eina.notepadv3.sistema.caminos;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import es.unizar.eina.notepadv3.Notepadv3;

import static es.unizar.eina.notepadv3.espresso.EspressoUtils.borrarNota;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.confirmar;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.filtrarPorFecha;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.listarCategorias;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.rellenarNota;

public class CaminoMoradoClaro {

    @Rule
    public ActivityTestRule<Notepadv3> mActivityRule =
            new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;

    @Before
    public void setUp() throws ParseException {
        mNotepad = mActivityRule.getActivity();
        mNotepad.getAdapter().setTest();
        mNotepad.getAdapter().setFakeDate("05/01/2019");
        mNotepad.getAdapter().cleanNotes();
        mNotepad.getAdapter().cleanCategories();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        mNotepad.getAdapter().createNote("Nota prevista", "Nota de test", -1, sdf.parse("10/01/2019"), sdf.parse("12/01/2019"));
        mNotepad.getAdapter().createNote("Nota caducada", "Nota de test", -1, sdf.parse("02/01/2019"), sdf.parse("04/01/2019"));
        mNotepad.getAdapter().createNote("Nota activa", "Nota de test", -1, sdf.parse("02/01/2019"), sdf.parse("08/01/2019"));

        for (int i = 0; i < 2; i++){
            rellenarNota("Nota normal " + i);
            confirmar();
        }
    }

    @After
    public void tearDown() {
        mNotepad.getAdapter().cleanNotes();
        mNotepad.getAdapter().cleanCategories();
    }

    @Test
    public void ejecutarCamino() {
        borrarNota("Nota normal 0");
        filtrarPorFecha("Filter expired notes");
        borrarNota("Nota caducada");
        filtrarPorFecha("Filter active notes");
        borrarNota("Nota activa");
        filtrarPorFecha("Filter predicted notes");
        borrarNota("Nota prevista");
        listarCategorias();
    }
}
