package br.com.alura.screenmatch_com_spring.principal;

import br.com.alura.screenmatch_com_spring.model.DadosEpsodio;
import br.com.alura.screenmatch_com_spring.model.DadosSerie;
import br.com.alura.screenmatch_com_spring.model.DadosTemporada;
import br.com.alura.screenmatch_com_spring.service.ConsumoAPI;
import br.com.alura.screenmatch_com_spring.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=67ba82e3";

    public void exibeMenu(){
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for(int i = 1; i <= dados.totalTemporadas(); i++){
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

//        for(int i = 0; i < dados.totalTemporadas(); i++){
//            List<DadosEpsodio> episodiosTemporada = temporadas.get(i).epsodios();
//            for(int j = 0; j < episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        //                       (parametro) -> expressao
        temporadas.forEach(t -> t.epsodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpsodio> dadosEpsodios = temporadas.stream()
                .flatMap(t -> t.epsodios().stream())
                .collect(Collectors.toList());
                //.toList(); cria uma coleção imutalvel, não podendo acrescentar nada

        System.out.println("\n ** TOP 5 EPSÓDIOS **");
        dadosEpsodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpsodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

    }
}
