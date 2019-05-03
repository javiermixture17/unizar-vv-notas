package es.unizar.eina.notepadv3;

import android.database.Cursor;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class contenidoTest {
    @Rule
    public ActivityTestRule<Notepadv3> activityRule = new ActivityTestRule<>(Notepadv3.class);
    Notepadv3 mNotepad;
    long idNuevaNota;
    String cuerpo;
    int categoriaId;
    Date fecha;

    @Before
    public void setUp() {
        mNotepad = activityRule.getActivity();
        categoriaId = -1;
        fecha = new Date();
    }

    @After
    public void tearDown(){
        mNotepad.getAdapter().deleteNote(idNuevaNota);
    }

    @Test()
    public void test_P1(){
        String titulo = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{¦}~0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ¡¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶•¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ∀∁∂∃∄∅∆∇∈∉∊∋∌∍∎∏∐∑∓∔∕∖∗∘∙√∛∜∝∞∟∠∡∢∣∤∥∦∧∨∩∪∫∬∭∮∯∰∱∲∳∴∵∶∷∸∹∺∻∼∽∾∿≀≁≂≃≄≅≆≇≈≉≊≋≌≍≎≏≐≑≒≓≔≕≖≗≘≙≚≛≜≝≞≟≠≡≢≣≤≥≦≧≨≩≪≫≬≭≮≯≰≱≲≳≴≵≶≷≸≹≺≻≼≽≾≿⊀⊁⊂⊃⊄⊅⊆⊇⊈⊉⊊⊋⊌⊍⊎⊏⊐⊑⊒⊓⊔⊕⊖⊗⊘⊙⊚⊛⊜⊝⊞⊟⊠⊡⊢⊣⊤⊥⊦⊧⊨⊩⊪⊫⊬⊭⊮⊯⊰⊱⊲⊳⊴⊵⊶⊷⊸⊹⊺⊻⊼⊽⊾⊿⋀⋁⋂⋃⋄⋅⋆⋇⋈⋉⋊⋋⋌⋍⋎⋏⋐⋑⋒⋓⋔⋕⋖⋗⋘⋙⋚⋛⋜⋝⋞⋟⋠⋡⋢⋣⋤⋥⋦⋧⋨⋩⋪⋫⋬⋭⋮⋯⋰⋱⋲⋳⋴⋵⋶⋷⋸⋹⋺⋻⋼⋽⋾⋿";
        idNuevaNota = mNotepad.getAdapter().createNote(titulo, "Soy una nota", categoriaId, fecha, fecha);
        Cursor salida = mNotepad.getAdapter().fetchNote(idNuevaNota);
        assertEquals(titulo, salida.getString(salida.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
    }

    @Test()
    public void test_P2(){

        int i=0;
        int hex = (char) 0x0;
        while (i<174){
            hex = hex+1;
            mNotepad.getAdapter().createNote(String.valueOf((char) hex), Integer.toString(i), categoriaId, fecha, fecha);
            mNotepad.getAdapter().createNote(Integer.toString(i)+String.valueOf((char) hex)+Integer.toString(i), Integer.toString(i), categoriaId, fecha, fecha);
            i++;
        }

    }

}