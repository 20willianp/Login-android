package app.login;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActPrincipal extends AppCompatActivity {
    private EditText edtNome, edtSenha;
    private Cursor cursor;
    private BDUser bdUser;
    public static int idAdm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_principal);

        edtNome = (EditText) findViewById(R.id.comum_edt_user);
        edtSenha = (EditText) findViewById(R.id.comum_edt_senha);

        bdUser= new BDUser(getBaseContext());
    }

    public void evtLogar(View v){
        if(isCadastrado()){
            // faça o login * PRIMEIRO ACESSO = TRUE, SE PERFIL FOI 1 é ADM = TRUE

            if(cursor.getInt(cursor.getColumnIndexOrThrow(Banco.PERFIL)) == 1){

                ActPrincipal.idAdm = cursor.getInt(cursor.getColumnIndexOrThrow(Banco.ID_USER));
                iniciaTelaUser(false,true);// não é primeiro acesso, tela de user adm
            }else{
                iniciaTelaUser(false,false);// nao é primeiro acesso, tela de user comum
            }

        }else{

            if(isUserPadrao()){
                // o usuario digitou admin - admin
                if(isTabelaVazia()){
                    //faca o login de usuario padrao
                    iniciaTelaUser(true,true); // primeiro acesso, tela de user adm
                }else{
                    // login incorreto
                    exibeToast("Usuario ou senha incorreto");
                }

            }else{
                // login incorreto
                exibeToast("Usuario ou senha incorreto");
            }
        }
    }

    //verifica se tem algum usuario cadastrado na base de dados, caso tenha o usuario nao pode
    // logar com o "nome=admin, senha=admin", caso nao tenha, o usuario loga com admin, admin
    private boolean isTabelaVazia(){
        cursor = bdUser.consulta(null, null);

        return cursor.getCount() == 0;
    }

    //verifica se o nome e senha informado existe na base de dados
    private boolean isCadastrado(){
        cursor = bdUser.consulta(edtNome.getText().toString(),edtSenha.getText().toString());

        return cursor.getCount() > 0;
    }

    // verifica se o usuario digitou admin, admin
    private boolean isUserPadrao(){
        return edtNome.getText().toString().equals("admin")
                && edtSenha.getText().toString().equals("admin");
    }

    // se for tela de admin TRUE, se for tela de usuario comum FALSE
    private void iniciaTelaUser(boolean primeiroAcesso, boolean telaAdmin){
        Intent tela;

        if(telaAdmin){
            tela = new Intent(this, ActUserAdmin.class);
            tela.putExtra("primeiroAcesso", primeiroAcesso);

        }else{
            tela = new Intent(this, ActUserComum.class);
            tela.putExtra("adm",false);
            tela.putExtra("id",cursor.getInt(cursor.getColumnIndexOrThrow(Banco.ID_USER)));
            tela.putExtra("nome",cursor.getString(cursor.getColumnIndexOrThrow(Banco.NOME_USER)));
            tela.putExtra("senha",cursor.getString(cursor.getColumnIndexOrThrow(Banco.SENHA_USER)));
        }

        startActivity(tela);
        limpaCampo();
    }

    /* >>>>>>>>> FEEDBACK & LIMPAR CAMPOS <<<<<<<<<<<<<<<<
     * >>>>>>>>> FEEDBACK & LIMPAR CAMPOS <<<<<<<<<<<<<<<<*/
    private void exibeToast(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_LONG).show();
    }

    private void limpaCampo(){
        edtSenha.setText("");
        edtNome.setText("");
        edtNome.requestFocus();
    }
}
