package br.backend.controlador.impl;

import br.backend.controlador.Controlador;
import br.backend.modelo.Categoria;
import br.backend.modelo.Requisicao;
import br.backend.modelo.Resposta;
import br.backend.servico.CategoriaServico;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CategoriaControladorImpl implements Controlador {

    private final CategoriaServico categoriaServico;
    private final ObjectMapper objectMapper;

    public CategoriaControladorImpl(CategoriaServico categoriaServico) {
        this.categoriaServico = categoriaServico;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String processarRequisicao(Requisicao<?> requisicao) {
        try {
            switch (requisicao.getAcao()) {
                case CRIAR: {
                    Categoria cat = (Categoria) requisicao.getDados();
                    Categoria catCriada = categoriaServico.inserirCategoria(cat.getNome(), cat.getTamanho(), cat.getEmbalagem());
                    return objectMapper.writeValueAsString(new Resposta<>("sucesso", "Categoria criada", catCriada));
                }

                case ENCONTRAR: {
                    Integer id = objectMapper.convertValue(requisicao.getDados(), Categoria.class).getId();
                    Categoria encontrada = categoriaServico.buscarPorId(id);
                }

                case ATUALIZAR: {
                    Categoria catAtualizacao = objectMapper.convertValue(requisicao.getDados(), Categoria.class);
                    Categoria catAtualizada = categoriaServico.atualizarCategoria(catAtualizacao.getId(), catAtualizacao);
                }

                case DELETAR: {
                    Integer id = objectMapper.convertValue(requisicao.getDados(), Categoria.class).getId();
                    boolean excluido = categoriaServico.deletarCategoria(id);

                }

                case LISTAR: {
                    return objectMapper.writeValueAsString(new Resposta<>("sucesso", "Lista de categorias", categoriaServico.listarCategorias()));
                }

                default:
                    return objectMapper.writeValueAsString(new Resposta<>("erro", "Ação desconhecida", null));
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                return objectMapper.writeValueAsString(new Resposta<>("erro", "Erro ao processar requisição: " + e.getMessage(), null));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
