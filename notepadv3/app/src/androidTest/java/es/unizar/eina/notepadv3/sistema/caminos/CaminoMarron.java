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
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.filtrarPorCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.filtrarPorFecha;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.listarCategorias;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.ordenarPor;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.rellenarCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.rellenarNota;


public class CaminoMarron {

    @Rule
    public ActivityTestRule<Notepadv3> mActivityRule =
            new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Before
    public void setUp() throws ParseException {
        System.out.println("EJECUTANDO SETUP");
        mNotepad = mActivityRule.getActivity();
        mNotepad.getAdapter().setTest();
        mNotepad.getAdapter().setFakeDate("05/01/2019");
        mNotepad.getAdapter().cleanNotes();
        mNotepad.getAdapter().cleanCategories();
        long idNuevaCategoria=0;
        for (int i = 0; i < 5; i++){
            idNuevaCategoria = mNotepad.getAdapter().createCategory("Categoria " + i);
        }
        mNotepad.getAdapter().createNote("Nota test activa", "cuerpo", Math.toIntExact(idNuevaCategoria), sdf.parse("02/01/2019"), sdf.parse("07/01/2019"));
        System.out.println("FIN SETUP");
    }

    @After
    public void tearDown() {
        mNotepad.getAdapter().cleanNotes();
        mNotepad.getAdapter().cleanCategories();
    }

    @Test
    public void ejecutarCamino() {
        rellenarCategoria("Categoria test");
        confirmar();
        filtrarPorCategoria("Categoria 4");
        editarNota("Nota test activa", " editada");
        atras();
        rellenarCategoria("Categoria test 2");
        atras();
        rellenarNota("Nota test 1");
        confirmar();
        rellenarNota("Nota test 2");
        atras();
        rellenarNota("Nota test 2");
        atras();
        filtrarPorFecha("Filter active notes");
        rellenarCategoria("Categoria test 3");
    }

}
