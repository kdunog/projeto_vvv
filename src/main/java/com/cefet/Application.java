package com.cefet;

import com.cefet.entity.Passageiro;
import com.cefet.repository.PassageiroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner inserirPassageiroTeste(PassageiroRepository repository) {
		return args -> {
			String cpfTeste = "123.456.789-00";

			if (repository.findByCpf(cpfTeste).isPresent()) {
				System.out.println("Passageiro de teste já existe no banco.");
				return;
			}

			Passageiro passageiro = new Passageiro();
			passageiro.setNome("Passageiro Teste");
			passageiro.setCpf(cpfTeste);
			passageiro.setTelefone("(31) 99999-0000");
			passageiro.setEmail("teste@cefet.br");
			passageiro.setSenha("123456");
			passageiro.setIdade(30);

			repository.save(passageiro);
			System.out.println("Passageiro de teste inserido com id: " + passageiro.getId());
		};
	}
}
