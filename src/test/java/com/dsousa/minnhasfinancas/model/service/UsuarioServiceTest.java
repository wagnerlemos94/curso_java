package com.dsousa.minnhasfinancas.model.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dsousa.minnhasfinancas.exeption.ErrorAutenticacao;
import com.dsousa.minnhasfinancas.exeption.RegraNegocioException;
import com.dsousa.minnhasfinancas.model.entity.Usuario;
import com.dsousa.minnhasfinancas.model.repository.UsuarioRepository;
import com.dsousa.minnhasfinancas.service.UsuarioService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@Autowired
	UsuarioService service;

	@Autowired
	UsuarioRepository repository;

	@Test
	public void deveSalvarUmUsuario() {
		repository.deleteAll();
		
		service.validarEmail("email@email.com");
		
		Usuario usuario = Usuario.builder()
				.nome("nome")
				.email("email@email.com")
				.senha("senha")
				.build();
		usuario = service.salvarUsuario(usuario);
		Assertions.assertThat(usuario).isNotNull();
		Assertions.assertThat(usuario.getId()).isEqualTo(1);
		Assertions.assertThat(usuario.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuario.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuario.getSenha()).isEqualTo("senha");
		
	}
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		repository.deleteAll();
		//cenario
		String email = "email@email.com";
		Usuario usuario = Usuario.builder()
				.email(email)
				.build();
		
		service.salvarUsuario(usuario);
		
		Throwable exception = Assertions.catchThrowable(() -> service.salvarUsuario(usuario));

		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Já existe um usuário cadastrado com este email");

	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		// cenario
		String email = "email@email.com";
		String senha = "senha";

		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		repository.save(usuario);

		Usuario result = service.autenticar(email, senha);

		Assertions.assertThat(result);

	}

	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		repository.deleteAll();

		repository.findByEmail("email@email.com");

		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));

		Assertions.assertThat(exception).isInstanceOf(ErrorAutenticacao.class)
				.hasMessage("Usuário não encontrado para email informado.");

	}

	@Test
	public void deveLancarErrorQuandoSenaNaoBater() {
		repository.deleteAll();
		Usuario usuario = Usuario.builder().email("email@email.com").senha("senha").build();
		repository.save(usuario);

		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "123"));

		Assertions.assertThat(exception).isInstanceOf(ErrorAutenticacao.class).hasMessage("Senha inválida.");
	}

	@Test
	public void deveValidarEmail() {
		repository.deleteAll();

		service.validarEmail("email@email.com");
	}

	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();

		repository.save(usuario);

		Throwable exception = Assertions.catchThrowable(() -> service.validarEmail("email@email.com"));

		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Já existe um usuário cadastrado com este email");
	}
}
