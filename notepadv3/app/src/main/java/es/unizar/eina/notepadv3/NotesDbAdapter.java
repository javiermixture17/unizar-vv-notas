package es.unizar.eina.notepadv3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


class DateSelector {
    private boolean esTest;
    private Date now;

    public DateSelector(){
        esTest= false;
    }

    public void setTest(){
        esTest= true;
    }

    public void setDate(String _date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        now = sdf.parse(_date);
    }

    public Date getNow(){
        if(esTest){
            return now;
        }
        else{
            return new Date();
        }
    }
}

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 *
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class NotesDbAdapter {

    /** nombre del campo titulo de la nota en la base de datos */
    public static final String KEY_TITLE = "title";

    /** nombre del campo cuerpo de la nota en la base de datos */
    public static final String KEY_BODY = "body";

    public static final String KEY_CATEGORY = "id_category";

    /** nombre del campo identificador de la nota en la base de datos */
    public static final String KEY_ROWID = "_id";

    /** nombre del campo de fecha de activación de la nota en la base de datos */
    public static final String KEY_ACTIVATION_DATE = "activation_date";

    /** nombre del campo de fecha de caducidad de la nota en la base de datos */
    public static final String KEY_EXPIRATION_DATE = "expiration_date";

    /** nombre de la clase */
    private static final String TAG = "NotesDbAdapter";

    /** objeto para realizar operaciones de creacion y actualizacion de base de datos */
    private DatabaseHelper mDbHelper;

    /** objeto para insertar, actualizar y borrar elementos de la base de datos */
    private SQLiteDatabase mDb;

    /**Seleccionar test o no*/
    public DateSelector dS = new DateSelector();

    /**
     * Database creation sql statement
     */
    public void setTest(){
        dS.setTest();
    }

    public void setFakeDate(String date) throws ParseException{
        dS.setDate(date);
    }


    private static final String DATABASE_CREATE =
            "create table notes (_id integer primary key autoincrement, "
                    + "title text not null, body text not null,"
                    + "activation_date integer not null,"
                    + "expiration_date integer not null,"
                    + "id_category integer, foreign key(id_category) references categories(_id));";

    private static final String DATABASE_CREATE_CATEGORIES =
            "create table categories (_id integer primary key autoincrement, "
                    + "title text not null);";

    /** nombre de la base de datos */
    private static final String DATABASE_NAME = "data";

    /** nombre de la tabla de la base de datos */
    private static final String DATABASE_TABLE = "notes";

    private static final String DATABASE_TABLE_CATEGORIES = "categories";

    /** versión de la base de datos */
    private static final int DATABASE_VERSION = 4;

    /** objeto para mantener el contexto */
    private final Context mCtx;

    /**
     * Clase para operaciones de creacion y actualizacion de base de datos
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        /**
         * Constructor
         * @param context contexto del sistema
         */
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Inicializacion de la actividad
         * @param db objeto para manipulacion de la base de datos
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_CATEGORIES);
            db.execSQL(DATABASE_CREATE);
        }

        /**
         * Actualizacion de la base de datos
         * @param db objeto para manipulacion de base de datos
         * @param oldVersion id de version de base de datos antigua
         * @param newVersion id de version de base de datos nueva
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            db.execSQL("DROP TABLE IF EXISTS categories");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public NotesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public NotesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    public long createNote(String title, String body, int id_category, Date activationDate, Date expirationDate) {
        if (title == null || title == "") {
            return -1;
        }
        if (activationDate.compareTo(expirationDate) >= 0 ){
            return -1;
        }
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        initialValues.put(KEY_CATEGORY, id_category);
        initialValues.put(KEY_ACTIVATION_DATE, activationDate.getTime());
        initialValues.put(KEY_EXPIRATION_DATE, expirationDate.getTime());
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public long createCategory(String title){
        if (title == null || title == "") {
            return -1;
        }
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);

        return mDb.insert(DATABASE_TABLE_CATEGORIES, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteNote(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteCategory(long rowId){
        Cursor notasCategoriaBorrar = fetchNotesFromCategory((int) rowId);
        notasCategoriaBorrar.moveToFirst();
        while(!notasCategoriaBorrar.isAfterLast()){
            long id_nota = notasCategoriaBorrar.getLong(notasCategoriaBorrar.getColumnIndex("_id"));
            updateNote(id_nota,
                        notasCategoriaBorrar.getString(notasCategoriaBorrar.getColumnIndexOrThrow(KEY_TITLE)),
                        notasCategoriaBorrar.getString(notasCategoriaBorrar.getColumnIndex(KEY_BODY)), -1,
                        new Date(notasCategoriaBorrar.getLong(notasCategoriaBorrar.getColumnIndexOrThrow(KEY_ACTIVATION_DATE))),
                        new Date(notasCategoriaBorrar.getLong(notasCategoriaBorrar.getColumnIndexOrThrow(KEY_ACTIVATION_DATE))));
            notasCategoriaBorrar.moveToNext();
        }
        return mDb.delete(DATABASE_TABLE_CATEGORIES, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public void cleanNotes(){
        mDb.execSQL("delete from " + DATABASE_TABLE);
    }

    public void cleanCategories(){
        mDb.execSQL("delete from " + DATABASE_TABLE_CATEGORIES);
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotes() {
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_BODY, KEY_CATEGORY, KEY_ACTIVATION_DATE, KEY_EXPIRATION_DATE}, null, null, null, null, KEY_TITLE);


    }

    public Cursor fetchNotesFromCategory(int id_category){
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_BODY, KEY_CATEGORY, KEY_ACTIVATION_DATE, KEY_EXPIRATION_DATE}, KEY_CATEGORY+"="+id_category, null, null, null, KEY_TITLE);
    }

    public Cursor fetchAllCategories(){
        return mDb.query(DATABASE_TABLE_CATEGORIES, new String[] {KEY_ROWID, KEY_TITLE},
                null, null, null, null, KEY_TITLE);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_BODY, KEY_CATEGORY, KEY_ACTIVATION_DATE, KEY_EXPIRATION_DATE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchCategory(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_CATEGORIES, new String[] {KEY_ROWID,
                                KEY_TITLE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchPredictedNotes() throws SQLException {
        Date now = dS.getNow();
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_BODY, KEY_CATEGORY, KEY_ACTIVATION_DATE, KEY_EXPIRATION_DATE},
                        KEY_ACTIVATION_DATE + ">" + now.getTime(), null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchActiveNotes() throws SQLException {
        Date now = dS.getNow();
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_BODY, KEY_CATEGORY, KEY_ACTIVATION_DATE, KEY_EXPIRATION_DATE},
                        KEY_ACTIVATION_DATE + "<" + now.getTime() + " and " + KEY_EXPIRATION_DATE + ">" + now.getTime(), null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchExpiredNotes() throws SQLException {
        Date now = dS.getNow();
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_BODY, KEY_CATEGORY, KEY_ACTIVATION_DATE, KEY_EXPIRATION_DATE},
                        KEY_EXPIRATION_DATE + "<" + now.getTime(), null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateNote(long rowId, String title, String body, int id_categoria, Date activationDate, Date expirationDate) {
        if (title == null || title == "") {
            return false;
        }
        if (body == null || body == "") {
            return false;
        }
        if (activationDate.compareTo(expirationDate) >= 0 ){
            return false;
        }
        if(rowId > 0){
            ContentValues args = new ContentValues();
            args.put(KEY_TITLE, title);
            args.put(KEY_BODY, body);
            args.put(KEY_CATEGORY, id_categoria);
            args.put(KEY_ACTIVATION_DATE, activationDate.getTime());
            args.put(KEY_EXPIRATION_DATE, expirationDate.getTime());

            return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
        }else{
            return false;
        }
    }

    public boolean updateCategory(long rowId, String title) {
        if (title == null || title == "") {
            return false;
        }
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);

        return mDb.update(DATABASE_TABLE_CATEGORIES, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}