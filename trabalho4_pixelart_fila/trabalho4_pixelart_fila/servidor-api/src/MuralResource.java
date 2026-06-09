import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.Map;

public class MuralResource {
    private final PixelArtService service;

    public MuralResource(PixelArtService service) {
        this.service = service;
    }

    public void handle(HttpExchange exchange) throws IOException {
        try {
            String metodo = exchange.getRequestMethod();

            if (metodo.equalsIgnoreCase("POST")) {
                Map<String, String> body = JsonUtil.parseJsonObject(JsonUtil.readBody(exchange.getRequestBody()));
                int largura = Integer.parseInt(body.getOrDefault("largura", "10"));
                int altura = Integer.parseInt(body.getOrDefault("altura", "10"));
                String dono = body.getOrDefault("dono", "Victor");

                String resposta = service.criarMural(largura, altura, dono);
                System.out.println("\n[API/PRODUTOR] Evento CRIAR_MURAL enviado para a fila.");
                System.out.println("[API/PRODUTOR] " + service.statusFilaJson());

                HttpResponder.enviarJson(exchange, 200, resposta);
            } else if (metodo.equalsIgnoreCase("GET")) {
                String query = exchange.getRequestURI().getQuery();
                boolean ansi = query != null && query.contains("ansi=true");
                HttpResponder.enviarJson(exchange, 200, service.visualizarMuralJson(ansi));
            } else {
                HttpResponder.enviarJson(exchange, 405, JsonUtil.erro("Metodo nao permitido"));
            }
        } catch (Exception e) {
            HttpResponder.enviarJson(exchange, 400, JsonUtil.erro(e.getMessage()));
        }
    }
}
