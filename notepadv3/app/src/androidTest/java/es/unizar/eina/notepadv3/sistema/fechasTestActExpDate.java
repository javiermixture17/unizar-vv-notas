package es.unizar.eina.notepadv3.sistema;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.unizar.eina.notepadv3.Notepadv3;
import es.unizar.eina.notepadv3.espresso.EspressoUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class fechasTestActExpDate {


    @Rule
    public ActivityTestRule<Notepadv3> mActivityRule =
            new ActivityTestRule<>(Notepadv3.class);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Notepadv3 mNotepad;
    private long idNuevaNota;
    private String titulo;
    private String cuerpo;
    private int categoriaId;
    private Date fechaAct;
    private Date fechaCad;

    @Before
    public void setUp() throws ParseException {
        mNotepad = mActivityRule.getActivity();
        titulo = "Nota test";
        cuerpo = "Esta es la nota de test";
        categoriaId = -1;
        mNotepad.getAdapter().setTest();
        mNotepad.getAdapter().setFakeDate("10/01/2019");
    }

    @After
    public void tearDown(){
        mNotepad.getAdapter().deleteNote(idNuevaNota);
    }

    @Test
    public void test_Prevista_Vigente() throws ParseException{
        fechaAct = sdf.parse("11/01/2019");
        fechaCad = sdf.parse("14/01/2019");

        idNuevaNota = mNotepad.getAdapter().createNote(titulo,cuerpo, categoriaId,fechaAct ,fechaCad );

        EspressoUtils.filtrarPorFecha("Filter predicted notes");
        onView(withText("Nota test"));
        EspressoUtils.filtrarPorFecha("Filter active notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter expired notes");
        onView(withText("Nota test")).check(doesNotExist());

        fechaAct = sdf.parse("08/01/2019");
        mNotepad.getAdapter().updateNote(idNuevaNota, titulo, cuerpo, categoriaId, fechaAct, fechaCad);

        EspressoUtils.filtrarPorFecha("Filter predicted notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter active notes");
        onView(withText("Nota test"));
        EspressoUtils.filtrarPorFecha("Filter expired notes");
        onView(withText("Nota test")).check(doesNotExist());

    }

    @Test
    public void test_Prevista_Caducada() throws ParseException{
        fechaAct = sdf.parse("11/01/2019");
        fechaCad = sdf.parse("14/01/2019");

        idNuevaNota = mNotepad.getAdapter().createNote(titulo,cuerpo, categoriaId,fechaAct ,fechaCad );

        EspressoUtils.filtrarPorFecha("Filter predicted notes");
        onView(withText("Nota test"));
        EspressoUtils.filtrarPorFecha("Filter active notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter expired notes");
        onView(withText("Nota test")).check(doesNotExist());

        fechaAct = sdf.parse("08/01/2019");
        fechaCad = sdf.parse("09/01/2019");
        mNotepad.getAdapter().updateNote(idNuevaNota, titulo, cuerpo, categoriaId, fechaAct, fechaCad);

        EspressoUtils.filtrarPorFecha("Filter predicted notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter active notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter expired notes");
        onView(withText("Nota test"));

    }

    @Test
    public void test_Vigente_Prevista() throws ParseException{
        fechaAct = sdf.parse("08/01/2019");
        fechaCad = sdf.parse("14/01/2019");

        idNuevaNota = mNotepad.getAdapter().createNote(titulo,cuerpo, categoriaId,fechaAct ,fechaCad );

        EspressoUtils.filtrarPorFecha("Filter predicted notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter active notes");
        onView(withText("Nota test"));
        EspressoUtils.filtrarPorFecha("Filter expired notes");
        onView(withText("Nota test")).check(doesNotExist());

        fechaAct = sdf.parse("11/01/2019");
        mNotepad.getAdapter().updateNote(idNuevaNota, titulo, cuerpo, categoriaId, fechaAct, fechaCad);

        EspressoUtils.filtrarPorFecha("Filter predicted notes");
        onView(withText("Nota test"));
        EspressoUtils.filtrarPorFecha("Filter active notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter expired notes");
        onView(withText("Nota test")).check(doesNotExist());

    }

    @Test
    public void test_Vigente_Caducada() throws ParseException{
        fechaAct = sdf.parse("08/01/2019");
        fechaCad = sdf.parse("14/01/2019");

        idNuevaNota = mNotepad.getAdapter().createNote(titulo,cuerpo, categoriaId,fechaAct ,fechaCad );

        EspressoUtils.filtrarPorFecha("Filter predicted notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter active notes");
        onView(withText("Nota test"));
        EspressoUtils.filtrarPorFecha("Filter expired notes");
        onView(withText("Nota test")).check(doesNotExist());

        fechaCad = sdf.parse("09/01/2019");
        mNotepad.getAdapter().updateNote(idNuevaNota, titulo, cuerpo, categoriaId, fechaAct, fechaCad);

        EspressoUtils.filtrarPorFecha("Filter predicted notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter active notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter expired notes");
        onView(withText("Nota test"));

    }

    @Test
    public void test_Caducada_Prevista() throws ParseException{
        fechaAct = sdf.parse("08/01/2019");
        fechaCad = sdf.parse("09/01/2019");

        idNuevaNota = mNotepad.getAdapter().createNote(titulo,cuerpo, categoriaId,fechaAct ,fechaCad );

        EspressoUtils.filtrarPorFecha("Filter predicted notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter active notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter expired notes");
        onView(withText("Nota test"));

        fechaAct = sdf.parse("11/01/2019");
        fechaCad = sdf.parse("14/01/2019");
        mNotepad.getAdapter().updateNote(idNuevaNota, titulo, cuerpo, categoriaId, fechaAct, fechaCad);

        EspressoUtils.filtrarPorFecha("Filter predicted notes");
        onView(withText("Nota test"));
        EspressoUtils.filtrarPorFecha("Filter active notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter expired notes");
        onView(withText("Nota test")).check(doesNotExist());

    }

    @Test
    public void test_Caducada_Vigente() throws ParseException{
        fechaAct = sdf.parse("08/01/2019");
        fechaCad = sdf.parse("14/01/2019");

        idNuevaNota = mNotepad.getAdapter().createNote(titulo,cuerpo, categoriaId,fechaAct ,fechaCad );

        EspressoUtils.filtrarPorFecha("Filter predicted notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter active notes");
        onView(withText("Nota test"));
        EspressoUtils.filtrarPorFecha("Filter expired notes");
        onView(withText("Nota test")).check(doesNotExist());

        fechaCad = sdf.parse("14/01/2019");
        mNotepad.getAdapter().updateNote(idNuevaNota, titulo, cuerpo, categoriaId, fechaAct, fechaCad);

        EspressoUtils.filtrarPorFecha("Filter predicted notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrarPorFecha("Filter active notes");
        onView(withText("Nota test"));
        EspressoUtils.filtrarPorFecha("Filter expired notes");
        onView(withText("Nota test")).check(doesNotExist());

    }

}
