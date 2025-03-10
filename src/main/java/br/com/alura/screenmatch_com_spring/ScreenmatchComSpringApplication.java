package br.com.alura.screenmatch_com_spring;

import br.com.alura.screenmatch_com_spring.model.DadosSerie;
import br.com.alura.screenmatch_com_spring.service.ConsumoAPI;
import br.com.alura.screenmatch_com_spring.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchComSpringApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchComSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoAPI = new ConsumoAPI();
		var json = ConsumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=67ba82e3");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
