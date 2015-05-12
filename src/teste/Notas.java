package teste;

public class Notas {

	private double nota;
	private String ra_aluno;
	private int id_avaliacao;
	private int id_materia;
	
	public double getNota() {
		return nota;
	}
	public String getRa_aluno() {
		return ra_aluno;
	}
	public int getId_avaliacao() {
		return id_avaliacao;
	}
	public int getId_materia() {
		return id_materia;
	}
	public void setNota(double nota) {
		this.nota = nota;
	}
	public void setRa_aluno(String ra_aluno) {
		this.ra_aluno = ra_aluno;
	}
	public void setId_avaliacao(int id_avaliacao) {
		this.id_avaliacao = id_avaliacao;
	}
	public void setId_materia(int id_materia) {
		this.id_materia = id_materia;
	}
	@Override
	public String toString() {
		return this.ra_aluno;
	}
	
}
