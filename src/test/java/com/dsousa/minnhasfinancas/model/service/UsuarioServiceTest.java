package com.dsousa.minnhasfinancas.model.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
	public void deveValidarEmail() {
		repository.deleteAll();
		
		service.validarEmail("email@email.com");
	}
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
		
		repository.save(usuario);
		
		service.validarEmail("email@email.com");
	}
}
