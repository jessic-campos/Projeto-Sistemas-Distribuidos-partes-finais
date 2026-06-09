import java.io.Serializable;

public abstract class Ferramenta implements Serializable {
    private String nome;

    public Ferramenta() {}
    public Ferramenta(String nome) { this.nome = nome; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public abstract void aplicar(Mural mural, int x, int y, String cor);
}
