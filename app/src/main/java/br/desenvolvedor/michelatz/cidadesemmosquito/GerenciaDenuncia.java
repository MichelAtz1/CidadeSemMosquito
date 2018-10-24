package br.desenvolvedor.michelatz.cidadesemmosquito;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.desenvolvedor.michelatz.cidadesemmosquito.ConexaoWEB.Config;
import br.desenvolvedor.michelatz.cidadesemmosquito.ConexaoWEB.RequestHandler;
import br.desenvolvedor.michelatz.cidadesemmosquito.abas.Equipe;
import br.desenvolvedor.michelatz.cidadesemmosquito.abas.Referencias;

public class GerenciaDenuncia extends AppCompatActivity {

    private EditText edtID;
    private EditText edtDescricao;
    private EditText edtCidade;
    private EditText edtRua;
    private EditText edtNumero;
    private EditText edtBairro;
    private EditText edtPonto;

    private Toolbar mToobar;
    private Toolbar mToobarBotton;
    private ImageView imgFotoDenuncia;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerencia_denuncia);

        mToobar = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle(" Cidade Sem Mosquito");
        mToobar.setLogo(R.drawable.icones);
        mToobar.setSubtitle(" Denúncia");
        setSupportActionBar(mToobar);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_inicio:
                        it = new Intent(GerenciaDenuncia.this, PrincipalActivity.class);
                        break;

                    case R.id.acao_denuncia:
                        it = new Intent(GerenciaDenuncia.this, Denuncia.class);
                        break;

                    case R.id.acao_mapa:
                        it = new Intent(GerenciaDenuncia.this, Maps.class);
                        break;

                    case R.id.acao_minhas_denuncias:
                        it = new Intent(GerenciaDenuncia.this, ListaDenuncias.class);
                        break;

                    case R.id.acao_dicas:
                        it = new Intent(GerenciaDenuncia.this, Dicas.class);
                        break;
                }
                startActivity(it);
                finish();

                return true;
            }
        });

        mToobarBotton.inflateMenu(R.menu.menu_botton);

        Intent intent = getIntent();

        id = intent.getStringExtra(Config.EMP_ID);

        edtDescricao = (EditText) findViewById(R.id.edtDescricao2);
        edtCidade = (EditText) findViewById(R.id.edtCidade2);
        edtRua= (EditText) findViewById(R.id.edtRua2);
        edtNumero= (EditText) findViewById(R.id.edtNumero2);
        edtBairro= (EditText) findViewById(R.id.edtBairro2);
        edtPonto = (EditText) findViewById(R.id.edtPonto2);
        imgFotoDenuncia = (ImageView) findViewById(R.id.imgFoto2);


        getEmployee();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.acao_inicio_cima:
                startActivity(new Intent(this,PrincipalActivity.class));
                finish();
                return true;
            case R.id.acao_denuncia_cima:
                startActivity(new Intent(this,Denuncia.class));
                finish();
                return true;
            case R.id.acao_mapa_cima:
                startActivity(new Intent(this,Maps.class));
                finish();
                return true;
            case R.id.acao_minhas_denuncias_cima:
                startActivity(new Intent(this,ListaDenuncias.class));
                finish();
                return true;
            case R.id.acao_dicas_cima:
                startActivity(new Intent(this,Dicas.class));
                finish();
                return true;
            case R.id.acao_referencia_cima:
                startActivity(new Intent(this,Referencias.class));
                finish();
                return true;
            case R.id.acao_equipe_cima:
                startActivity(new Intent(this, Equipe.class));
                finish();
                return true;
            case R.id.acao_logout_cima:
                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove("nomeKey");
                editor.remove("emailKey");

                editor.commit();
                editor.clear();

                startActivity(new Intent(this,Login.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getEmployee(){
        class GetEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(GerenciaDenuncia.this,"Buscando Dados...","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEmployee(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_EMP,id);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    private void showEmployee(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String rua = c.getString(Config.TAG_RUA);
            String numero = c.getString(Config.TAG_NUMERO);
            String bairro = c.getString(Config.TAG_BAIRRO);
            String descricao = c.getString(Config.TAG_DESCRICAO);
            String cidade = c.getString(Config.TAG_CIDADE);
            String ponto = c.getString(Config.TAG_PONTO);
            String imagem = c.getString(Config.TAG_IMAGEM);

            Bitmap myBitmapAgain = decodeBase64(imagem);
            Bitmap imagemQuadrada = cortaImagem(myBitmapAgain);

            edtDescricao.setText(descricao);
            edtCidade.setText(cidade);
            edtRua.setText(rua);
            edtNumero.setText(numero);
            edtBairro.setText(bairro);
            edtPonto.setText(ponto);
            imgFotoDenuncia.setImageBitmap(imagemQuadrada);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static Bitmap cortaImagem(Bitmap bmpOriginal) {
        int w = bmpOriginal.getWidth();
        int h = bmpOriginal.getHeight();

        if(w > h) {
            int resto = w-h;
            //Bitmap croppedBitmap = Bitmap.createBitmap(bmpOriginal, 0, 0, bmpOriginal.getWidth() - resto, bmpOriginal.getHeight() - 10);
            Bitmap croppedBitmap = Bitmap.createBitmap(bmpOriginal, 0, 0, bmpOriginal.getWidth() -20, bmpOriginal.getHeight() - 0);
            Bitmap tamanhoReduzidoImagem = Bitmap.createScaledBitmap(croppedBitmap, 250, 250, true);
            return tamanhoReduzidoImagem;
        }else if (w < h){
            int resto = h-w;
            Bitmap croppedBitmap = Bitmap.createBitmap(bmpOriginal, 0, 0, bmpOriginal.getWidth() - 0, bmpOriginal.getHeight() -20);
            Bitmap tamanhoReduzidoImagem = Bitmap.createScaledBitmap(croppedBitmap, 250, 250, true);
            return tamanhoReduzidoImagem;
        }else {
            return bmpOriginal;
        }
    }

    private void editarDenuncia(){

        final String rua = edtRua.getText().toString().trim();
        final String ponto = edtPonto.getText().toString().trim();
        final String numero= edtNumero.getText().toString().trim();
        final String descricao= edtDescricao.getText().toString().trim();
        final String bairro= edtBairro.getText().toString().trim();

        class EditarDenuncia extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(GerenciaDenuncia.this,"Editando Dados...","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(GerenciaDenuncia.this,s,Toast.LENGTH_LONG).show();
                startActivity(new Intent(GerenciaDenuncia.this,ListaDenuncias.class));
                finish();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_EMP_ID,id);
                hashMap.put(Config.KEY_EMP_PONTO,ponto);
                hashMap.put(Config.KEY_EMP_NUMERO,numero);
                hashMap.put(Config.KEY_EMP_RUA,rua);
                hashMap.put(Config.KEY_EMP_BAIRRO,bairro);
                hashMap.put(Config.KEY_EMP_DESCRICAO,descricao);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_EMP,hashMap);

                return s;
            }
        }

        EditarDenuncia ue = new EditarDenuncia();
        ue.execute();
    }

    private void deletarDenuncia(){
        class DeletarDenuncia extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(GerenciaDenuncia.this, "Deletando Dados", "Aguarde...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(GerenciaDenuncia.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_DELETE_EMP, id);
                return s;
            }
        }

        DeletarDenuncia de = new DeletarDenuncia();
        de.execute();
    }

    private void confirmDeleteEmployee(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Tem certeza que deseja deletar esta denúncia?");

        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deletarDenuncia();
                        startActivity(new Intent(GerenciaDenuncia.this,ListaDenuncias.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void editar(View v){
        editarDenuncia();
    }

    public void excluir(View v){
        confirmDeleteEmployee();
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public void onBackPressed() {
        Intent it;
        it = new Intent(this, ListaDenuncias.class);
        startActivity(it);
        finish();
        super.onBackPressed();
    }

    public void botaoAjuda(View v){

        Spanned result;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml("Nesta aba, você pode excluir ou editar uma denúncia.\n" +
                    "\t<p>Altere as informações que você deseja mudar e clique no botão Editar para salvar as alterações, ou clique em Excluir para apagar a denúncia.",Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml("Nesta aba, você pode excluir ou editar uma denúncia.\n" +
                    "\t<p>Altere as informações que você deseja mudar e clique no botão Editar para salvar as alterações, ou clique em Excluir para apagar a denúncia.");
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Gerenciar Denúncia");
        alertDialogBuilder.setMessage(result);

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}