package br.desenvolvedor.michelatz.cidadesemmosquito.modelo;

import java.io.Serializable;

/**
 * Created by Michel Atz on 22/10/2016.
 */

public class Usuario{

    private Long id;
    private String nome;
    private String email;
    private int ativo;

    public Usuario() {

    }

    public Usuario(Long id, String nome, String email, int ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getAtivo() {
        return ativo;
    }

    public void setAtivo(int ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
