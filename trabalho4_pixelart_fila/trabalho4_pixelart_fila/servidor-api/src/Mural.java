import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Mural implements Serializable {
    private int largura;
    private int altura;
    private Usuario dono;
    private final List<Pixel> pixels = new ArrayList<>();

    public Mural() {
        this(10, 10, new Artista("Victor"));
    }

    public Mural(int largura, int altura, Usuario dono) {
        this.largura = largura;
        this.altura = altura;
        this.dono = dono;
    }

    public synchronized void criar(int largura, int altura, String dono) {
        this.largura = largura;
        this.altura = altura;
        this.dono = new Artista(dono == null || dono.isBlank() ? "Victor" : dono);
        this.pixels.clear();
    }

    public synchronized void pintarPixel(int x, int y, String cor) {
        validar(x, y);
        apagarPixel(x, y);
        pixels.add(new Pixel(new Coordenada(x, y), cor));
    }

    public synchronized void apagarPixel(int x, int y) {
        pixels.removeIf(p -> p.getCoordenada().getX() == x && p.getCoordenada().getY() == y);
    }

    public synchronized List<Pixel> listarPixels() {
        return new ArrayList<>(pixels);
    }

    public synchronized String visualizarMuralAnsi() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dono: ").append(dono.getNome()).append("\n");
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                Pixel pixel = buscarPixel(x, y);
                sb.append(pixel == null ? " ." : simboloCor(pixel.getCor()));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public synchronized String visualizarMuralTexto() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dono: ").append(dono.getNome()).append("\n");
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                Pixel pixel = buscarPixel(x, y);
                sb.append(pixel == null ? " ." : " " + pixel.getCor().charAt(0));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private Pixel buscarPixel(int x, int y) {
        for (Pixel p : pixels) {
            if (p.getCoordenada().getX() == x && p.getCoordenada().getY() == y) {
                return p;
            }
        }
        return null;
    }

    private void validar(int x, int y) {
        if (x < 0 || y < 0 || x >= largura || y >= altura) {
            throw new IllegalArgumentException("Coordenada fora do mural");
        }
    }

    private String simboloCor(String cor) {
        switch (cor.toUpperCase()) {
            case "VERMELHO": return "\u001B[41m  \u001B[0m";
            case "AZUL": return "\u001B[44m  \u001B[0m";
            case "VERDE": return "\u001B[42m  \u001B[0m";
            case "AMARELO": return "\u001B[43m  \u001B[0m";
            case "PRETO": return "\u001B[40m  \u001B[0m";
            case "BRANCO": return "\u001B[47m  \u001B[0m";
            case "ROXO": return "\u001B[45m  \u001B[0m";
            case "MARROM": return "\u001B[48;5;94m  \u001B[0m";
            case "LARANJA": return "\u001B[48;5;208m  \u001B[0m";
            default: return " ?";
        }
    }

    public synchronized String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"largura\":").append(largura)
          .append(",\"altura\":").append(altura)
          .append(",\"dono\":\"").append(JsonUtil.escape(dono.getNome())).append("\"")
          .append(",\"pixels\":[");
        for (int i = 0; i < pixels.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append(pixels.get(i).toJson());
        }
        sb.append("]}");
        return sb.toString();
    }
}
