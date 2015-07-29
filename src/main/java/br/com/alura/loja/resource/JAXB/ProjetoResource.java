package br.com.alura.loja.resource.JAXB;

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

import br.com.alura.loja.dao.JAXB.ProjetoDAO;
import br.com.alura.loja.modelo.JAXB.Projeto;


@Path("jaxb/projetos")
public class ProjetoResource {
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Projeto busca(@PathParam("id") long id) {
		Projeto projeto = new ProjetoDAO().busca(id);
		return projeto;
	}
	
	
	//Podemos testar Post com o CURL passando o parrametro -d
	@POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response adiciona(Projeto projeto) {
        new ProjetoDAO().adiciona(projeto);
        
        URI location = URI.create("/projetos/"+projeto.getId());
        return Response.created(location).build();
    }
	
	@Path("{id}")
    @DELETE
    public Response remove(@PathParam("id") long id) {
		new ProjetoDAO().remove(id);
        return Response.ok().build();
    }
	
}
