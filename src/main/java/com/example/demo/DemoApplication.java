package com.example.demo;

import com.example.demo.principal.Principal;
import com.example.demo.repository.AutorRepository;
import com.example.demo.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private LivroRepository repositorioLivro;

    @Autowired
    private AutorRepository repositorioAutor;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

  @Override
  public void run(String... args) throws Exception {

    Principal principal = new Principal(repositorioLivro, repositorioAutor);

    principal.exibeMenu();
  }

}
