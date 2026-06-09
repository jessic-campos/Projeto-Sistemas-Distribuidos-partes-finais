import java.io.Serializable;

public class Pixel implements Serializable {
    private Coordenada coordenada;
    private String cor;

    public Pixel() {}

    public Pixel(Coordenada coordenada, String cor) {
        this.coordenada = coordenada;
        this.cor = cor.toUpperCase();
    }

    public Coordenada getCoordenada() { return coordenada; }
    public String getCor() { return cor; }
    public void setCoordenada(Coordenada coordenada) { this.coordenada = coordenada; }
    public void setCor(String cor) { this.cor = cor.toUpperCase(); }

    public String toJson() {
        return "{\"x\":" + coordenada.getX() + ",\"y\":" + coordenada.getY() + ",\"cor\":\"" + cor + "\"}";
    }

    @Override
    public String toString() {
        return "Pixel{x=" + coordenada.getX() + ", y=" + coordenada.getY() + ", cor='" + cor + "'}";
    }
}
