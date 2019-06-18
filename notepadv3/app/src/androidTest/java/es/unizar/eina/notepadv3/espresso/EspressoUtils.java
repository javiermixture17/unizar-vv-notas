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

    public static void confirmar(){
        onView(withText("Confirm")).perform(click());
    }

    public static void rellenarNota(String nombre) {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText("Add item")).perform(click());
        onView(ViewMatchers.withId(R.id.title)).perform(typeText(nombre), closeSoftKeyboard());
    }

    public static void rellenarCategoria(String nombre) {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText("Add category")).perform(click());
        onView(ViewMatchers.withId(R.id.title)).perform(typeText(nombre), closeSoftKeyboard());
    }

    public static void ordenarPor(String tipoOrdenado) {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText("Order by "+ tipoOrdenado)).perform(click());
    }

    public static void filtrarPorFecha(String tipoFiltrado) {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText(tipoFiltrado)).perform(click());
    }

    public static void filtrarPorCategoria(String nombreCategoria){
        onView(ViewMatchers.withId(R.id.filtrador)).perform(click());
        onView(withText(nombreCategoria)).perform(click());
    }

    public static void borrarNota(String nombre) {
        onView(withText(nombre)).perform(longClick());
        onView(withText("Delete note")).perform(click());

    }

    public static void borrarCategoria(String nombre) {
        onView(withText(nombre)).perform(longClick());
        onView(withText("Delete category")).perform(click());

    }

    public static void editarNota(String nombreAntiguo, String nuevoNombre){
        onView(withText(nombreAntiguo)).perform(longClick());
        onView(withText("Edit note")).perform(click());
        onView(ViewMatchers.withId(R.id.title)).perform(typeText(nuevoNombre), closeSoftKeyboard());
    }

    public static void editarCategoria(String nombreAntiguo, String nuevoNombre){
        onView(withText(nombreAntiguo)).perform(longClick());
        onView(withText("Edit category")).perform(click());
        onView(ViewMatchers.withId(R.id.title)).perform(typeText(nuevoNombre), closeSoftKeyboard());
    }

    public static void listarCategorias(){
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText("View categories")).perform(click());

    }
}
