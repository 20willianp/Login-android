package app.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActUserComum extends AppCompatActivity {
    private EditText edtNome, edtSenha, edtConfirSenha;
    private BDUser bdUser;
    private int idUser;
    private int op = 0; // 0 - atualizar ¨ 1 - excluir
    private boolean adm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_comum);

        edtNome = (EditText) findViewById(R.id.comum_edt_nome);
        edtSenha = (EditText) findViewById(R.id.comum_edt_senha);
        edtConfirSenha = (EditText) findViewById(R.id.comum_edt_confir_senha);

        bdUser = new BDUser(getBaseContext());
        initTela();
    }

    private void initTela(){
        Bundle parametro = getIntent().getExtras();

        adm = parametro.getBoolean("adm");
        edtNome.setText(parametro.getString("nome"));
        edtSenha.setText(parametro.getString("senha"));
        edtConfirSenha.setText(parametro.getString("senha"));
        idUser = parametro.getInt("id");
    }

    public void comumEvtAtualizar(View v){
        if(!isCampoVazio()){
            if(confirmaSenha()){
                alert("Tem certeza que deseja atualizar esse cadastro");
            }
        }
    }

    public void comumEvtExcluir(View v){
        alert("Tem certeza que deseja excluir esse cadastro?");
    }

    public void comumEvtVoltar(View v){
       voltarTela();
    }

    private void voltarTela(){
        Intent tela;

        if(adm){
            tela = new Intent(this, ActUserAdmin.class);
            tela.putExtra("primeiroAcesso", false);
        }else{
            tela = new Intent(this, ActPrincipal.class);
        }

        startActivity(tela);
    }

    private void alert(String mensagem){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Atenção");
        alert.setMessage(mensagem);

        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (op){
                    case 1: // atualizar

                        if(bdUser.alterar(idUser,
                                edtNome.getText().toString(), edtSenha.getText().toString())){
                            exibeToast("Dados  atualizados");
                        }else{
                            exibeToast("Erro ao atualizar dados");
                        }

                        break;
                    case 0: // excluir

                        if(bdUser.excluir(idUser)){
                            exibeToast("Usuario excluido");
                            voltarTela();
                        }else{
                            exibeToast("Erro ao excluir usuario");
                        }

                        break;
                }
            }
        });

        alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

            }
        });

        alert.show();
    }

    private boolean isCampoVazio(){
        if(edtNome.getText().toString().isEmpty() ||
                edtSenha.getText().toString().isEmpty() ||
                edtConfirSenha.getText().toString().isEmpty()){

            exibeToast("Preencha todos os campos");

            return true;
        }

        return false;
    }

    private boolean confirmaSenha(){
        if(edtSenha.getText().toString().equals(edtConfirSenha.getText().toString())){
            return true;
        }

        exibeToast("Erro ao confirmar senha");
        return false;
    }

    private void exibeToast(String texto){
        Toast.makeText(this,texto,Toast.LENGTH_LONG).show();
    }
}
