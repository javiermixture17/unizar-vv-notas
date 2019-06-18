package es.unizar.eina.notepadv3.sistema;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import es.unizar.eina.notepadv3.Notepadv3;
import es.unizar.eina.notepadv3.espresso.EspressoUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class fechasTestRealDate {


    @Rule
    public ActivityTestRule<Notepadv3> mActivityRule =
            new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;
    private long idNuevaNota;


    @Before
    public void setUp() throws ParseException {
        mNotepad = mActivityRule.getActivity();
        mNotepad.getAdapter().setTest();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        idNuevaNota = mNotepad.getAdapter().createNote("Nota test", "Esta es la nota de test",
                -1, sdf.parse("02/01/2019"), sdf.parse("04/01/2019"));
    }

    @After
    public void tearDown(){
        mNotepad.getAdapter().deleteNote(idNuevaNota);
    }

    @Test
    public void test_P1() throws ParseException{
        mNotepad.getAdapter().setFakeDate("01/01/2019");

        EspressoUtils.filtrar("Filter predicted notes");
        onView(withText("Nota test"));
        EspressoUtils.filtrar("Filter active notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrar("Filter expired notes");
        onView(withText("Nota test")).check(doesNotExist());

        mNotepad.getAdapter().setFakeDate("03/01/2019");

        EspressoUtils.filtrar("Filter predicted notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrar("Filter active notes");
        onView(withText("Nota test"));
        EspressoUtils.filtrar("Filter expired notes");
        onView(withText("Nota test")).check(doesNotExist());

    }

    @Test
    public void test_P2() throws ParseException{
        mNotepad.getAdapter().setFakeDate("03/01/2019");

        EspressoUtils.filtrar("Filter predicted notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrar("Filter active notes");
        onView(withText("Nota test"));
        EspressoUtils.filtrar("Filter expired notes");
        onView(withText("Nota test")).check(doesNotExist());


        mNotepad.getAdapter().setFakeDate("05/01/2019");

        EspressoUtils.filtrar("Filter predicted notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrar("Filter active notes");
        onView(withText("Nota test")).check(doesNotExist());
        EspressoUtils.filtrar("Filter expired notes");
        onView(withText("Nota test"));

    }

}
