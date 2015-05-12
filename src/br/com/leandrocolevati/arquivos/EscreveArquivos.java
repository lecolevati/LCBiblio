package br.com.leandrocolevati.arquivos;

public enum EscreveArquivos {

	APPEND(true), OVERWRITE(false);
	private final boolean valor;

	EscreveArquivos(boolean valorOpcao) {
		valor = valorOpcao;
	}

	public boolean getValor() {
		return valor;
	}

}
