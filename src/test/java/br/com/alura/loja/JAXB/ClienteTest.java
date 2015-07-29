package br.com.alura.loja.JAXB;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.alura.loja.modelo.JAXB.Carrinho;
import br.com.alura.loja.modelo.JAXB.Produto;
import br.com.alura.loja.modelo.JAXB.Projeto;
import br.com.alura.loja.resource.JAXB.Servidor;


public class ClienteTest {
	
	private HttpServer server;
	private Client client;

	@Before
	public void iniciaServidor(){
		server = Servidor.startServidor();
		ClientConfig config = new ClientConfig(new LoggingFilter());
		client = ClientBuilder.newClient(config);
	}
	
	@After
	public void finalizaServidor(){
		server.stop();
	}
	
	@Test
	public void testeQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
		
		WebTarget target = client.target("http://localhost:8080");
		
		Carrinho carrinho = target.path("jaxb/carrinhos/1").request().get(Carrinho.class);
		
		assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}
	
	@Test
	public void testeQueTrazProjetoDeId1() {
		WebTarget target = client.target("http://localhost:8080");
		Projeto projeto = target.path("jaxb/projetos/1").request().get(Projeto.class);
		
		assertEquals("Minha loja", projeto.getNome());
		assertEquals(projeto.getAnoDeInicio(),2014);
	}
	
	@Test
	public void testeQueAdicionaUmCarrinhoViaPost() {
        WebTarget target = client.target("http://localhost:8080");
        
        Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");
        
        Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);
        
        //Teste se a requisisão obteve sucesso:201
        Response response = target.path("jaxb/carrinhos").request().post(entity);
        String location = response.getHeaderString("Location");
        assertEquals(201, response.getStatus());
        
        System.out.println(location);
        
        //Testa se o resource gerado é o mesmo que o enviado 
        Carrinho carrinhoCarregado = client.target(location).request().get(Carrinho.class);
        assertEquals(carrinhoCarregado.getRua(), carrinho.getRua());
        assertEquals(carrinhoCarregado.getProdutos().size(), carrinho.getProdutos().size());
	}
}
