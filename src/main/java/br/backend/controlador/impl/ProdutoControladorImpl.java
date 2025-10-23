package br.backend.controlador.impl;

import br.backend.controlador.Controlador;
import br.backend.modelo.Categoria;
import br.backend.modelo.Produto;
import br.backend.modelo.Requisicao;
import br.backend.modelo.Resposta;
import br.backend.servico.ProdutoServico;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProdutoControladorImpl implements Controlador {

    private final ProdutoServico produtoServico;
    private final ObjectMapper objectMapper;

    public ProdutoControladorImpl(ProdutoServico produtoServico) {
        this.produtoServico = produtoServico;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String processarRequisicao(Requisicao<?> requisicao) {
        try {
            switch (requisicao.getAcao()) {
                case CRIAR: {
                    Produto obj = objectMapper.convertValue(requisicao.getDados(), Produto.class);
                    Categoria categoria = obj.getCategoria();
                    Produto objCriado = produtoServico.inserirProduto(
                            obj.getNome(), obj.getPreco(), obj.getUnidade(), categoria,
                            obj.getQuantidade(), obj.getQuantidadeMinima(), obj.getQuantidadeMaxima()
                    );
                    return objectMapper.writeValueAsString(new Resposta<>("sucesso", "Produto criado com sucesso", objCriado));
                }

                case ENCONTRAR: {
                    Integer id = objectMapper.convertValue(requisicao.getDados(), Produto.class).getId();
                    Produto encontrado = produtoServico.buscarPorId(id);
                    if (encontrado != null) {
                        return objectMapper.writeValueAsString(new Resposta<>("sucesso", "Produto encontrado", encontrado));
                    } else {
                        return objectMapper.writeValueAsString(new Resposta<>("erro", "Produto não encontrado", null));
                    }
                }

                case ATUALIZAR: {
                    Produto objAtualizacao = objectMapper.convertValue(requisicao.getDados(), Produto.class);
                    Produto objAtualizado = produtoServico.atualizarProduto(objAtualizacao.getId(), objAtualizacao);
                    return objectMapper.writeValueAsString(new Resposta<>("sucesso", "Produto atualizado com sucesso", objAtualizado));
                }

                case DELETAR: {
                    Integer id = objectMapper.convertValue(requisicao.getDados(), Produto.class).getId();
                    boolean excluido = produtoServico.deletarProduto(id);
                    if (excluido) {
                        return objectMapper.writeValueAsString(new Resposta<>("sucesso", "Produto deletado", null));
                    } else {
                        return objectMapper.writeValueAsString(new Resposta<>("erro", "Produto não encontrado", null));
                    }
                }

                case LISTAR: {
                    return objectMapper.writeValueAsString(new Resposta<>("sucesso", "Lista de produtos", produtoServico.listarProdutos()));
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
