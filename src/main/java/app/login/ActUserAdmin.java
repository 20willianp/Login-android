/*
* O usuario administrador não pode excluir ele mesmo
*
*
*
*
*
* */
package app.login;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.Toast;

public class ActUserAdmin extends AppCompatActivity {
    private EditText edtNome, edtSenha, edtConfSenha;
    private Switch swtAdmin;
    private Cursor cursor;
    private BDUser bdUser;
    private ListView lvDados;
    private boolean primeiroAcesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_admin);

        edtNome = (EditText) findViewById(R.id.adm_edt_nome);
        edtConfSenha = (EditText) findViewById(R.id.adm_edt_conf_senha);
        edtSenha = (EditText) findViewById(R.id.adm_edt_senha);
        swtAdmin = (Switch) findViewById(R.id.swt_admin);
        lvDados = (ListView) findViewById(R.id.adm_lv_dados);

        bdUser = new BDUser(getBaseContext());

        primeiroAcesso();
        carregaListView();
        eventLvDados();
    }

    public void admEvtSalvar(View v){
        if(!isCampoEmpety()){ //verifica se os campos estão vazios
            if(confirmaSenha()){ //confirma a senha do usuario

                if(bdUser.inserir(
                        edtNome.getText().toString(),
                        edtSenha.getText().toString(),
                        convertBoolInt())){

                    exibeToast("Usuario cadastrado");
                    swtAdmin.setSelected(false); //volta o componente para posição padrao
                    carregaListView(); // atualiza a listView
                    limpaCampos(); // limpa os campos

                    if(primeiroAcesso){
                        startActivity(new Intent(this,ActPrincipal.class));
                    }
                }else{
                    exibeToast("Erro ao inserir usuario");
                }

            }
        }
    }

    //faz logof
    public void admEvtVoltar(View v){
        startActivity(new Intent(this,ActPrincipal.class));
    }

    private void carregaListView(){
        cursor = bdUser.consulta(null,null); //retorna todos os dados cadastrados

        String [] campos = new String [] {Banco.NOME_USER, Banco.SENHA_USER};
        int [] idTxt = new int[] {R.id.txt_list_user,R.id.txt_list_senha};

        SimpleCursorAdapter adpt = new SimpleCursorAdapter(
                getBaseContext(), R.layout.lista_dados,cursor,campos,idTxt,0);
        lvDados.setAdapter(adpt);
    }

    // quando o usuario clicar, é passado os dados que foram clicados para a activity ActUserComum
    private void eventLvDados(){
        lvDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long id) {
                Intent tela;

                cursor.moveToPosition(posicao);
                tela = new Intent(ActUserAdmin.this,ActUserComum.class);
                tela.putExtra("id",cursor.getInt(cursor.getColumnIndexOrThrow(Banco.ID_USER)));
                tela.putExtra("nome",cursor.getString(cursor.getColumnIndexOrThrow(Banco.NOME_USER)));
                tela.putExtra("senha",cursor.getString(cursor.getColumnIndexOrThrow(Banco.SENHA_USER)));
                tela.putExtra("adm", true);
                startActivity(tela);
            }
        });
    }

    //verifica se é o primeiro acesso do usuario no sistema
    private void primeiroAcesso(){
        Bundle parametro = getIntent().getExtras();
        primeiroAcesso = parametro.getBoolean("primeiroAcesso");
        if(primeiroAcesso){
            swtAdmin.setVisibility(View.INVISIBLE);
            swtAdmin.setChecked(true);
        }
    }

    /* >>>>>>>>>>>>>> CONVERTER BOOL E MENSAGEM DE FEEDBACK <<<<<<<<<<<<<<<*/
    private int convertBoolInt(){
        return swtAdmin.isChecked()?1:0;
    }

    private void exibeToast(String texto){
        Toast.makeText(this,texto,Toast.LENGTH_SHORT).show();
    }

    /* >>>>>>>>> METODOS DE VERIFICAÇÂO  <<<<<<<<<<<<<<<<
    * (CONFIRMAÇÃO DE SENHA, CAMPOS VAZIOS E LIMPAR CAMPOS)*/

    private boolean isCampoEmpety(){

        if(edtConfSenha.getText().toString().isEmpty() ||
                edtSenha.getText().toString().isEmpty() ||
                edtNome.getText().toString().isEmpty()){

            exibeToast("Preencha todos os campos");
            return true;
        }

        return false;
    }


    private boolean confirmaSenha(){

        if(edtSenha.getText().toString().equals(
                edtConfSenha.getText().toString())){

            return true;
        }

        exibeToast("Erro ao confirmar senha");
        return false;
    }

    private void limpaCampos(){
        edtNome.setText("");
        edtSenha.setText("");
        edtConfSenha.setText("");
        swtAdmin.setChecked(false);
        edtNome.requestFocus();
    }
}
