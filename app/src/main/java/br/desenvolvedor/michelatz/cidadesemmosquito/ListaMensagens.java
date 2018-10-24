package br.desenvolvedor.michelatz.cidadesemmosquito;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import br.desenvolvedor.michelatz.cidadesemmosquito.ConexaoWEB.Config;
import br.desenvolvedor.michelatz.cidadesemmosquito.ConexaoWEB.RequestHandler;
import br.desenvolvedor.michelatz.cidadesemmosquito.abas.Equipe;
import br.desenvolvedor.michelatz.cidadesemmosquito.abas.Referencias;
import br.desenvolvedor.michelatz.cidadesemmosquito.modelo.Mensagem;

public class ListaMensagens extends AppCompatActivity{
    private String JSON_STRING;
    private Toolbar mToobar;
    private Toolbar mToobarBotton;
    private ListView listView;
    private AdapterListView adapterListView;
    private ArrayList<Mensagem> itens;
    private String id;
    private ImageView imgFotoDenuncia;
    private TextView txRua;
    private TextView txData;
    private TextView txStatus;
    private EditText edtComentario;
    public static String emailUsuario = "Isso é uma global";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_mensagens);

        imgFotoDenuncia = (ImageView) findViewById(R.id.fotoMensagem);
        txRua = (TextView) findViewById(R.id.txtRuaDenuncia);
        txData = (TextView) findViewById(R.id.txData);
        txStatus = (TextView) findViewById(R.id.txStatus);
        edtComentario = (EditText) findViewById(R.id.edtComentario);

        mToobar = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle(" Cidade Sem Mosquito");
        mToobar.setLogo(R.drawable.icones);
        mToobar.setSubtitle(" Dados da Denúncia");
        setSupportActionBar(mToobar);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_inicio:
                        it = new Intent(ListaMensagens.this, PrincipalActivity.class);
                        break;

                    case R.id.acao_denuncia:
                        it = new Intent(ListaMensagens.this, Denuncia.class);
                        break;

                    case R.id.acao_mapa:
                        it = new Intent(ListaMensagens.this, Maps.class);
                        break;

                    case R.id.acao_minhas_denuncias:
                        it = new Intent(ListaMensagens.this, ListaDenuncias.class);
                        break;

                    case R.id.acao_dicas:
                        it = new Intent(ListaMensagens.this, Dicas.class);
                        break;
                }
                startActivity(it);
                finish();

                return true;
            }
        });

        mToobarBotton.inflateMenu(R.menu.menu_botton);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("id");
        getJSON();
        listView = (ListView) findViewById(R.id.listViewMensagens);
        //listView.setScrollContainer(false);
    }

    public void deletaItem(View v) {
        adapterListView.removeItem((Integer) v.getTag());
        String idMensagem= adapterListView.idSelecionado;
        deletarMensagem(idMensagem);
    }

    private void deletarMensagem(final String idMens){
        class DeletarMensagem extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListaMensagens.this, "Deletando Mensagem", "Aguarde...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Intent intent = new Intent(ListaMensagens.this, ListaMensagens.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_DELETAR_MENSAGEM, idMens);
                return s;
            }
        }

        DeletarMensagem de = new DeletarMensagem();
        de.execute();
    }

    private void showEmployee(){

        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        itens = new ArrayList<Mensagem>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_ID);
                String mensagem = jo.getString(Config.TAG_MENSAGEM);
                String nome = jo.getString(Config.TAG_NOME);
                String email = jo.getString(Config.TAG_EMAIL);
                String iddenuncia = jo.getString(Config.TAG_ID_DENUNCIA);

                HashMap<String,String> employees = new HashMap<>();
                employees.put(Config.TAG_ID,id);
                employees.put(Config.TAG_MENSAGEM,mensagem);
                employees.put(Config.TAG_NOME,nome);
                employees.put(Config.TAG_EMAIL,email);
                employees.put(Config.TAG_ID_DENUNCIA,iddenuncia);

                Mensagem item1 = new Mensagem(id,mensagem,nome,email,iddenuncia);
                itens.add(item1);

                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapterListView = new AdapterListView(this, itens);
        listView.setAdapter(adapterListView);
        listView.setCacheColorHint(Color.TRANSPARENT);
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

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListaMensagens.this,"Buscando Denúncias","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEmployee();
                showDenuncia(s);
            }

            @Override
            protected String doInBackground(Void... params) {

                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                String emails = sharedpreferences.getString("emailKey", null);
                emailUsuario = emails;
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_MENSAGENS,id);
                JSON_STRING = s;
                String v = rh.sendGetRequestParam(Config.URL_GET_EMP, id);
                return v;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showDenuncia(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String imagem = c.getString(Config.TAG_IMAGEM);
            String rua = c.getString(Config.TAG_RUA);
            String data = c.getString(Config.TAG_DATA);
            String status = c.getString(Config.TAG_ACAO);

            Bitmap myBitmapAgain = decodeBase64(imagem);
            Bitmap imagemQuadrada = cortaImagem(myBitmapAgain);
            imgFotoDenuncia.setImageBitmap(imagemQuadrada);
            txRua.setText("  Endereço: "+rua);
            txData.setText("  Data: "+data);
            txStatus.setText("Status: "+status);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static Bitmap cortaImagem(Bitmap bmpOriginal) {
        int w = bmpOriginal.getWidth();
        int h = bmpOriginal.getHeight();

        if(w > h) {
            Bitmap croppedBitmap = Bitmap.createBitmap(bmpOriginal, 0, 0, bmpOriginal.getWidth() -20, bmpOriginal.getHeight() - 0);
            Bitmap tamanhoReduzidoImagem = Bitmap.createScaledBitmap(croppedBitmap, 250, 250, true);
            return tamanhoReduzidoImagem;
        }else if (w < h){
            Bitmap croppedBitmap = Bitmap.createBitmap(bmpOriginal, 0, 0, bmpOriginal.getWidth() - 0, bmpOriginal.getHeight() -20);
            Bitmap tamanhoReduzidoImagem = Bitmap.createScaledBitmap(croppedBitmap, 250, 250, true);
            return tamanhoReduzidoImagem;
        }else {
            return bmpOriginal;
        }
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void addComentario(View v){
        adicionarComentario();
    }

    private void adicionarComentario(){

        final String comentario = edtComentario.getText().toString();


        class adicionarComentario extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListaMensagens.this,"Salvando Comentário...","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ListaMensagens.this,s,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ListaMensagens.this, ListaMensagens.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }

            @Override
            protected String doInBackground(Void... params) {
                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                final String email = sharedpreferences.getString("emailKey", null);
                final String nomeUser = sharedpreferences.getString("nomeKey", null);

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_EMP_NOME,nomeUser);
                hashMap.put(Config.KEY_EMP_EMAIL,email);
                hashMap.put(Config.KEY_EMP_COMENTARIO,comentario);
                hashMap.put(Config.KEY_EMP_ID_DENUNCIA,id);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_INSERIR_MENSAGEM,hashMap);

                return s;
            }
        }

        adicionarComentario ue = new adicionarComentario();
        ue.execute();
    }

    @Override
    public void onBackPressed(){
        Intent it;
        it = new Intent(this, Maps.class);
        startActivity(it);
        finish();
        super.onBackPressed();  // optional depending on your needs
    }

    public void botaoAjuda(View v){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Minhas Denúncias");
        alertDialogBuilder.setMessage("Texto explicando sobre esta aba!");

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
