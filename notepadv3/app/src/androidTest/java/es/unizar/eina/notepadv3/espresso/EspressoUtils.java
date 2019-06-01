package es.unizar.eina.notepadv3.espresso;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import es.unizar.eina.notepadv3.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class EspressoUtils {
    public static void atras() {
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressBack();
    }

    public static void rellenarCategoria(String nombre) {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText("Add category")).perform(click());
        onView(ViewMatchers.withId(R.id.title)).perform(typeText(nombre), closeSoftKeyboard());
    }

    public static void filtrar(String tipoFiltrado) {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText(tipoFiltrado)).perform(click());
    }

    public static void deleteNote(String nombre) {
        onView(withText(nombre)).perform(longClick());
        onView(withText("Delete note")).perform(click());

    }
}
