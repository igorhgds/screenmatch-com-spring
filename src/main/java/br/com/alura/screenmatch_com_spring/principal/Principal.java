package br.com.alura.screenmatch_com_spring.principal;

import br.com.alura.screenmatch_com_spring.model.DadosEpisodio;
import br.com.alura.screenmatch_com_spring.model.DadosSerie;
import br.com.alura.screenmatch_com_spring.model.DadosTemporada;
import br.com.alura.screenmatch_com_spring.model.Episodio;
import br.com.alura.screenmatch_com_spring.service.ConsumoAPI;
import br.com.alura.screenmatch_com_spring.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

            //Exibindo os dados da série
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados + "\n");


            // Exibindo os dados das Temporadas
        List<DadosTemporada> temporadas = new ArrayList<>();
		for(int i = 1; i <= dados.totalTemporadas(); i++){
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);
        System.out.println();


//        for(int i = 0; i < dados.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for(int j = 0; j < episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }


            // Listando os dados dos Episódios sem tratamento
//        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());
//                //.toList(); cria uma coleção imutalvel, não podendo acrescentar nada
//        dadosEpisodios.forEach(System.out::println);


            // Declarando a lista de Episodios informando as temporadas
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());
        episodios.forEach(System.out::println);
        System.out.println();


            // Realizando uma busca para encontrar a temporada do episódio com base em um trecho do titulo
        System.out.println("Digite um trecho do titulo do episódio");
        var trechoTitulo = leitura.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("Episódio encontrado!");
            System.out.println("Episódio: " + episodioBuscado.get().getTitulo() + " - Temporada: " + episodioBuscado.get().getTemporada());
        } else{
            System.out.println("Episódio nao encontrado!");
        }


//        System.out.println("\n ** TOP 5 EPSÓDIOS **");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .limit(5)
//                .forEach(System.out::println);


            //Declarando os top 10 episodios com base na avaliação
        System.out.println("\n ** TOP 10 EPSÓDIOS **");
        episodios.stream()
                .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())
                .limit(10)
                .forEach(System.out::println);


            //Realizando uma busca dos episódios a partir de determinada data
//        System.out.println("A partir de que ano você deseja ver os episódios? ");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//        LocalDate dataBusca = LocalDate.of(ano, 1 , 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        " Temporada: " + e.getTemporada() +
//                                ", Episodio: " + e.getTitulo() +
//                                ", Data Lançamento: " + e.getDataLancamento().format(formatador)
//                ));

            // Criando um mapa com dados por temporada
        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacaoPorTemporada);
    }
}
