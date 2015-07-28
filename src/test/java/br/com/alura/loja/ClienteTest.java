package br.com.alura.loja;

import static org.junit.Assert.*;

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

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.modelo.Projeto;
import br.com.alura.loja.resource.Servidor;

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
		
		String conteudo = target.path("/carrinhos/1").request().get(String.class);
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		
		assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}
	
	@Test
	public void testeQueTrazProjetoDeId1() {
		WebTarget target = client.target("http://localhost:8080");
		String conteudo = target.path("/projetos/1").request().get(String.class);
		
		Projeto projeto = (Projeto) new XStream().fromXML(conteudo);
		
		assertEquals("Minha loja", projeto.getNome());
		assertEquals(projeto.getAnoDeInicio(),2014);
	}
	
	@Test
	public void testeQueAdicionaUmCarrinhoViaPost() {
        WebTarget target = client.target("http://localhost:8080");
        
        //precisamos criar um carrinho e transforma-lo em XML para realizar o post:
        Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");
        String xml = carrinho.toXML();
        
//        //Agora que temos o XML e sabemos que o media type 
//        que enviaremos é application/xml, 
//        precisamos representar isso de alguma maneira. 
//        Utilizaremos a classe Entity do próprio JAX-RS, para criar tal representação
        
        Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
        
        //Teste se a requisisão obteve sucesso:201
        Response response = target.path("/carrinhos").request().post(entity);
        String location = response.getHeaderString("Location");
        assertEquals(201, response.getStatus());
        
        //Testa se o resource gerado é o mesmo que o enviado 
        String conteudo = client.target(location).request().get(String.class);
        assertTrue(conteudo.contains("Tablet") && conteudo.contains("Rua Vergueiro"));
	}
}
