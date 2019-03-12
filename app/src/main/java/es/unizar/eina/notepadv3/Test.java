package es.unizar.eina.notepadv3;

import android.util.Log;

import java.util.ArrayList;

public class Test {
    private static ArrayList<Long> rowIdsCreados = new ArrayList<>();

    public static void ejecucionTest(NotesDbAdapter mDbHelper){
        long rowIdCorrecto = mDbHelper.createNote("Hola", "Soy una nota", -1);
        Log.d("Prueba 1", "Valor devuelto: " + rowIdCorrecto);

        try{
            long rowIdIncorrecto = mDbHelper.createNote(null, "Soy una nota", -1);
            Log.d("Prueba 2", "Valor devuelto: " + rowIdIncorrecto);
        }catch(Throwable e){
            Log.d("Prueba 2", "Excepción", e);
        }

        try{
            long rowIdIncorrecto = mDbHelper.createNote("", "Soy una nota", -1);
            Log.d("Prueba 3", "Valor devuelto: " + rowIdIncorrecto);
        }catch(Exception e){
            Log.d("Prueba 3", "Excepción", e);
        }

        try{
            long rowIdIncorrecto = mDbHelper.createNote("Hola", null, -1);
            Log.d("Prueba 4", "Valor devuelto: " + rowIdIncorrecto);
        }catch(Exception e){
            Log.d("Prueba 4", "Excepción", e);
        }

        ///////////////////////////////////////////////////////////////////

        boolean result = mDbHelper.updateNote(rowIdCorrecto, "Hola", "Soy una nota", -1);
        Log.d("Prueba 5", "Valor devuelto: " + result);

        try{
            result = mDbHelper.updateNote(-2, "Hola", "Soy una nota", -1);
            Log.d("Prueba 6", "Valor devuelto: " + result);
        }catch(Exception e){
            Log.d("Prueba 6", "Excepción", e);
        }

        try{
            result = mDbHelper.updateNote(rowIdCorrecto, null, "Soy una nota", -1);
            Log.d("Prueba 7", "Valor devuelto: " + result);
        }catch(Exception e){
            Log.d("Prueba 7", "Excepción", e);
        }

        try{
            result = mDbHelper.updateNote(rowIdCorrecto, "", "Soy una nota", -1);
            Log.d("Prueba 8", "Valor devuelto: " + result);
        }catch(Exception e){
            Log.d("Prueba 8", "Excepción", e);
        }

        try{
            result = mDbHelper.updateNote(rowIdCorrecto, "Hola", null, -1);
        }catch(Exception e){
            Log.d("Prueba 9", "Excepción", e);
        }

        ///////////////////////////////////////////////////////////////////

        result = mDbHelper.deleteNote(rowIdCorrecto);
        Log.d("Prueba 10", "Valor devuelto: " + result);

        try{
            result = mDbHelper.deleteNote(-2);
            Log.d("Prueba 11", "Valor devuelto: " + result);
        }catch(Exception e){
            Log.d("Prueba 11", "Excepción", e);
        }

        ///////////////////////////////////////////////////////////////////
        // EJECUCION TESTS CATEGORIAS
        ///////////////////////////////////////////////////////////////////

        rowIdCorrecto = mDbHelper.createCategory("Hola");
        Log.d("Prueba 1", "Valor devuelto: " + rowIdCorrecto);

        try{
            long rowIdIncorrecto = mDbHelper.createCategory(null);
            Log.d("Prueba 2", "Valor devuelto: " + rowIdIncorrecto);
        }catch(Throwable e){
            Log.d("Prueba 2", "Excepción", e);
        }

        try{
            long rowIdIncorrecto = mDbHelper.createCategory("");
            Log.d("Prueba 3", "Valor devuelto: " + rowIdIncorrecto);
        }catch(Throwable e){
            Log.d("Prueba 3", "Excepción", e);
        }

        ///////////////////////////////////////////////////////////////////

        result = mDbHelper.updateCategory(rowIdCorrecto, "Hola");
        Log.d("Prueba 4", "Valor devuelto: " + result);

        try{
            result = mDbHelper.updateCategory(-2, "Hola");
            Log.d("Prueba 5", "Valor devuelto: " + result);
        }catch(Exception e){
            Log.d("Prueba 5", "Excepción", e);
        }

        try{
            result = mDbHelper.updateCategory(rowIdCorrecto, null);
            Log.d("Prueba 6", "Valor devuelto: " + result);
        }catch(Exception e){
            Log.d("Prueba 6", "Excepción", e);
        }

        try{
            result = mDbHelper.updateCategory(rowIdCorrecto, "");
            Log.d("Prueba 7", "Valor devuelto: " + result);
        }catch(Exception e){
            Log.d("Prueba 7", "Excepción", e);
        }

        ///////////////////////////////////////////////////////////////////

        result = mDbHelper.deleteCategory(rowIdCorrecto);
        Log.d("Prueba 8", "Valor devuelto: " + result);

        try{
            result = mDbHelper.deleteCategory(-2);
            Log.d("Prueba 9", "Valor devuelto: " + result);
        }catch(Exception e){
            Log.d("Prueba 9", "Excepción", e);
        }
    }

    public static void generarNotas(int cantidad, NotesDbAdapter mDbHelper){
        System.out.println("Generando nota ----------------------------------------------");
        for(int i = 0; i < cantidad; i++){
            rowIdsCreados.add(mDbHelper.createNote("Nota " + i, "Soy una nota", -1));
        }
        System.out.println(rowIdsCreados);
    }

    public static void borrarNotas(NotesDbAdapter mDbHelper){
        for(long rowId : rowIdsCreados){
            mDbHelper.deleteNote(rowId);
        }
    }

    public static void sobrecargaTest(NotesDbAdapter mDbHelper){
        String str = "a";
        for(int i = 0; i <= 29000000; i = i + 1000000){
            mDbHelper.createNote("Hola", new String(new char[i]).replace("\0", "a"), -1);
            Log.d("Nota creada", " con longitud igual a " + i);
        }
    }
}