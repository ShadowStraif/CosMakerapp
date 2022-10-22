package com.example.cosmaker.Utils;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;

import com.example.cosmaker.Costume;
import com.example.cosmaker.CostumeFragment;
import com.example.cosmaker.Model.CosMakerModel;
import com.example.cosmaker.Model.DetailsModel;
import com.example.cosmaker.Model.ImagesModel;
import com.example.cosmaker.Model.NoteModel;
import com.example.cosmaker.Model.SupportModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int VERSION=1;
    private static final String NAME="CosMakerDatabase";

//CHARACTERS TABLE
    private static final String CHARACTER_TABLE="character_table";
    private static final String _id= "_id";
    private static final String CHARACTERNAME="name";
    private static final String FANDOM="fandomdb";
    private static final String DATE ="startdatedb";
    private static final String WAY= "way";

    private static final String CREATE_CHARACTER_TABLE = "CREATE TABLE " + CHARACTER_TABLE + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CHARACTERNAME + " TEXT, " + FANDOM + " TEXT, "
            + DATE + " TEXT, " +  WAY + " BLOB " + ")";

//DETAILS TABLE
    private static final String DETAIL_TABLE="detail";
    private static final String DETAILID= "detailid";
    private static final String DETAILNAME="detailname";
    private static final String PRIORIRY="priority";
    private static final String TYPE ="type";
    private static final String STATUS ="status";
    private static final String CHARID ="chid";
    private static final String CREATE_DETAIL_TABLE = "CREATE TABLE " + DETAIL_TABLE + "(" + DETAILID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DETAILNAME + " TEXT, " + PRIORIRY + " INTEGER, " + TYPE + " TEXT, "
            + STATUS + " INTEGER, " + CHARID + " INTEGER , FOREIGN KEY(" +CHARID+ ") REFERENCES " + CHARACTER_TABLE + "("+_id+") ON DELETE CASCADE );" ;

    private static final String NOTE_TABLE="notetable";
    private static final String NOTEID= "noteid";
    private static final String NOTE="note";
    private static final String NOTESTATUS="notstatus";
    private static final String NOTECHARID="notecharid";
    private static final String CREATE_NOTE_TABLE = "CREATE TABLE " + NOTE_TABLE + "(" + NOTEID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOTE + " TEXT, " + NOTESTATUS + " INTEGER, " + NOTECHARID + " INTEGER , FOREIGN KEY(" +NOTECHARID+ ") REFERENCES " + CHARACTER_TABLE + "("+_id+") ON DELETE CASCADE);" ;

 //REFERENCE TABLE
   private static final String REFERENCE_TABLE="reftable";
    private static final String REFID= "refid";
    private static final String IMAGEB= "imageb";
    private static final String REFCHARID ="chid";
    private static final String CREATE_REFERENCE_TABLE = "CREATE TABLE " + REFERENCE_TABLE + "(" + REFID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +  IMAGEB + " BLOB, "  + REFCHARID + " INTEGER ,FOREIGN KEY(" +REFCHARID+ ") REFERENCES " + CHARACTER_TABLE + "("+_id+") ON DELETE CASCADE);" ;

    //QUOTE TABLE
    private static final String QUOTE_TABLE="quotetable";
    private static final String QUOTEID= "quoteid";
    private static final String QUOTE="quote";
       private static final String CREATE_QUOTE_TABLE = "CREATE TABLE " + QUOTE_TABLE + "(" + QUOTEID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + QUOTE + " TEXT " + ")";

    private static final String SUPPORTINFO_TABLE="supporttable";
    private static final String SUPID= "supid";
    private static final String ENDDATE= "enddate";
    private static final String BUDGET ="budget";
    private static final String SUPCHARID ="supcharid";
    private static final String FINALIMG ="finalimg";
    private static final String CREATE_SUPPORTINFO_TABLE = "CREATE TABLE " + SUPPORTINFO_TABLE + "(" + SUPID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
          + ENDDATE + " TEXT, " + BUDGET + " TEXT, "
            +  FINALIMG + " BLOB, "  + SUPCHARID + " INTEGER ,FOREIGN KEY(" +SUPCHARID+ ") REFERENCES " + CHARACTER_TABLE + "("+_id+") ON DELETE CASCADE);" ;


    private SQLiteDatabase db;
    public DataBaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL(CREATE_CHARACTER_TABLE);
        db.execSQL(CREATE_DETAIL_TABLE);
        db.execSQL(CREATE_NOTE_TABLE);
        db.execSQL(CREATE_REFERENCE_TABLE);
        db.execSQL(CREATE_QUOTE_TABLE);
        db.execSQL(CREATE_SUPPORTINFO_TABLE);
        String q1,q2,q3,q5,q6,q7,q8,q9,q10,q11;
        q1="If you can dream it then you can make it.";
        q2="Your crafting skills are your magic wand.";
        q3="Неудача — возможность узнать что-то новое.";
        q5 = "Embrace your dreams, and… whatever happens, protect your honor… as SOLDIER!";
        q6 = "Все говорят: «Невозможно, невозможно, невозможно...» А вдруг получится?";
        q7 = "Дело не в том, сколько у вас времени, дело в том, как вы его используете.";
        q8 = "С опытом приходит мастерство.";
        q9 = "Если выхода нет, то его всегда можно сделать.";
        q10 = "Ваши тропы — у вас под ногами. Каждый увидит свою в должное время.";
        q11 = "Концовка не так важна, как моменты, ведущие к ней.";
       String [] quotes = new String [] { q1,q2,q3,q5,q6,q7,q8,q9,q10,q11};
    for (int i=0;i<10;i++)
        {
            ContentValues cv = new ContentValues();
            cv.put(QUOTE, quotes[i]);
            db.insert(QUOTE_TABLE, null, cv);
        }
       }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CHARACTER_TABLE);
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    public String randomQuotes() {
        Random r = new Random();
        int i1 = r.nextInt(10);
        i1++;
        String[] selectionArgs = null;
        selectionArgs = new String[] { String.valueOf(i1) };
        Cursor cur2=db.rawQuery("select * from "+ QUOTE_TABLE +   " where "+ QUOTEID +"=?",selectionArgs);
        cur2.moveToFirst();
        String quote = cur2.getString(cur2.getColumnIndex(QUOTE));
        cur2.close();
        return quote;
    }

      public void insertCharacter(CosMakerModel character){
        ContentValues cv = new ContentValues();
        cv.putNull(_id);
        cv.put(CHARACTERNAME, character.getCharacterName());
        cv.put(FANDOM, character.getFandom());
        cv.put(DATE, character.getStartDate());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        character.getImageres().compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byt= byteArrayOutputStream.toByteArray();
        cv.put(WAY, byt);
        db.insert(CHARACTER_TABLE, null, cv);
    }

    public void insertSupportInfo(SupportModel sup, long id){
        ContentValues cv = new ContentValues();
        cv.put(ENDDATE, sup.getEddate());
        cv.put(BUDGET, sup.getBudget());
        cv.put(SUPCHARID,id);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        sup.getFinalimage().compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byt= byteArrayOutputStream.toByteArray();
        cv.put(FINALIMG, byt);
        db.insert(SUPPORTINFO_TABLE, null, cv);
    }

    public void insertDetail(DetailsModel detail, long id){
        ContentValues cv = new ContentValues();
        cv.put(DETAILNAME, detail.getDetailName());
        cv.put(PRIORIRY, detail.getDetailLevel());
        cv.put(TYPE, detail.getDetailType());
        cv.put(STATUS,0);
        cv.put(CHARID,detail.getCharid());
        db.insert(DETAIL_TABLE, null, cv);
    }

    public void insertNote(NoteModel note, long id){
        ContentValues cv = new ContentValues();
        cv.put(NOTE, note.getNotetext());
        cv.put(NOTESTATUS, 0);
        cv.put(NOTECHARID, id);
        db.insert(NOTE_TABLE, null, cv);
    }

    public void insertImages(ImagesModel image, long id){
        ContentValues cv = new ContentValues();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.getImageres().compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byt= byteArrayOutputStream.toByteArray();
        cv.put(IMAGEB, byt);
        cv.put(REFCHARID,id);
        db.insert(REFERENCE_TABLE, null, cv);
    }

   public Bitmap returnimagebyte(int characterid)
   {
       Cursor cur=null;
       String[] selectionArgs = null;
       selectionArgs = new String[] { String.valueOf(characterid) };
       cur = db.rawQuery("select * from "+ CHARACTER_TABLE +   " where "+ _id +"=?",selectionArgs);
       cur.moveToFirst();
       if(cur==null) {
           cur.moveToNext();
       }
       else{
                   int index = cur.getColumnIndex(WAY);
                   byte[] imgByte = cur.getBlob(index);
           return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
           }
       cur.close();
     return null;
   }

    public Bitmap returrefnimagebyte(int imageid)
    {
        Cursor cur=null;
        String[] selectionArgs = null;
        selectionArgs = new String[] { String.valueOf(imageid) };
        cur = db.rawQuery("select * from "+ REFERENCE_TABLE +   " where "+ REFID +"=?",selectionArgs);
        cur.moveToFirst();
        if(cur==null) {
            cur.moveToNext();
        }
        else{
            int index = cur.getColumnIndex(IMAGEB);
            byte[] imgByte = cur.getBlob(index);
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        cur.close();
        return null;
    }

    public List<ImagesModel> returnrefimagebyte(int characterid)
    {
        Cursor cur=null;
        String[] selectionArgs = null;
        List<ImagesModel> imagesList = new ArrayList<>();
        selectionArgs = new String[] { String.valueOf(characterid) };
        cur = db.rawQuery("select * from "+ REFERENCE_TABLE +   " where "+ REFCHARID +"=?",selectionArgs);
        cur.moveToFirst();
        if(cur!=null)
        {
            if(cur.moveToFirst()) {
                do {
                    ImagesModel image = new ImagesModel();
                    image.setId(cur.getInt(cur.getColumnIndex(REFID)));

                    int index = cur.getColumnIndex(IMAGEB);
                    byte[] imgByte = cur.getBlob(index);
                    image.setImageres(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                    image.setCharacterid(cur.getInt(cur.getColumnIndex(REFCHARID)));

                    imagesList.add(image);

                } while (cur.moveToNext());
            }
        }
        cur.close();
        return imagesList;
    }

    public List<NoteModel> getAllNotess(int characterid){
        List<NoteModel> notesList = new ArrayList<>();
        Cursor cur=null;
        String[] selectionArgs = null;
        selectionArgs = new String[] { String.valueOf(characterid) };
        cur = db.rawQuery("select * from "+ NOTE_TABLE +   " where "+ NOTECHARID +"=?",selectionArgs);
        if(cur!=null)
        {
            if(cur.moveToFirst()) {
                do {
                    NoteModel note = new NoteModel();
                    note.setId(cur.getInt(cur.getColumnIndex(NOTEID)));
                    note.setNotetext(cur.getString(cur.getColumnIndex(NOTE)));
                    note.setStatus(cur.getInt(cur.getColumnIndex(NOTESTATUS)));
                    note.setCharacterid(cur.getInt(cur.getColumnIndex(NOTECHARID)));

                    notesList.add(note);

                } while (cur.moveToNext());

            }
        }
        cur.close();
        return notesList;
    }


    public List<DetailsModel> getAllDetailsbyID(int characterid){
        List<DetailsModel> detailsList = new ArrayList<>();
        Cursor cur=null;
        String[] selectionArgs = null;
        selectionArgs = new String[] { String.valueOf(characterid) };
            cur = db.rawQuery("select * from "+ DETAIL_TABLE +   " where "+ CHARID +"=?",selectionArgs);
            if(cur!=null)
            {
                if(cur.moveToFirst()) {
                    do {
                        DetailsModel detail = new DetailsModel();

                            detail.setId(cur.getInt(cur.getColumnIndex(DETAILID)));
                            detail.setDetailName(cur.getString(cur.getColumnIndex(DETAILNAME)));
                            detail.setDetailLevel(cur.getInt(cur.getColumnIndex(PRIORIRY)));
                            detail.setDetailType(cur.getString(cur.getColumnIndex(TYPE)));
                            detail.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                            detail.setCharid(cur.getInt(cur.getColumnIndex(CHARID)));

                            detailsList.add(detail);
                    } while (cur.moveToNext());
                }
            }
            if(cur!=null){ cur.close();}
        return detailsList;
    }

    public List<SupportModel> getSupInformation(int characterid){
        List<SupportModel> supportList = new ArrayList<>();
        Cursor cur=null;
        String[] selectionArgs = null;
        selectionArgs = new String[] { String.valueOf(characterid) };
        cur = db.rawQuery("select * from "+ SUPPORTINFO_TABLE +   " where "+ SUPCHARID +"=?",selectionArgs);
        if(cur!=null)
        {
            if(cur.moveToFirst()) {
                do {
                    SupportModel sup = new SupportModel();

                    sup.setId(cur.getInt(cur.getColumnIndex(SUPID)));
                    sup.setEddate(cur.getString(cur.getColumnIndex(ENDDATE)));
                    sup.setBudget(cur.getString(cur.getColumnIndex(BUDGET)));
                    sup.setSupportcharid(cur.getInt(cur.getColumnIndex(SUPCHARID)));
                    int index = cur.getColumnIndexOrThrow(FINALIMG);
                    byte[] imgByte = cur.getBlob(index);
                    sup.setFinalimage(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                    supportList.add(sup);
                } while (cur.moveToNext());
            }
        }
        if(cur!=null){ cur.close();}
        return supportList;
    }

    public boolean isFinished(int characterid){
        Cursor cur=null;
        String[] selectionArgs = null;
        selectionArgs = new String[] { String.valueOf(characterid) };
        cur = db.rawQuery("select * from "+ SUPPORTINFO_TABLE +   " where "+ SUPCHARID +"=?",selectionArgs);
        if(cur!=null)
        {
            if(cur.moveToFirst()) {
                return true;
            }
        }
        return false;
    }


    public double progresscounting(int characterid)
    {
        String check;
        double progress =0;
        Cursor cur=null;
        String[] selectionArgs = null;
        selectionArgs = new String[] { String.valueOf(characterid) };
        cur = db.rawQuery("select * from "+ DETAIL_TABLE +   " where "+ CHARID +"=?",selectionArgs);
        int size =cur.getCount();
        int cnt1=0;
        int cnt2=0;
        int cnt3=0;
        int cnt4=0;
        if(cur!=null)
        {
            if(cur.moveToFirst()) {
                do {
                    if((cur.getInt(cur.getColumnIndex(PRIORIRY))==1))
                    {
                       cnt1++;
                    }
                    else  if ((cur.getInt(cur.getColumnIndex(PRIORIRY))==2))
                    {
                        cnt2++;
                    }
                    else  if ((cur.getInt(cur.getColumnIndex(PRIORIRY))==3))
                    {
                        cnt3++;
                    }
                    else  if ((cur.getInt(cur.getColumnIndex(PRIORIRY))==4))
                    {
                        cnt4++;
                    }

                } while (cur.moveToNext());
            }
        }
        int m= (cnt1+cnt2*2+cnt3*3+cnt4*4);
        double x = 100 / m;
        if (cur != null) {
               if (cur.moveToFirst()) {
                   do {
                       if (cur.getInt(cur.getColumnIndex(STATUS)) == 1 && (cur.getInt(cur.getColumnIndex(PRIORIRY)) == 1)) {
                           progress += x;
                       } else if (cur.getInt(cur.getColumnIndex(STATUS)) == 1 && (cur.getInt(cur.getColumnIndex(PRIORIRY)) == 2)) {
                           progress += x * 2;
                       } else if (cur.getInt(cur.getColumnIndex(STATUS)) == 1 && (cur.getInt(cur.getColumnIndex(PRIORIRY)) == 3)) {
                           progress += x * 3;
                       } else if (cur.getInt(cur.getColumnIndex(STATUS)) == 1 && (cur.getInt(cur.getColumnIndex(PRIORIRY)) == 4)) {
                           progress += x * 4;
                       }
                   } while (cur.moveToNext());
               }
           }

        if(cur!=null){ cur.close();}
        return progress;

    }

    public List<CosMakerModel> getCharacterbyid(int characterid){
        List<CosMakerModel> charactersList = new ArrayList<>();
        Cursor cur=null;
        String[] selectionArgs = null;
        selectionArgs = new String[] { String.valueOf(characterid) };
        cur = db.rawQuery("select * from "+ CHARACTER_TABLE +   " where "+ _id +"=?",selectionArgs);
        cur.moveToFirst();
        CosMakerModel character = new CosMakerModel();
                    character.setId(cur.getInt(cur.getColumnIndex(_id)));
                    character.setCharacterName(cur.getString(cur.getColumnIndex(CHARACTERNAME)));
                    character.setFandom(cur.getString(cur.getColumnIndex(FANDOM)));
                    character.setStartDate(cur.getString(cur.getColumnIndex(DATE)));
                     int index = cur.getColumnIndexOrThrow(WAY);
                     byte[] imgByte = cur.getBlob(index);
                    character.setImageres(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                       charactersList.add(character);
                      cur.close();

        return charactersList;
    }

public int getLastInsertCharacter()
{
    Cursor cur=null;
        int id;
        cur=db.query(CHARACTER_TABLE, null, null, null, null, null, null, null);
        if(cur!=null && cur.moveToLast())
        {
                     id = cur.getInt(cur.getColumnIndex(_id));
            return id;
        }
        cur.close();
   return 0;
}

    public List<CosMakerModel> getAllCharacters(){
        List<CosMakerModel> charactersList = new ArrayList<>();
        Cursor cur=null;
            cur=db.query(CHARACTER_TABLE, null, null, null, null, null, null, null);
            if(cur!=null)
            {
                if(cur.moveToFirst()) {
                    do {
                        CosMakerModel character = new CosMakerModel();
                        character.setId(cur.getInt(cur.getColumnIndex(_id)));
                        character.setCharacterName(cur.getString(cur.getColumnIndex(CHARACTERNAME)));
                        character.setFandom(cur.getString(cur.getColumnIndex(FANDOM)));
                        character.setStartDate(cur.getString(cur.getColumnIndex(DATE)));
                        int index = cur.getColumnIndexOrThrow(WAY);
                        byte[] imgByte = cur.getBlob(index);
                       character.setImageres(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                       charactersList.add(character);
                    } while (cur.moveToNext());

                }
            }
            cur.close();
        return charactersList;
    }

    public void updateCharacter(int id, CosMakerModel character) {
        ContentValues cv = new ContentValues();
        cv.put(CHARACTERNAME, character.getCharacterName());
        cv.put(FANDOM, character.getFandom());
        cv.put(DATE, character.getStartDate());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        character.getImageres().compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byt= byteArrayOutputStream.toByteArray();
        cv.put(WAY, byt);
        db.update(CHARACTER_TABLE, cv, _id + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteCharacter(int id){
        db.delete(CHARACTER_TABLE, _id + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateDetail(int id, String detaile,String type,int priority ) {
        ContentValues cv = new ContentValues();
        cv.put(DETAILNAME, detaile);
        cv.put(PRIORIRY, priority);
        cv.put(TYPE, type);
        db.update(DETAIL_TABLE, cv, DETAILID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteDetail(int id){
        db.delete(DETAIL_TABLE, DETAILID + "= ?", new String[] {String.valueOf(id)});
    }


    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(DETAIL_TABLE, cv, DETAILID + "= ?", new String[] {String.valueOf(id)});

    }
    public void updateNoteStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(NOTESTATUS, status);
        db.update(NOTE_TABLE, cv, NOTEID + "= ?", new String[] {String.valueOf(id)});
    }
    public void updateNote(int id, String note) {
        ContentValues cv = new ContentValues();
        cv.put(NOTE, note);
        db.update(NOTE_TABLE, cv, NOTEID + "= ?", new String[] {String.valueOf(id)});
    }
    public void deleteNote(int id){
        db.delete(NOTE_TABLE, NOTEID + "= ?", new String[] {String.valueOf(id)});
    }
    public void deleteImage(int id){
        db.delete(REFERENCE_TABLE, REFID + "= ?", new String[] {String.valueOf(id)});
    }

}
