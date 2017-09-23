package app.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by willian on 20/09/17.
 */

public class BDUser {
    private SQLiteDatabase db;
    private Banco banco;

    public BDUser(Context contexto){
        banco = new Banco(contexto);
    }

    //inseri um novo usuario
    public boolean inserir(String usuario, String senha, int perfil){
        ContentValues valores = new ContentValues();

        db = banco.getWritableDatabase();
        valores.put(banco.NOME_USER, usuario);
        valores.put(banco.SENHA_USER, senha);
        valores.put(banco.PERFIL, perfil);

        try {
            if(db.insert(banco.TB_USER, null, valores) != -1){
                return true;
            }

            return false;
        }catch (Exception e){
            return false;
        }finally {
            db.close();
        }
    }

    //altara o usuario
    public boolean alterar(int id, String nome, String senha){
        ContentValues valores = new ContentValues();
        String where = banco.ID_USER+" = "+id;

        db = banco.getWritableDatabase();
        valores.put(banco.NOME_USER, nome);
        valores.put(banco.SENHA_USER, senha);

        try{
            db.update(banco.TB_USER, valores, where, null);
            return true;
        }catch (Exception e){
            return false;
        }finally {
            db.close();
        }

    }

    //exclui o usuario
    public boolean excluir(int id){
        String where = banco.ID_USER+" = "+id;

        db = banco.getWritableDatabase();

        try {
            db.delete(banco.TB_USER, where, null);
            return true;
        }catch (Exception e){
            return false;
        }finally {
            db.close();
        }
    }

    //consulta usuario
    public Cursor consulta(String nome, String senha){
        Cursor cursor;
        String [] campos = {banco.NOME_USER, banco.SENHA_USER, banco.PERFIL, banco.ID_USER};
        String where = null;

        if(nome != null && senha != null){
            where = banco.NOME_USER+" = '"+nome+"' AND "+banco.SENHA_USER+" = '"+senha+"'";
        }

        db = banco.getReadableDatabase();
        cursor = db.query(banco.TB_USER, campos, where, null, null,null, null);

        //cursor so vai trazer null, se a consulta der erro
        if(cursor != null){
            cursor.moveToFirst();
        }

        db.close();
        return cursor;
    }
}
