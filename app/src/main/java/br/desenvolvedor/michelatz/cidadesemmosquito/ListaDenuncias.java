package br.desenvolvedor.michelatz.cidadesemmosquito;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import br.desenvolvedor.michelatz.cidadesemmosquito.ConexaoWEB.Config;
import br.desenvolvedor.michelatz.cidadesemmosquito.ConexaoWEB.RequestHandler;
import br.desenvolvedor.michelatz.cidadesemmosquito.abas.Equipe;
import br.desenvolvedor.michelatz.cidadesemmosquito.abas.Referencias;

public class ListaDenuncias extends AppCompatActivity implements ListView.OnItemClickListener{

    private ListView listView;

    private String JSON_STRING;
    private Toolbar mToobar;
    private Toolbar mToobarBotton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_denuncias);


        mToobar = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle(" Cidade Sem Mosquito");
        mToobar.setLogo(R.drawable.icones);
        mToobar.setSubtitle(" Minhas Denúncias");
        setSupportActionBar(mToobar);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_inicio:
                        it = new Intent(ListaDenuncias.this, PrincipalActivity.class);
                        break;

                    case R.id.acao_denuncia:
                        it = new Intent(ListaDenuncias.this, Denuncia.class);
                        break;

                    case R.id.acao_mapa:
                        it = new Intent(ListaDenuncias.this, Maps.class);
                        break;

                    case R.id.acao_minhas_denuncias:
                        it = new Intent(ListaDenuncias.this, ListaDenuncias.class);
                        break;

                    case R.id.acao_dicas:
                        it = new Intent(ListaDenuncias.this, Dicas.class);
                        break;
                }
                startActivity(it);
                finish();

                return true;
            }
        });

        mToobarBotton.inflateMenu(R.menu.menu_botton);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        if(verificaConexao()){
            getJSON();
        }else{
            naoConectou();
        }
    }

    public boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    public void naoConectou(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Problemas ao Conectar");
        alertDialogBuilder.setMessage("Para acessar esta aba, seu celular precisa estar conectado a internet.\n\n Por Favor!\n Conecte-se a internet e tente novamente!");

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(ListaDenuncias.this, PrincipalActivity.class));
                        finish();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void showEmployee(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_ID);
                String rua = jo.getString(Config.TAG_RUA);
                String data2 = jo.getString(Config.TAG_DATA);
                String status = jo.getString(Config.TAG_ACAO);

                String data = data2+"\n";

                HashMap<String,String> employees = new HashMap<>();
                employees.put(Config.TAG_ID,id);
                employees.put(Config.TAG_RUA,rua);
                employees.put(Config.TAG_ACAO,status);
                employees.put(Config.TAG_DATA,data);
                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ListaDenuncias.this, list, R.layout.list_item,
                new String[]{Config.TAG_RUA,Config.TAG_ACAO,Config.TAG_DATA},
                new int[]{R.id.enderecoDenuncia,R.id.statusDenuncia,R.id.dataDenuncia});

        listView.setAdapter(adapter);
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
                loading = ProgressDialog.show(ListaDenuncias.this,"Buscando Denúncias","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... params) {
                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                String emails = sharedpreferences.getString("emailKey", null);
                final String email = emails;

                HashMap<String,String> params2 = new HashMap<>();
                params2.put(Config.KEY_EMP_EMAIL,email);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.URL_GET_ALL, params2);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,GerenciaDenuncia.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String empId = map.get(Config.TAG_ID).toString();
        intent.putExtra(Config.EMP_ID,empId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        Intent it;
        it = new Intent(this, PrincipalActivity.class);
        startActivity(it);
        finish();
        super.onBackPressed();
    }

    public void botaoAjuda(View v){

        Spanned result;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml("Nesta aba, são listadas todas as denúncias que você já realizou neste aplicativo.\n" +
                    "\t<p>Clique em alguma das denúncias para poder editá-la ou excluí-la.",Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml("Nesta aba, são listadas todas as denúncias que você já realizou neste aplicativo.\n" +
                    "\t<p>Clique em alguma das denúncias para poder editá-la ou excluí-la.\n");
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Minhas Denúncias");
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
