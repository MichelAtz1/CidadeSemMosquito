package br.desenvolvedor.michelatz.cidadesemmosquito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.desenvolvedor.michelatz.cidadesemmosquito.modelo.Usuario;
import br.desenvolvedor.michelatz.cidadesemmosquito.modelo.UsuarioDAO;

public class Login extends AppCompatActivity {

    private Toolbar mToobar;
    private final String nomeDB = "cidadeSemMosquito";
    EditText edtNome;
    EditText edtEmail;

    String nome;
    String email;

    public static final String MyPREFERENCES = "MinhasPreferencias" ;
    public static final String Nome = "nomeKey";
    public static final String Email = "emailKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToobar = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle(" Cidade Sem Mosquito");
        mToobar.setLogo(R.drawable.icon_logo);
        mToobar.setSubtitle(" Login");
        setSupportActionBar(mToobar);

        edtNome= (EditText)findViewById(R.id.edtNomeUsuario);
        edtEmail = (EditText)findViewById(R.id.edtEmailUsuario);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void entrar(View v){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+";
        email = edtEmail.getText().toString().trim();

        if(edtNome.getText().length()==0){
            Toast.makeText(Login.this,"O campo Nome deve ser preenchido",Toast.LENGTH_LONG).show();
        }else if(edtEmail.getText().length()==0){
            Toast.makeText(Login.this,"O campo E-mail deve ser preenchido",Toast.LENGTH_LONG).show();
        }else {

            if (email.matches(emailPattern)){

                UsuarioDAO dao = new UsuarioDAO(this, nomeDB);
                dao.inserir(getUsuario());
                dao.close();

                Intent it = new Intent(this, PrincipalActivity.class);
                startActivity(it);
                finish();

            }
            else{
                Toast.makeText(getApplicationContext(), "Por Favor, insira um e-mail válido!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private Usuario getUsuario(){
        Usuario u = new Usuario();
        SharedPreferences.Editor editor = sharedpreferences.edit();

        nome = edtNome.getText().toString();
        u.setNome(nome);
        editor.putString(Nome, nome);

        email = edtEmail.getText().toString();
        u.setEmail(email);
        editor.putString(Email, email);

        editor.commit();

        return u;
    }
}
