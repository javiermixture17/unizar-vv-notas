package es.unizar.eina.notepadv3.sistema.caminos;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;

import es.unizar.eina.notepadv3.Notepadv3;

import static es.unizar.eina.notepadv3.espresso.EspressoUtils.atras;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.borrarCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.borrarNota;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.confirmar;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.editarCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.editarNota;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.filtrarPorCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.filtrarPorFecha;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.listarCategorias;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.rellenarCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.rellenarNota;


public class CaminoRosa {

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
        rellenarNota("Nota test 1");
        confirmar();
        for (int i = 0; i < 5; i++){
            mNotepad.getAdapter().createCategory("Categoria " + i);
        }
    }

    @After
    public void tearDown() {
        mNotepad.getAdapter().cleanNotes();
        mNotepad.getAdapter().cleanCategories();
    }

    @Test
    public void ejecutarCamino() {
        listarCategorias();
        editarCategoria("Categoria 4", " editada");
        confirmar();
        atras();
        rellenarCategoria("Categoria 6");
        atras();
        rellenarNota("Nota test 2");
        atras();
        editarNota("Nota test 1", " editada");
        atras();
        rellenarCategoria("Categoria 7");
        atras();
        editarNota("Nota test 1", " editada");
        atras();
        listarCategorias();
        atras();
        filtrarPorFecha("Filter active notes"); //revisar
        rellenarNota("Nota test 3");
        confirmar();
        listarCategorias();
        atras();
        rellenarNota("Nota test 4");
        confirmar();
        listarCategorias();
        atras();
        editarNota("Nota test 1", " editada");
        confirmar();
        rellenarCategoria("Categoria 8");
        confirmar();
        rellenarNota("Nota test 5");
        confirmar();
        rellenarCategoria("Categoria 9");
        confirmar();
        editarNota("Nota test 1 editada", " 2");
    }

}
