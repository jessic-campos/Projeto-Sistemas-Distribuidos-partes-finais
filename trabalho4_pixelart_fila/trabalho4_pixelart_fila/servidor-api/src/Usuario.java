import java.io.Serializable;

public class Usuario implements Serializable {
    private String nome;

    public Usuario() {}
    public Usuario(String nome) { this.nome = nome; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
