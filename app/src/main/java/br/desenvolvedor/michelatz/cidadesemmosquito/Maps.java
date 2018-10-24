package br.desenvolvedor.michelatz.cidadesemmosquito;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.games.GamesMetadata;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import br.desenvolvedor.michelatz.cidadesemmosquito.ConexaoWEB.Config;
import br.desenvolvedor.michelatz.cidadesemmosquito.ConexaoWEB.RequestHandler;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;
    ObtainGPS gps;

    private Toolbar mToobarBotton;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_inicio:
                        it = new Intent(Maps.this, PrincipalActivity.class);
                        break;

                    case R.id.acao_denuncia:
                        it = new Intent(Maps.this, Denuncia.class);
                        break;

                    case R.id.acao_mapa:
                        it = new Intent(Maps.this, Maps.class);
                        break;

                    case R.id.acao_minhas_denuncias:
                        it = new Intent(Maps.this, ListaDenuncias.class);
                        break;

                    case R.id.acao_dicas:
                        it = new Intent(Maps.this, Dicas.class);
                        break;
                }
                startActivity(it);
                finish();

                return true;
            }
        });

        mToobarBotton.inflateMenu(R.menu.menu_botton);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            getLocalization();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(verificaConexao()){
            getJSON();
        }else{
            naoConectou();
        }
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

    public void naoConectou(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Problemas ao Conectar");
        alertDialogBuilder.setMessage("Para acessar esta aba, seu celular precisa estar conectado a internet.\n\n Por Favor!\n Conecte-se a internet e tente novamente!");

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(Maps.this, PrincipalActivity.class));
                        finish();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void getLocalization() throws IOException {
        gps = new ObtainGPS(Maps.this);

        if (GetLocalization(Maps.this)) {
            if (gps.canGetLocation()) {
                marcadores(new LatLng(gps.getLatitude(),gps.getLongitude()), "Você esta aqui!", "Bem vindo ao Cidade sem Mosquito!", true, "Local");
            } else {
                AlertDialog erroLocation = new AlertDialog.Builder(this).create();
                erroLocation.setTitle("Localização não encontrada");
                erroLocation.setMessage("Sua Localização não foi encontrada!! Tente novamente!");
                erroLocation.show();
                gps.showSettingsAlert();
            }
        }
    }

    public boolean GetLocalization(Context context) {
        int REQUEST_PERMISSION_LOCALIZATION = 221;
        boolean res = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                res = false;
                ActivityCompat.requestPermissions((Activity) context, new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_LOCALIZATION);
            }
        }
        return res;
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Maps.this,"Buscando Denúncias","Aguarde...",false,false);
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
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_PONTOS_DENUNCIA);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showEmployee(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                String idSelecionado = jo.getString(Config.TAG_ID);

                String longitude = jo.getString(Config.TAG_LONGITUDE);
                Double longitudeDoub = Double.parseDouble(longitude);

                String latitude = jo.getString(Config.TAG_LATITUDE);
                Double latitudeDoub = Double.parseDouble(latitude);

                String acao = jo.getString(Config.TAG_ACAO);

                marcadores(new LatLng(latitudeDoub, longitudeDoub), "Status: "+acao,"Clique aqui para saber mais!"+idSelecionado, false, acao);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void marcadores(LatLng latlong, String titulo, String snnipet, boolean resposta, String acao) {

        MarkerOptions opcoes = new MarkerOptions();
        if (resposta == true) {
            //opcoes.position(latlong).title(titulo).snippet(snnipet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            /*
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(Maps.this, PrincipalActivity.class);
                    startActivity(intent);
                    //Toast.makeText(Maps.this, "Por Favor! Selecione qualquer ponto do mapa.", Toast.LENGTH_SHORT).show();
                }
            });
            */
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 15));
            //marker = mMap.addMarker(opcoes);
        }else{
            if(acao.equals("Solucionado")) {
                opcoes.position(latlong).title(titulo).snippet(snnipet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        StringTokenizer st = new StringTokenizer(marker.getSnippet());
                        String idLocoPrev = st.nextToken("!");
                        String idLocoNext = st.nextToken("!");

                        Intent intent = new Intent(Maps.this, ListaMensagens.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", idLocoNext);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                marker = mMap.addMarker(opcoes);
            }else if(acao.equals("Denúncia Não Procede")) {

            }else{
                opcoes.position(latlong).title(titulo).snippet(snnipet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        StringTokenizer st = new StringTokenizer(marker.getSnippet());
                        String idLocoPrev = st.nextToken("!");
                        String idNext = st.nextToken("!");

                        Intent intent = new Intent(Maps.this, ListaMensagens.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", idNext);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                marker = mMap.addMarker(opcoes);
            }
        }
    }

    public void onBackPressed()
    {
        Intent it;
        it = new Intent(this, PrincipalActivity.class);
        startActivity(it);
        finish();
        super.onBackPressed();
    }

    public void botaoAjuda(View v) {
        Spanned result;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml("Nesta aba, estão todas as denúncias realizadas em sua cidade, apresentas em forma de pontos no mapa.\n" +
                    "\t<p>No mapa, as denúncias são representadas de duas formas." +
                    "\t<p>Quando o ponto é verde, significa que a denúncia foi solucionada pelo órgão publico responsável." +
                    "\t<p>Para maiores informações sobre a denúncia clique no ponto.",Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml("Nesta aba, estão todas as denúncias realizadas em sua cidade, apresentas em forma de pontos no mapa.\n" +
                    "\t<p>No mapa, as denúncias são representadas de duas formas." +
                    "\t<p>Quando o ponto é verde, significa que a denúncia foi solucionada pelo órgão publico responsável." +
                    "\t<p>Para maiores informações sobre a denúncia clique no ponto.");
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Mapa dos Focos");
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
