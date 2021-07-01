package com.dsousa.minnhasfinancas.model.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dsousa.minnhasfinancas.model.entity.Lancamento;
import com.dsousa.minnhasfinancas.model.enuns.StatusLancamento;
import com.dsousa.minnhasfinancas.model.enuns.TipoLancamento;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		//cenario
		
		Lancamento lancamento = criarLancamento();
		
		lancamento = repository.save(lancamento);
		
		Assertions.assertThat(lancamento.getId()).isNotNull();
	}
	
	@Test
	public void deveDeletarUmLancamneto() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		repository.delete(lancamento);
		
		Lancamento lancamentoInexistente = repository.findById(lancamento.getId()).get();
		Assertions.assertThat(lancamentoInexistente).isNotEqualTo(lancamentoInexistente);
		
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		lancamento.setAno(2018);
		lancamento.setDescricao("Teste Atualizar");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(lancamento);
		
		Lancamento lancamentoAtualiza = repository.findById(lancamento.getId()).get();
		
		Assertions.assertThat(lancamentoAtualiza.getAno()).isEqualTo(2018);
		Assertions.assertThat(lancamentoAtualiza.getDescricao()).isEqualTo("Teste Atualizar");
		Assertions.assertThat(lancamentoAtualiza.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
		
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		Assertions.assertThat(lancamentoEncontrado.isPresent()).isTrue();

		

	}
	
	private Lancamento criarEPersistirUmLancamento() {
		Lancamento lancamento = criarLancamento();
		lancamento = repository.save(lancamento);
		return lancamento;
	}
	
	private Lancamento criarLancamento() {
		return Lancamento.builder()
		.ano(2019)
		.mes(1)
		.descricao("LANCAMNETO QUALQUER")
		.tipo(TipoLancamento.RECEITA)
		.status(StatusLancamento.PENDENTE)
		.dataCadastro(LocalDate.now())
		.build();
	}
	
}
