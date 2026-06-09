import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.Map;

public class PixelResource {
    private final PixelArtService service;

    public PixelResource(PixelArtService service) {
        this.service = service;
    }

    public void handlePixel(HttpExchange exchange) throws IOException {
        try {
            String metodo = exchange.getRequestMethod();
            if (metodo.equalsIgnoreCase("POST")) {
                Map<String, String> body = JsonUtil.parseJsonObject(JsonUtil.readBody(exchange.getRequestBody()));
                int x = Integer.parseInt(body.getOrDefault("x", "0"));
                int y = Integer.parseInt(body.getOrDefault("y", "0"));
                String cor = body.getOrDefault("cor", "AZUL");
                HttpResponder.enviarJson(exchange, 200, service.pintarPixel(x, y, cor));
                System.out.println("\n[CLIENTE] Pintou o pixel (" + x + ", " + y + ") com a cor " + cor);
                System.out.println(service.visualizarMural(true));
            } else if (metodo.equalsIgnoreCase("DELETE")) {
                String[] partes = exchange.getRequestURI().getPath().split("/");
                int x = Integer.parseInt(partes[2]);
                int y = Integer.parseInt(partes[3]);
                HttpResponder.enviarJson(exchange, 200, service.apagarPixel(x, y));
                System.out.println("\n[CLIENTE] Apagou o pixel (" + x + ", " + y + ")");
                System.out.println(service.visualizarMural(true));
            } else {
                HttpResponder.enviarJson(exchange, 405, JsonUtil.erro("Metodo nao permitido"));
            }
        } catch (Exception e) {
            HttpResponder.enviarJson(exchange, 400, JsonUtil.erro(e.getMessage()));
        }
    }

    public void handlePixels(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            HttpResponder.enviarJson(exchange, 200, service.listarPixelsJson());
        } else {
            HttpResponder.enviarJson(exchange, 405, JsonUtil.erro("Metodo nao permitido"));
        }
    }
}
