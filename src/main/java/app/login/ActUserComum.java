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

    //pega os dados do usuario vindo da tela anterior
    private void initTela(){
        Bundle parametro = getIntent().getExtras();

        adm = parametro.getBoolean("adm");
        edtNome.setText(parametro.getString("nome"));
        edtSenha.setText(parametro.getString("senha"));
        edtConfirSenha.setText(parametro.getString("senha"));
        idUser = parametro.getInt("id");
    }

    //atualiza os dados do usuario, atribui o valor "0" para flag "op" para realizar a atualização
    public void comumEvtAtualizar(View v){
        if(!isCampoVazio()){
            if(confirmaSenha()){
                op = 0; //atualizar
                alert("Tem certeza que deseja atualizar esse cadastro");
            }
        }
    }
    //exclui os dados do usuario, atribui o valor "1" para flag "op" para realizar a exclusão
    //verifica se o usuario que está logado não está tentando excluir ele mesmo
    public void comumEvtExcluir(View v){
        if(ActPrincipal.idAdm == idUser){
            exibeToast("Não é possivel excluir, este usuário está logado");
        }else{
            op = 1; //excluir
            alert("Tem certeza que deseja excluir esse cadastro?");
        }
    }
    // volta para a tela anterior, Activity ActUserAdmin ou ActPrincipal.
    public void comumEvtVoltar(View v){
       voltarTela();
    }

    // volta para a tela anterior, Activity ActUserAdmin quando um usuario adminstrador estiver
    // logado ou ActPrincipal quando um usuario comum estiver logado.
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

    //um alerte de confirmação para as ações de exclusão ou alteração
    private void alert(String mensagem){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Atenção");
        alert.setMessage(mensagem);

        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (op){
                    case 0: // atualizar

                        if(bdUser.alterar(idUser,
                                edtNome.getText().toString(), edtSenha.getText().toString())){
                            exibeToast("Dados  atualizados");
                        }else{
                            exibeToast("Erro ao atualizar dados");
                        }

                        break;
                    case 1: // excluir

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

    //verifica se os campos estão vazios
    private boolean isCampoVazio(){
        if(edtNome.getText().toString().isEmpty() ||
                edtSenha.getText().toString().isEmpty() ||
                edtConfirSenha.getText().toString().isEmpty()){

            exibeToast("Preencha todos os campos");

            return true;
        }

        return false;
    }

    //realiza a confirmação de senha
    private boolean confirmaSenha(){
        if(edtSenha.getText().toString().equals(edtConfirSenha.getText().toString())){
            return true;
        }

        exibeToast("Erro ao confirmar senha");
        return false;
    }

    // FEEDBACK
    private void exibeToast(String texto){
        Toast.makeText(this,texto,Toast.LENGTH_LONG).show();
    }
}
