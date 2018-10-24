package br.desenvolvedor.michelatz.cidadesemmosquito;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import br.desenvolvedor.michelatz.cidadesemmosquito.abas.Equipe;
import br.desenvolvedor.michelatz.cidadesemmosquito.abas.Referencias;

public class Dicas extends AppCompatActivity {

    ImageView pro;
    int cont;

    private Toolbar mToobar;
    private Toolbar mToobarBotton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicas);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToobar = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle(" Cidade Sem Mosquito");
        mToobar.setLogo(R.drawable.icones);
        mToobar.setSubtitle(" Dicas");
        setSupportActionBar(mToobar);


        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton);
        //mToobarBotton.setBackgroundColor();
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_inicio:
                        it = new Intent(Dicas.this, PrincipalActivity.class);
                        break;

                    case R.id.acao_denuncia:
                        it = new Intent(Dicas.this, Denuncia.class);
                        break;

                    case R.id.acao_mapa:
                        it = new Intent(Dicas.this, Maps.class);
                        break;

                    case R.id.acao_minhas_denuncias:
                        it = new Intent(Dicas.this, ListaDenuncias.class);
                        break;

                    case R.id.acao_dicas:
                        it = new Intent(Dicas.this, Dicas.class);
                        break;
                }
                startActivity(it);
                finish();

                return true;
            }
        });

        mToobarBotton.inflateMenu(R.menu.menu_botton);

        pro = (ImageView) findViewById(R.id.imageView);

        cont = 1;

        if (cont == 1) {
            pro.setImageResource(R.drawable.img_1);
        }
    }

    public void proximo(View v) {

        cont++;

        if (cont < 1) {
            cont = 1;
        }

        if (cont == 1) {
            pro.setImageResource(R.drawable.img_1);


        }

        if (cont == 2) {
            pro.setImageResource(R.drawable.img_2);
        }

        if (cont == 3) {
            pro.setImageResource(R.drawable.img_3);

        }

        if (cont == 4) {
            pro.setImageResource(R.drawable.img_4);

        }

        if (cont == 5) {
            pro.setImageResource(R.drawable.img_5);

        }
        if (cont == 6) {
            pro.setImageResource(R.drawable.img_6);

        }
        if (cont == 7) {
            pro.setImageResource(R.drawable.img_7);

        }
        if (cont == 8) {
            pro.setImageResource(R.drawable.img_8);

        }


        if (cont > 8) {
            cont = 8;
        }
    }

    public void anterior(View v) {

        cont--;

        if (cont < 1) {
            cont = 1;
        }

        if (cont == 1) {
            pro.setImageResource(R.drawable.img_1);
        }

        if (cont == 2) {
            pro.setImageResource(R.drawable.img_2);
        }

        if (cont == 3) {
            pro.setImageResource(R.drawable.img_3);
        }

        if (cont == 4) {
            pro.setImageResource(R.drawable.img_4);
        }

        if (cont == 5) {
            pro.setImageResource(R.drawable.img_5);
        }
        if (cont == 6) {
            pro.setImageResource(R.drawable.img_6);
        }
        if (cont == 7) {
            pro.setImageResource(R.drawable.img_7);
        }
        if (cont == 8) {
            pro.setImageResource(R.drawable.img_8);
        }


        if (cont > 8) {
            cont = 8;
        }

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

public void botaoAjuda(View v){

    Spanned result;

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        result = Html.fromHtml("Nesta aba, são apresentadas ilustrações de como você pode eliminar os focos do mosquito em sua Residência.",Html.FROM_HTML_MODE_LEGACY);
    } else {
        result = Html.fromHtml("Nesta aba, são apresentadas ilustrações de como você pode eliminar os focos do mosquito em sua Residência.");
    }

    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setTitle("Dicas");
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

    @Override
    public void onBackPressed()
    {
        Intent it;
        it = new Intent(this, PrincipalActivity.class);
        startActivity(it);
        finish();
        super.onBackPressed();
    }


}
