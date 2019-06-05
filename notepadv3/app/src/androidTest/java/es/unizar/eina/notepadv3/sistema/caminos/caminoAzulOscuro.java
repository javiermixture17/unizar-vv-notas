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
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.editarCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.editarNota;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.filtrarPorCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.filtrarPorFecha;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.listarCategorias;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.ordenarPor;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.rellenarCategoria;
import static es.unizar.eina.notepadv3.espresso.EspressoUtils.rellenarNota;


public class caminoAzulOscuro {

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
        rellenarNota("Nota test 1");
        confirmar();
        long idNuevaCategoria=0;
        for (int i = 0; i < 5; i++){
            idNuevaCategoria = mNotepad.getAdapter().createCategory("Categoria " + i);
        }
        mNotepad.getAdapter().createNote("Nota test activa", "cuerpo", Math.toIntExact(idNuevaCategoria), sdf.parse("02/01/2019"), sdf.parse("07/01/2019"));
        mNotepad.getAdapter().createNote("Nota test prevista", "cuerpo", Math.toIntExact(idNuevaCategoria), sdf.parse("06/01/2019"), sdf.parse("07/01/2019"));
        mNotepad.getAdapter().createNote("Nota test caducada", "cuerpo", Math.toIntExact(idNuevaCategoria), sdf.parse("02/01/2019"), sdf.parse("03/01/2019"));
        mNotepad.getAdapter().createNote("Nota test caducada 2", "cuerpo", Math.toIntExact(idNuevaCategoria), sdf.parse("02/01/2019"), sdf.parse("03/01/2019"));
        mNotepad.getAdapter().createNote("Nota test caducada 3", "cuerpo", Math.toIntExact(idNuevaCategoria), sdf.parse("02/01/2019"), sdf.parse("03/01/2019"));
        System.out.println("FIN SETUP");

    }

    @After
    public void tearDown() {
        mNotepad.getAdapter().cleanNotes();
        mNotepad.getAdapter().cleanCategories();
    }

    @Test
    public void ejecutarCamino() {
        filtrarPorFecha("Filter active notes");
        rellenarNota("Nota test 2");
        confirmar();
        filtrarPorFecha("Filter active notes");
        editarNota("Nota test activa", " editada");
        confirmar();
        filtrarPorFecha("Filter predicted notes");
        editarNota("Nota test prevista", " editada");
        confirmar();
        ordenarPor("category");
        rellenarNota("Nota test 3");
        confirmar();
        ordenarPor("title");
        rellenarNota("Nota test 4");
        confirmar();
        filtrarPorFecha("Filter expired notes");
        editarNota("Nota test caducada", " e");
        confirmar();
        filtrarPorCategoria("Categoria 4");
        editarNota("Nota test caducada e", " 2");
        confirmar();
        borrarNota("Nota test caducada e 2");
        editarNota("Nota test caducada 2", " e");
        atras();
        borrarNota("Nota test caducada 2");
        editarNota("Nota test caducada 3", " e");
        atras();
        filtrarPorFecha("Filter predicted notes");
        rellenarNota("Nota test 5");
        atras();
        filtrarPorCategoria("Categoria 4");
        editarNota("Nota test caducada 3", " e");
        atras();
        ordenarPor("title");
        editarNota("Nota test 4", " editada");
    }

}
