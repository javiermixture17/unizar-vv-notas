package es.unizar.eina.notepadv3.sistema.caminos;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;

import es.unizar.eina.notepadv3.Notepadv3;

import static es.unizar.eina.notepadv3.espresso.EspressoUtils.atras;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.borrarNota;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.confirmar;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.editarCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.editarNota;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.filtrarPorCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.filtrarPorFecha;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.listarCategorias;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.ordenarPor;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.rellenarCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.rellenarNota;


public class CaminoAmarillo {

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
        rellenarNota("Nota test 2");
        confirmar();
        rellenarNota("Nota test 3");
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
        rellenarCategoria("Categoria 6");
        confirmar();
        borrarNota("Nota test 1");
        rellenarCategoria("Categoria 7");
        confirmar();
        listarCategorias();
        atras();
        borrarNota("Nota test 2");
        rellenarCategoria("Categoria 8");
        confirmar();
        filtrarPorFecha("Filter active notes");
        listarCategorias();
        atras();
        filtrarPorFecha("Filter predicted notes");
        rellenarCategoria("Categoria 9");
        confirmar();
        filtrarPorFecha("Filter predicted notes");
        listarCategorias();
        atras();
        ordenarPor("title");
        listarCategorias();
        editarCategoria("Categoria 4", " editada");
        confirmar();
        editarCategoria("Categoria 4 editada", " 2");
        atras();
        atras();
        ordenarPor("category");
        rellenarCategoria("Categoria 10");
        confirmar();
        ordenarPor("title");
        rellenarCategoria("Categoria 11");
        atras();
        borrarNota("Nota test 3");
        rellenarCategoria("Categoria 12");
        atras();
        filtrarPorCategoria("Categoria 1");
        rellenarCategoria("Categoria 13");
        atras();
        filtrarPorFecha("Filter active notes");
        rellenarCategoria("Categoria 14");
        atras();
        filtrarPorFecha("Filter predicted notes");
        rellenarCategoria("Categoria 15");
        atras();
        ordenarPor("category");
        filtrarPorCategoria("Categoria 2");
        rellenarCategoria("Categoria 16");
    }

}
