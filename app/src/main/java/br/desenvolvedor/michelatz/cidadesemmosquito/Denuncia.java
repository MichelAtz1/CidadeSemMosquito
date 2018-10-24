package br.desenvolvedor.michelatz.cidadesemmosquito;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import br.desenvolvedor.michelatz.cidadesemmosquito.ConexaoWEB.Config;
import br.desenvolvedor.michelatz.cidadesemmosquito.ConexaoWEB.RequestHandler;
import br.desenvolvedor.michelatz.cidadesemmosquito.abas.Equipe;
import br.desenvolvedor.michelatz.cidadesemmosquito.abas.Referencias;

public class Denuncia extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private EditText txtCidade;
    private EditText edtNumero;
    private EditText edtBairro;
    private EditText edtRua;
    private EditText edtPonto;
    private EditText edtDescricao;
    private Toolbar mToobar;
    private Toolbar mToobarBotton;

    ObtainGPS gps;
    String numeroFoto = "inicial";
    String ruaFoto = "inicial";
    String bairroFoto = "inicial";
    String cidadeFoto = "inicial";
    String enderecoFoto = "inicial";
    String estadoFoto = "inicial";
    String caminhoImagem = "inicial";
    String longitude = "inicial";
    String latitude = "inicial";
    String imagemBase64;


    private Address endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia);

        mToobar = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle(" Cidade Sem Mosquito");
        mToobar.setLogo(R.drawable.icon_logo);
        mToobar.setSubtitle(" Denúncia");
        setSupportActionBar(mToobar);


        txtCidade = (EditText) findViewById(R.id.edtCidade);
        edtRua = (EditText) findViewById(R.id.edtRua);
        edtNumero = (EditText) findViewById(R.id.edtNumero);
        edtBairro = (EditText) findViewById(R.id.edtBairro);
        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtPonto = (EditText) findViewById(R.id.edtPonto);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_inicio:
                        it = new Intent(Denuncia.this, PrincipalActivity.class);
                        break;

                    case R.id.acao_denuncia:
                        it = new Intent(Denuncia.this, Denuncia.class);
                        break;

                    case R.id.acao_mapa:
                        it = new Intent(Denuncia.this, Maps.class);
                        break;

                    case R.id.acao_minhas_denuncias:
                        it = new Intent(Denuncia.this, ListaDenuncias.class);
                        break;

                    case R.id.acao_dicas:
                        it = new Intent(Denuncia.this, Dicas.class);
                        break;
                }
                startActivity(it);
                finish();

                return true;
            }
        });

        mToobarBotton.inflateMenu(R.menu.menu_botton);

        if(verificaConexao()){
            try {
                getLocalization();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            naoConectou();
        }
    }

    public void naoConectou(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Problemas ao Conectar");
        alertDialogBuilder.setMessage("Para acessar esta aba, seu celular precisa estar conectado a internet.\n\n Por Favor!\n Conecte-se a internet e tente novamente!");

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(Denuncia.this, PrincipalActivity.class));
                        finish();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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

    public  boolean verificaConexao() {
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

    public void getLocalization() throws IOException {
        gps = new ObtainGPS(Denuncia.this);


        if (GetLocalization(Denuncia.this)) {
            if (gps.canGetLocation()) {

                longitude = String.valueOf(gps.getLongitude());
                latitude = String.valueOf(gps.getLatitude());
                endereco = buscaEndereco(gps.getLatitude(),gps.getLongitude());

                numeroFoto = endereco.getFeatureName();
                ruaFoto = endereco.getThoroughfare();
                bairroFoto = endereco.getSubLocality();
                //bairroFoto = "Camobi";
                cidadeFoto = endereco.getLocality();
                estadoFoto = endereco.getAdminArea();
                /*
                Log.d("Numero: ", "" + numeroFoto);
                Log.d("Rua: ", "" + ruaFoto);
                Log.d("Bairro :", "" + bairroFoto);
                Log.d("Cidade: ", "" + cidadeFoto);
                Log.d("Estado: ", "" + estadoFoto);
                */
                txtCidade.setText(cidadeFoto);
                edtNumero.setText(numeroFoto);
                edtBairro.setText(bairroFoto);
                edtRua.setText(ruaFoto);

            } else {
                AlertDialog.Builder erroLocation = new AlertDialog.Builder(this);
                erroLocation.setTitle("Localização não encontrada");
                erroLocation.setMessage("Sua Localização não foi encontrada!! Tente novamente!");
                erroLocation.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                startActivity(new Intent(Denuncia.this, PrincipalActivity.class));
                                finish();
                            }
                        });
                AlertDialog alertDialog = erroLocation.create();
                alertDialog.show();

                gps.showSettingsAlert();
            }

        }
    }

    public boolean GetLocalization(Context context) {
        int REQUEST_PERMISSION_LOCALIZATION = 221;
        boolean res = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                res = false;
                ActivityCompat.requestPermissions((Activity) context, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_LOCALIZATION);
            }
        }
        return res;
    }

    public Address buscaEndereco(double latitude, double longitude) throws IOException{

        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;
        geocoder = new Geocoder(getApplicationContext());
        addresses = geocoder.getFromLocation(latitude,longitude,1);
        if (addresses.size()>0){
            address = addresses.get(0);
        }

        return  address;
    }

    public void salvar(View v){
        if(caminhoImagem.equals("inicial")){
            Toast.makeText(Denuncia.this, "A Foto eh Obrigatória ", Toast.LENGTH_SHORT).show();

        }else{
            addEmployee();
        }

    }


    private void addEmployee(){

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
        String dateString = sdf.format(date);

        SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        String emails = sharedpreferences.getString("emailKey", null);

        final String numero = edtNumero.getText().toString();;
        final String rua = edtRua.getText().toString();
        final String bairro = edtBairro.getText().toString();
        final String cidade = cidadeFoto;
        final String estado = estadoFoto;
        final String email = emails;
        final String data = dateString;
        final String imagem = imagemBase64;
        final String descricao = edtDescricao.getText().toString();
        final String ponto = edtPonto.getText().toString();
        final String acao = "Nâo Avaliado";
        final String lat = latitude;
        final String longi = longitude;

        class AddEmployee extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Denuncia.this,"Denuncia sendo salva...","Aguarde...",false,false);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Denuncia.this,s,Toast.LENGTH_LONG).show();

                AlertDialog.Builder DenunciaSalva = new AlertDialog.Builder(Denuncia.this);
                DenunciaSalva.setTitle("Denúncia Realizada com Sucesso!");
                DenunciaSalva.setMessage("Sua Denúncia de um possivél foco, foi enviada ao seu município!\n\nPara edita-lá ou exclui-lá, por favor acesse a aba \"MINHAS DENÚNCIAS\"!");
                DenunciaSalva.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                startActivity(new Intent(Denuncia.this, PrincipalActivity.class));
                                finish();
                            }
                        });
                AlertDialog alertDialog = DenunciaSalva.create();
                alertDialog.show();

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_EMP_CIDADE,cidade);
                params.put(Config.KEY_EMP_RUA,rua);
                params.put(Config.KEY_EMP_BAIRRO,bairro);
                params.put(Config.KEY_EMP_NUMERO,numero);
                params.put(Config.KEY_EMP_ESTADO,estado);
                params.put(Config.KEY_EMP_IMAGEM,imagem);
                params.put(Config.KEY_EMP_DESCRICAO,descricao);
                params.put(Config.KEY_EMP_DATA,data);
                params.put(Config.KEY_EMP_EMAIL,email);
                params.put(Config.KEY_EMP_ACAO,acao);
                params.put(Config.KEY_EMP_LATITUDE,lat);
                params.put(Config.KEY_EMP_LONGITUDE,longi);
                params.put(Config.KEY_EMP_PONTO,ponto);

                /*
                Log.d("Longitude",longi);
                Log.d("Longitude",lat);
                Log.d("Longitude",numero);
                Log.d("Longitude",bairro);
                Log.d("Longitude",email);
                Log.d("Longitude",ponto);
                Log.d("Longitude",rua);
                Log.d("Longitude",estado);
                */

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }

    public void foto(View v){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Denuncia.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
            return;
        }
        Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it, 0);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(Denuncia.this, "Desculpe!!! você não pode usar este aplicativo sem conceder permissão!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        ImageView iv = (ImageView) findViewById(R.id.foto);

        Log.d("teste-Data1", "Data= "+data);
        Log.d("teste-Data2", "data");
        Log.d("teste-Data3", "terceiro: "+data.getExtras().get("data"));

        Bitmap bp = (Bitmap) data.getExtras().get("data");
        Bitmap imagemQuadrada = cortaImagem(bp);
        imagemBase64 = encodeToBase64(bp, Bitmap.CompressFormat.JPEG, 100);
        caminhoImagem ="Imagem ok";
        iv.setImageBitmap(imagemQuadrada);
        //iv.setImageBitmap(bp);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
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
            result = Html.fromHtml("Nesta aba, é possível você realizar denúncias ao seu município.\n" +
                    "\t<p>Por favor, retire uma foto do possível criadouro do mosquito e preencha os dados necessários.\n" +
                    "\t<p>Alguns dados já serão preenchidos através de sua Geo-localização.",Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml("Nesta aba, é possível você realizar denúncias ao seu município.\n" +
                    "\t<p>Por favor, retire uma foto do possível criadouro do mosquito e preencha os dados necessários.\n" +
                    "\t<p>Alguns dados já serão preenchidos através de sua Geo-localização.");
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Realizar Denúncia");
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