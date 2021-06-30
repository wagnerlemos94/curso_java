package com.dsousa.minnhasfinancas.exeption;

public class ErrorAutenticacao extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ErrorAutenticacao(String mensagem) {
		super(mensagem);
	}
	
	

}
