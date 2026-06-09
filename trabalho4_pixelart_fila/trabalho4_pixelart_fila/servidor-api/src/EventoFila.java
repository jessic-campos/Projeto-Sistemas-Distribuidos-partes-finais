import java.time.LocalDateTime;
import java.util.Map;

public class EventoFila {
    private final String tipo;
    private final int x;
    private final int y;
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final int largura;
    private final int altura;
    private final String cor;
    private final String dono;
    private final String criadoEm;

    public EventoFila(String tipo, int x, int y, int x1, int y1, int x2, int y2,
                      int largura, int altura, String cor, String dono, String criadoEm) {
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.largura = largura;
        this.altura = altura;
        this.cor = cor;
        this.dono = dono;
        this.criadoEm = criadoEm == null ? LocalDateTime.now().toString() : criadoEm;
    }

    public static EventoFila criarMural(int largura, int altura, String dono) {
        return new EventoFila("CRIAR_MURAL", 0, 0, 0, 0, 0, 0, largura, altura, "", dono, null);
    }

    public static EventoFila pintarPixel(int x, int y, String cor) {
        return new EventoFila("PINTAR_PIXEL", x, y, 0, 0, 0, 0, 0, 0, cor, "", null);
    }

    public static EventoFila apagarPixel(int x, int y) {
        return new EventoFila("APAGAR_PIXEL", x, y, 0, 0, 0, 0, 0, 0, "", "", null);
    }

    public static EventoFila aplicarPincel(int x1, int y1, int x2, int y2, String cor) {
        return new EventoFila("APLICAR_PINCEL", 0, 0, x1, y1, x2, y2, 0, 0, cor, "", null);
    }

    public static EventoFila aplicarBorracha(int x, int y) {
        return new EventoFila("APLICAR_BORRACHA", x, y, 0, 0, 0, 0, 0, 0, "", "", null);
    }

    public static EventoFila fromJson(String json) {
        Map<String, String> m = JsonUtil.parseJsonObject(json);
        return new EventoFila(
            m.getOrDefault("tipo", ""),
            parseInt(m.get("x")),
            parseInt(m.get("y")),
            parseInt(m.get("x1")),
            parseInt(m.get("y1")),
            parseInt(m.get("x2")),
            parseInt(m.get("y2")),
            parseInt(m.get("largura")),
            parseInt(m.get("altura")),
            m.getOrDefault("cor", ""),
            m.getOrDefault("dono", ""),
            m.getOrDefault("criadoEm", null)
        );
    }

    private static int parseInt(String valor) {
        try {
            return valor == null || valor.isBlank() ? 0 : Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String toJson() {
        return "{"
            + "\"tipo\":\"" + JsonUtil.escape(tipo) + "\","
            + "\"x\":" + x + ","
            + "\"y\":" + y + ","
            + "\"x1\":" + x1 + ","
            + "\"y1\":" + y1 + ","
            + "\"x2\":" + x2 + ","
            + "\"y2\":" + y2 + ","
            + "\"largura\":" + largura + ","
            + "\"altura\":" + altura + ","
            + "\"cor\":\"" + JsonUtil.escape(cor) + "\","
            + "\"dono\":\"" + JsonUtil.escape(dono) + "\","
            + "\"criadoEm\":\"" + JsonUtil.escape(criadoEm) + "\""
            + "}";
    }

    public String getTipo() { return tipo; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getX1() { return x1; }
    public int getY1() { return y1; }
    public int getX2() { return x2; }
    public int getY2() { return y2; }
    public int getLargura() { return largura; }
    public int getAltura() { return altura; }
    public String getCor() { return cor; }
    public String getDono() { return dono; }
    public String getCriadoEm() { return criadoEm; }
}
