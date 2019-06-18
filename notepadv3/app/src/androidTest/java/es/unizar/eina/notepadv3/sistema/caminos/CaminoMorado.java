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
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.filtrarPorCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.filtrarPorFecha;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.listarCategorias;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.ordenarPor;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.rellenarCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.rellenarNota;

public class CaminoMorado {
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
        long catId = mNotepad.getAdapter().createCategory("Categoria test 1");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i < 3; i++){
            mNotepad.getAdapter().createNote("Nota test " + i, "", (int) catId, sdf.parse("02/01/2019"), sdf.parse("04/01/2019"));
        }
        listarCategorias();
        atras();
    }

    @After
    public void tearDown() {
        mNotepad.getAdapter().cleanNotes();
        mNotepad.getAdapter().cleanCategories();
    }

    @Test
    public void ejecutarCamino() {
        ordenarPor("title");
        ordenarPor("title");
        ordenarPor("category");
        ordenarPor("category");
        filtrarPorFecha("Filter predicted notes");
        filtrarPorFecha("Filter predicted notes");
        filtrarPorFecha("Filter active notes");
        filtrarPorFecha("Filter active notes");
        filtrarPorFecha("Filter expired notes");
        filtrarPorFecha("Filter expired notes");
        filtrarPorCategoria("Categoria test 1");
        filtrarPorCategoria("Categoria test 1");
        borrarNota("Nota test 0");
        borrarNota("Nota test 1");
        filtrarPorCategoria("Categoria test 1");
        filtrarPorFecha("Filter expired notes");
        filtrarPorFecha("Filter active notes");
        filtrarPorFecha("Filter predicted notes");
        ordenarPor("category");
        ordenarPor("title");
        filtrarPorFecha("Filter predicted notes");
        ordenarPor("title");
        filtrarPorFecha("Filter active notes");
        ordenarPor("title");
        filtrarPorFecha("Filter expired notes");
        ordenarPor("title");
        filtrarPorCategoria("Categoria test 1");
        ordenarPor("title");
        borrarNota("Nota test 2");
        ordenarPor("title");
        rellenarCategoria("Categoria test 2");
    }
}
