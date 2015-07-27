package br.com.alura.loja.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;

@Path("carrinhos")
public class CarrinhoResource {
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public String busca(@PathParam("id") long id) {
		// Poderiamos usar o (@QueryParam("id")
		
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho.toXML();
	}
	
	
	@POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response adiciona(String conteudo) {
        Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
        new CarrinhoDAO().adiciona(carrinho);

        URI location = URI.create("/carrinhos/"+carrinho.getId());
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
	
}
