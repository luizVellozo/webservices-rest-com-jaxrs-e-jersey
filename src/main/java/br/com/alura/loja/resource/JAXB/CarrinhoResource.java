package br.com.alura.loja.resource.JAXB;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.alura.loja.dao.JAXB.CarrinhoDAO;
import br.com.alura.loja.modelo.JAXB.Carrinho;
import br.com.alura.loja.modelo.JAXB.Produto;

@Path("jaxb/carrinhos")
public class CarrinhoResource {
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Carrinho busca(@PathParam("id") long id) {
		// Poderiamos usar o (@QueryParam("id")
		
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho;
	}
	
	
	@POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response adiciona(Carrinho carrinho) {
        new CarrinhoDAO().adiciona(carrinho);

        URI location = URI.create("jaxb/carrinhos/"+carrinho.getId());
        return Response.created(location).build();
    }
	
	/*
	 * Se quizer o DELETE realmente idempotente (seguindo a especificação), 
	 * ao invés de receber o ID do produto eu deveria receber um ID único que representasse 
	 * o ID do produto dentro deste carrinho de maneira única. Como: /carrinhos/ID_DO_CARRINHO/produtos/ID_DO_PRODUTO_E_CONTADOR_UNICO
	*/
	@Path("{id}/produtos/{produtoId}")
    @DELETE
    public Response remove(@PathParam("id") long id, @PathParam("produtoId") long produtoId) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
        carrinho.remove(produtoId);
        return Response.ok().build();
    }
	
	
	// Deveria haver um @PATCH para aceitar a alteração de uma parte do recurso
	@Path("{id}/produtos/{produtoId}")
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public Response alteraQuantidade(@PathParam("id") long id, @PathParam("produtoId") long produtoId, Produto produto) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
        carrinho.trocaQuantidade(produto);
        return Response.ok().build();
    }
	
}
