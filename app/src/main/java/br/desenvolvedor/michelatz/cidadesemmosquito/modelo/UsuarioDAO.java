package br.desenvolvedor.michelatz.cidadesemmosquito.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Michel Atz on 22/10/2016.
 */

public class UsuarioDAO extends SQLiteOpenHelper {

    public UsuarioDAO(Context context, String nomeDB) {
        super(context, nomeDB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE usuario(id INTEGER KEY," +
                "nome TEXT NOT NULL, email TEXT NOT NULL," +
                "ativo INTEGER NOT NULL);";

        db.execSQL(sql);

    }

    public void inserir(Usuario u) {

        ContentValues cv = new ContentValues();
        cv.put("nome",u.getNome());
        cv.put("email",u.getEmail());
        cv.put("ativo",1);

        SQLiteDatabase db = getWritableDatabase();
        db.insert("usuario",null,cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public ArrayList<Usuario> getUsuarios(){
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

        String sql = "Select * from usuario;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql,null);

        while(c.moveToNext()){
            Long id = c.getLong(c.getColumnIndex("id"));
            String nome = c.getString(c.getColumnIndex("nome"));
            String email = c.getString(c.getColumnIndex("email"));
            int ativo = c.getInt(c.getColumnIndex("ativo"));

            Usuario u = new Usuario(id,nome,email,ativo);
            usuarios.add(u);
        }
        return usuarios;
    }

    public int verificaBanco() {

        String Query = "Select * from usuario where ativo = '1';";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(Query, null);
        if(c.getCount() <= 0){
            return 0;
        }else {
            /*
            Usuario u = new Usuario();
            u.setNome(c.getString(c.getColumnIndex("nome")));
            u.setEmail(c.getString(c.getColumnIndex("email")));
            */

            return 1;
        }
    }
}
