package br.desenvolvedor.michelatz.cidadesemmosquito.ConexaoWEB;

/**
 * Created by Michel Atz on 18/10/2016.
 */

public class Config {

    //endereços web
    /*
    public static final String URL_ADD ="http://www.cidadesemmosquito.com/conexao/addEmp.php";

    public static final String URL_GET_ALL = "http://www.cidadesemmosquito.com/conexao/listaDenuncias.php";
    public static final String URL_GET_EMP = "http://www.cidadesemmosquito.com/conexao/pegarDenuncia.php?id=";
    public static final String URL_UPDATE_EMP = "http://www.cidadesemmosquito.com/conexao/editarDenuncia.php";
    public static final String URL_DELETE_EMP = "http://www.cidadesemmosquito.com/conexao/excluirDenuncia.php?id=";
    public static final String URL_GET_PONTOS_DENUNCIA = "http://www.cidadesemmosquito.com/conexao/listaPontos.php";
    public static final String URL_GET_MENSAGENS= "http://www.cidadesemmosquito.com/conexao/buscaMensagens.php?iddenuncia=";
    public static final String URL_INSERIR_MENSAGEM = "http://www.cidadesemmosquito.com/conexao/inserirMensagem.php";
    public static final String URL_DELETAR_MENSAGEM = "http://www.cidadesemmosquito.com/conexao/excluirMensagem.php?id=";
    */

    public static final String URL_ADD ="http://projetosw.esy.es/conexaoMosquito/addEmp.php";

    public static final String URL_GET_ALL = "http://projetosw.esy.es/conexaoMosquito/listaDenuncias.php";
    public static final String URL_GET_EMP = "http://projetosw.esy.es/conexaoMosquito/pegarDenuncia.php?id=";
    public static final String URL_UPDATE_EMP = "http://projetosw.esy.es/conexaoMosquito/editarDenuncia.php";
    public static final String URL_DELETE_EMP = "http://projetosw.esy.es/conexaoMosquito/excluirDenuncia.php?id=";
    public static final String URL_GET_PONTOS_DENUNCIA = "http://projetosw.esy.es/conexaoMosquito/listaPontos.php";
    public static final String URL_GET_MENSAGENS= "http://projetosw.esy.es/conexaoMosquito/buscaMensagens.php?iddenuncia=";
    public static final String URL_INSERIR_MENSAGEM = "http://projetosw.esy.es/conexaoMosquito/inserirMensagem.php";
    public static final String URL_DELETAR_MENSAGEM = "http://projetosw.esy.es/conexaoMosquito/excluirMensagem.php?id=";

    //Chaves que seram usadas nos scripts PHPs
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_NUMERO= "numero";
    public static final String KEY_EMP_RUA= "rua";
    public static final String KEY_EMP_BAIRRO= "bairro";
    public static final String KEY_EMP_CIDADE = "cidade";
    public static final String KEY_EMP_ESTADO = "estado";
    public static final String KEY_EMP_IMAGEM = "imagem";
    public static final String KEY_EMP_DESCRICAO= "descricao";
    public static final String KEY_EMP_EMAIL= "email";
    public static final String KEY_EMP_NOME= "nome";
    public static final String KEY_EMP_COMENTARIO= "comentario";
    public static final String KEY_EMP_ID_DENUNCIA= "iddenuncia";
    public static final String KEY_EMP_DATA= "data";
    public static final String KEY_EMP_ACAO= "acao";
    public static final String KEY_EMP_LONGITUDE= "longitude";
    public static final String KEY_EMP_LATITUDE= "latitude";
    public static final String KEY_EMP_PONTO= "ponto";

    //Tags JSON
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_RUA = "rua";
    public static final String TAG_NUMERO = "numero";
    public static final String TAG_BAIRRO = "bairro";
    public static final String TAG_CIDADE = "cidade";
    public static final String TAG_MENSAGEM = "mensagem";
    public static final String TAG_IMAGEM = "imagem";
    public static final String TAG_DESCRICAO = "descricao";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_NOME = "nome";
    public static final String TAG_ID_DENUNCIA = "iddenuncia";
    public static final String TAG_DATA = "data";
    public static final String TAG_ACAO = "acao";
    public static final String TAG_LONGITUDE = "longitude";
    public static final String TAG_LATITUDE = "latitude";
    public static final String TAG_PONTO = "ponto";

    //Id da denuncia que sera passada por intent
    public static final String EMP_ID = "emp_id";
}
