package com.dsousa.minnhasfinancas.service;

import java.util.List;

import com.dsousa.minnhasfinancas.model.entity.Lancamento;
import com.dsousa.minnhasfinancas.model.enuns.StatusLancamento;

public interface LancamentoService {

	Lancamento salvar(Lancamento lancamento);

	Lancamento atualizar(Lancamento lancamento);

	void deletar(Lancamento lancamento);

	List<Lancamento> buscar(Lancamento lancamentoFiltro);

	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);

}
