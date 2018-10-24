package br.desenvolvedor.michelatz.cidadesemmosquito.modelo;

import java.io.Serializable;

/**
 * Created by Michel Atz on 12/11/2016.
 */

public class Mensagem{

    private String id;
    private String texto;
    private String nome;
    private String email;
    private String iddenuncia;

    public Mensagem() {

    }

    public Mensagem(String id, String texto, String nome, String email, String iddenuncia) {
        this.id = id;
        this.texto = texto;
        this.nome = nome;
        this.email = email;
        this.iddenuncia = iddenuncia;
    }

    public String getIddenuncia() {
        return iddenuncia;
    }

    public void setIddenuncia(String iddenuncia) {
        this.iddenuncia = iddenuncia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
