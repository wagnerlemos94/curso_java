package com.dsousa.minnhasfinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsousa.minnhasfinancas.model.entity.Lancamento;
import com.dsousa.minnhasfinancas.model.enuns.StatusLancamento;
import com.dsousa.minnhasfinancas.model.enuns.TipoLancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	@Query(value = 
			" select sum(l.valor) from Lancamento l join l.usuario u "
		  + " where u.id = :idUsuario and l.tipo = :tipo and l.status = :status group by u")
	BigDecimal obeterSaldoPorTipoLancamentoEUsuarioEStatus(
			@Param("idUsuario") Long idUsuario,
			@Param("tipo") TipoLancamento tipo,
			@Param("status") StatusLancamento status);

}
