package br.backend.modelo;

import br.backend.modelo.enums.Acao;
import br.backend.modelo.enums.Entidade;

public class Requisicao<T> {

    private Acao acao;         // Antes era String
    private Entidade entidade; // Antes era String
    private T dados;

    public Requisicao() {
    }

    public Requisicao(Acao acao, Entidade entidade, T dados) {
        this.acao = acao;
        this.entidade = entidade;
        this.dados = dados;
    }

    public Acao getAcao() {
        return acao;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }

    public T getDados() {
        return dados;
    }

    public void setDados(T dados) {
        this.dados = dados;
    }
}
