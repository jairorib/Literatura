package com.example.demo.principal;

import com.example.demo.model.*;
import com.example.demo.repository.AutorRepository;
import com.example.demo.repository.LivroRepository;
import com.example.demo.service.ConsumoApi;
import com.example.demo.service.ConverteDados;
import com.example.demo.model.Livro;

import java.util.*;

public class Principal {

  private ConsumoApi consumo = new ConsumoApi();

  private ConverteDados conversor = new ConverteDados();

  private Scanner leitura = new Scanner(System.in);

  private final String ENDERECO = "https://gutendex.com/books?search=";

  private List<DadosLivro> dadosLivros = new ArrayList<>();

  private AutorRepository repositorioAutor;
  private LivroRepository repositorioLivro;

  public Principal(LivroRepository repositorioLivro, AutorRepository repositorioAutor) {
    this.repositorioLivro = repositorioLivro;
    this.repositorioAutor = repositorioAutor;
  }

  public void exibeMenu() {
    var opcao = -1;
    while(opcao != 0) {
      var menu = """
              \n\n
              ------------------------
              Escolha o número de sua opção:
              1- buscar livro pelo título
              2- listar livros registrados
              3- listar autores registrados
              4- listar autores vivos em um determinado ano
              5- listar livros em um determinado idioma
              0- sair
              """;

      System.out.println(menu);

      opcao = leitura.nextInt();
      leitura.nextLine();

      switch (opcao) {
        case 1:
          buscarLivro();
          break;
        case 2:
          listarLivrosBuscados();
          break;
        case 3:
          listarAutoresBuscados();
          break;
        case 4:
          listarAutoresVivosNoAno();
          break;
        case 5:
          listarLivrosPorIdioma();
          break;
        case 0:
          System.out.println("Saindo...");
        default:
          System.out.println("Opção inválida");
      }
    }
  }

  private void buscarLivro() {
    var dados = getDadosLivro();

    if (dados.isEmpty()) {
      System.out.println("Livro não encontrado!");
    } else {

      for (DadosLivro book: dados) {

        Autor autor = new Autor(new DadosAutor(book.autor().get(0).nome(), book.autor().get(0).nascimento(), book.autor().get(0).morte()));

        if (!repositorioAutor.existsByNome(autor.getNome())) {
          autor = repositorioAutor.save(autor);
        } else {
          autor = repositorioAutor.findByNome(autor.getNome());
        }

        Livro livro = new Livro(book);
        livro.setAutor(autor);

        if (!repositorioLivro.existsByTitulo(livro.getTitulo())) {
          repositorioLivro.save(livro);
        }

        System.out.println(String.format("""
                -----Livro-----
                Título: %s
                Autor: %s
                Idioma: %s
                Número de downloads: %d
                ---------------
                """, book.titulo(), book.autor().get(0).nome(), book.idioma().get(0), book.totalDownloads()));
      }
    }
  }

  private List<DadosLivro> getDadosLivro() {
    System.out.println("Insira o nome do livro que você quer procurar");

    var nomeLivro = leitura.nextLine();

    var json = consumo.obterDados(ENDERECO + nomeLivro.replace(" ", "+"));

    DadosLivro[] dadosLivro = conversor.obterDados(json, DadosLivro[].class);

    List<DadosLivro> livros = new ArrayList<>();

    for (DadosLivro book : dadosLivro) {

      boolean contains = book.titulo().toUpperCase().contains(nomeLivro.toUpperCase());
      if (contains == true) {
        livros.add(book);
      }
    }

    return livros;

  }

  private String livrosListados(Livro livro) {
    return String.format("""
                    ----- Livro -----
                    Título: %s
                    Autor: %s
                    Idioma: %s
                    Número de downloads: %d
                    ---------------
                    """, livro.getTitulo(), livro.getAutor().getNome(), livro.getIdioma(), livro.getTotalDownloads());
  }

  private void listarLivrosBuscados() {
    List<Livro> livros = repositorioLivro.findAll();

    livros.stream()
            .sorted(Comparator.comparing(Livro::getTitulo))
            .forEach(a -> System.out.println(livrosListados(a)));

  }

  private void listarAutoresBuscados() {
    List<Autor> autores = repositorioAutor.findAll();

    autores.stream()
            .sorted(Comparator.comparing(Autor::getNome))
            .forEach(System.out::println);
  }

  private void listarAutoresVivosNoAno() {
    System.out.println("Insira o ano que deseja pesquisar");
    var ano = leitura.nextInt();
    leitura.nextLine();
    var autores = repositorioAutor.buscarPorAnoDeFalescimento(ano);

    if (!autores.isEmpty()) {
      autores.stream()
              .forEach(System.out::println);
    }
  }

  private void listarLivrosPorIdioma() {
    System.out.println(
            """
                    Insira o idioma para realizar a busca:
                    es- espanhol
                    en- inglês
                    fr- fracês
                    pt- português
            """);
    var idioma = leitura.nextLine();
    var livros = repositorioLivro.findByIdioma(Idioma.fromString(idioma));

    if (!livros.isEmpty()) {

      livros.stream()
              .sorted(Comparator.comparing(Livro::getTitulo))
              .forEach(a -> System.out.println(livrosListados(a)));

    } else {
      System.out.println("Livro não encontrado!");
    }
  }
}
