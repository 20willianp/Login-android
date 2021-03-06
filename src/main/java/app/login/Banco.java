package app.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Banco extends SQLiteOpenHelper{

    public static final String NOME_BANCO = "login.db";
    public static final String TB_USER = "user";
    public static final String ID_USER = "_id";
    public static final String NOME_USER = "nome";
    public static final String SENHA_USER = "senha";
    public static final String PERFIL = "adm";
    public static final int VERSAO_DB = 1;


    public Banco(Context contexto){
        super(contexto, NOME_BANCO, null, VERSAO_DB);
    }

    //cria o banco de dados caso ele não exista.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDB) {
        sqLiteDB.execSQL("CREATE TABLE "+TB_USER+" ("+ID_USER+" integer primary key autoincrement, "
                +NOME_USER+" text, "+SENHA_USER+" text, "+PERFIL+" integer )");
    }

    //se a versão do banco de dados for diferente a qual está instalado no APP, será atualizado.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDB, int vAntiga, int vNova) {
        //Fazer correção desse metodo, essa implementação poderá ocorrer falhas.
        sqLiteDB.execSQL("DROP TABLE IF EXISTS "+TB_USER);
        onCreate(sqLiteDB);
    }
}
