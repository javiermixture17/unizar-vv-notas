package es.unizar.eina.notepadv3.sistema;

import androidx.test.rule.ActivityTestRule;

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

public class fechasTest {


    @Rule
    public ActivityTestRule<Notepadv3> mActivityRule =
            new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;


    @Before
    public void setUp() throws ParseException {
        mNotepad = mActivityRule.getActivity();
        mNotepad.getAdapter().setTest();
        mNotepad.getAdapter().setFakeDate("01/01/2019");
        mNotepad.getAdapter().cleanNotes();
        mNotepad.getAdapter().cleanCategories();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        mNotepad.getAdapter().createNote("Nota test", "Esta es la nota de test", -1, sdf.parse("02/01/2019"), sdf.parse("04/01/2019"));
    }

    @Test
    public void test_P1() throws ParseException{
        EspressoUtils.filtrar("Filter predicted notes");

        onView(withText("Nota test"));
        mNotepad.getAdapter().setFakeDate("03/01/2019");
        EspressoUtils.filtrar("Filter predicted notes");
        onView(withText("Nota test")).check(doesNotExist());

    }

}
