package com.dsousa.minnhasfinancas.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dsousa.minnhasfinancas.api.dto.LancamentoDTO;
import com.dsousa.minnhasfinancas.exeption.RegraNegocioException;
import com.dsousa.minnhasfinancas.model.entity.Lancamento;
import com.dsousa.minnhasfinancas.model.entity.Usuario;
import com.dsousa.minnhasfinancas.model.enuns.StatusLancamento;
import com.dsousa.minnhasfinancas.model.enuns.TipoLancamento;
import com.dsousa.minnhasfinancas.service.LancamentoService;
import com.dsousa.minnhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResource {

	private final LancamentoService service;
	private final UsuarioService usuarioService;


	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam(value = "usuario") Long idUsuario) {
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);

		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if (usuario.isPresent()) {
			return ResponseEntity.badRequest()
					.body("Não foi possísel realizar a consulta. Usuário não encontrado para o Id informado");
		}else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento entidade = converter(dto);
			entidade = service.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
		return service.obterPorId(id).map(entity -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				lancamento = service.atualizar(lancamento);
				System.out.println(lancamento.toString());
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}

	@DeleteMapping("{id}")
	public ResponseEntity delete(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}

	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = usuarioService.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o Id informado."));

		lancamento.setUsuario(usuario);
		if(dto.getTipo() != null) {			
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}
		if(dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		return lancamento;
	}

}
