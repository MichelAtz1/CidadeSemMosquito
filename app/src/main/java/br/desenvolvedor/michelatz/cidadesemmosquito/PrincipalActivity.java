package br.desenvolvedor.michelatz.cidadesemmosquito;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import br.desenvolvedor.michelatz.cidadesemmosquito.abas.Equipe;
import br.desenvolvedor.michelatz.cidadesemmosquito.abas.Referencias;

public class PrincipalActivity extends AppCompatActivity {
    private Toolbar mToobar;
    private final String nomeDB = "cidadeSemMosquito";

    public static final int FROM_HTML_MODE_COMPACT = 63;
    public static final int FROM_HTML_MODE_LEGACY = 0;
    public static final int FROM_HTML_OPTION_USE_CSS_COLORS = 256;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE = 32;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_DIV = 16;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_HEADING = 2;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST = 8;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM = 4;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH = 1;
    public static final int TO_HTML_PARAGRAPH_LINES_CONSECUTIVE = 0;
    public static final int TO_HTML_PARAGRAPH_LINES_INDIVIDUAL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        String emails = sharedpreferences.getString("emailKey", null);

        if(emails == null){
            Intent it;
            it = new Intent(this, Login.class);
            startActivity(it);
            finish();
        }
        mToobar = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle(" Cidade Sem Mosquito");
        mToobar.setLogo(R.drawable.icones);
        mToobar.setSubtitle(" Inicio");
        setSupportActionBar(mToobar);

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

    public void dicas(View v) {
        Intent it;
        it = new Intent(this, Dicas.class);
        startActivity(it);
        finish();
    }

    public void denuncias(View v) {
        Intent it;
        it = new Intent(this, Denuncia.class);
        startActivity(it);
        finish();
    }

    public void listaDenuncias(View v) {
        Intent it;
        it = new Intent(this, ListaDenuncias.class);
        startActivity(it);
        finish();
    }

    public void mapa(View v) {
        Intent it;
        it = new Intent(this, Maps.class);
        startActivity(it);
        finish();
    }

    public void botaoAjuda(View v) {
        Spanned result;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml("<b>Bem vindo ao Cidade Sem Mosquito!</b><p>Nesta primeira aba são apresentados quatro botões, que trazem as seguintes possibilidades:\n" +
                    "\t<p><b>• DENUNCIAR FOCO:</b><br>Possibilita que você denuncie um possível foco do mosquito Aedes Aegypti ao seu município;\n" +
                    "\t<p><b>• MAPA DOS FOCOS:</b><br>Mostra todas as denúncias realizadas em sua cidade;<p>\n" +
                    "\t<p><b>• MINHAS DENÚNCIAS:</b><br>São apresentadas as denúncias que você realizou, para que você possa editá-las ou excluí-las;\n" +
                    "\t<p><b>• DICAS:</b><br>São apresentadas ilustrações de como você pode eliminar os focos do mosquito em sua Residência.",Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml("<b>Bem vindo ao Cidade Sem Mosquito!</b><p text-align: justify;>Nesta primeira aba são apresentados quatro botões, que trazem as seguintes possibilidades:\n" +
                    "\t<p><b>• DENUNCIAR FOCO:</b><br>Possibilita que você denuncie um possível foco do mosquito Aedes Aegypti ao seu município;\n" +
                    "\t<p><b>• MAPA DOS FOCOS:</b><br>Mostra todas as denúncias realizadas em sua cidade;\n" +
                    "\t<p><b>• MINHAS DENÚNCIAS:</b><br>São apresentadas as denúncias que você realizou, para que você possa editá-las ou excluí-las;\n" +
                    "\t<p><b>• DICAS:</b><br>São apresentadas ilustrações de como você pode eliminar os focos do mosquito em sua Residência.");
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Inicio");
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
