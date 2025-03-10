package br.com.alura.screenmatch_com_spring.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
