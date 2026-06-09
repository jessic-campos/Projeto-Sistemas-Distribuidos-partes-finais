import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.Map;

public class FerramentaResource {
    private final PixelArtService service;

    public FerramentaResource(PixelArtService service) {
        this.service = service;
    }

    public void handlePincel(HttpExchange exchange) throws IOException {
        try {
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                HttpResponder.enviarJson(exchange, 405, JsonUtil.erro("Metodo nao permitido"));
                return;
            }
            Map<String, String> body = JsonUtil.parseJsonObject(JsonUtil.readBody(exchange.getRequestBody()));

            int x1 = Integer.parseInt(body.getOrDefault("x1", "0"));
            int y1 = Integer.parseInt(body.getOrDefault("y1", "0"));
            int x2 = Integer.parseInt(body.getOrDefault("x2", "0"));
            int y2 = Integer.parseInt(body.getOrDefault("y2", "0"));
            String cor = body.getOrDefault("cor", "PRETO");

            String resposta = service.aplicarPincel(x1, y1, x2, y2, cor);

            System.out.println("\n[CLIENTE] Aplicou pincel de (" + x1 + ", " + y1 + ") ate (" + x2 + ", " + y2 + ") com a cor " + cor);
            System.out.println(service.visualizarMural(true));

            HttpResponder.enviarJson(exchange, 200, resposta);
        } catch (Exception e) {
            HttpResponder.enviarJson(exchange, 400, JsonUtil.erro(e.getMessage()));
        }
    }

    public void handleBorracha(HttpExchange exchange) throws IOException {
        try {
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                HttpResponder.enviarJson(exchange, 405, JsonUtil.erro("Metodo nao permitido"));
                return;
            }
            Map<String, String> body = JsonUtil.parseJsonObject(JsonUtil.readBody(exchange.getRequestBody()));
            int x = Integer.parseInt(body.getOrDefault("x", "0"));
            int y = Integer.parseInt(body.getOrDefault("y", "0"));
            HttpResponder.enviarJson(exchange, 200, service.aplicarBorracha(x, y));
            System.out.println("\n[CLIENTE] Aplicou borracha em (" + x + ", " + y + ")");
            System.out.println(service.visualizarMural(true));
        } catch (Exception e) {
            HttpResponder.enviarJson(exchange, 400, JsonUtil.erro(e.getMessage()));
        }
    }
}
