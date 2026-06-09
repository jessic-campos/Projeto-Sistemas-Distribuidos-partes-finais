import java.util.List;

public class PixelArtService {
    private final FilaPersistente fila = new FilaPersistente();
    private final MuralStore store = new MuralStore("dados/mural_estado.ser");
    private final Pincel pincel = new Pincel();
    private final Borracha borracha = new Borracha();

    public synchronized String criarMural(int largura, int altura, String dono) {
        fila.publicar(EventoFila.criarMural(largura, altura, dono));
        return JsonUtil.ok("Evento CRIAR_MURAL publicado na fila. O WorkerFila processara quando estiver ativo.");
    }

    public synchronized String pintarPixel(int x, int y, String cor) {
        fila.publicar(EventoFila.pintarPixel(x, y, cor));
        return JsonUtil.ok("Evento PINTAR_PIXEL publicado na fila para (" + x + ", " + y + ").");
    }

    public synchronized String apagarPixel(int x, int y) {
        fila.publicar(EventoFila.apagarPixel(x, y));
        return JsonUtil.ok("Evento APAGAR_PIXEL publicado na fila para (" + x + ", " + y + ").");
    }

    public synchronized String aplicarPincel(int x1, int y1, int x2, int y2, String cor) {
        fila.publicar(EventoFila.aplicarPincel(x1, y1, x2, y2, cor));
        return JsonUtil.ok("Evento APLICAR_PINCEL publicado na fila de (" + x1 + ", " + y1 + ") ate (" + x2 + ", " + y2 + ").");
    }

    public synchronized String aplicarBorracha(int x, int y) {
        fila.publicar(EventoFila.aplicarBorracha(x, y));
        return JsonUtil.ok("Evento APLICAR_BORRACHA publicado na fila para (" + x + ", " + y + ").");
    }

    public synchronized String listarPixelsJson() {
        Mural mural = store.carregar();
        List<Pixel> pixels = mural.listarPixels();
        StringBuilder sb = new StringBuilder();
        sb.append("{\"ok\":true,\"pixels\":[");
        for (int i = 0; i < pixels.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append(pixels.get(i).toJson());
        }
        sb.append("],\"mensagensPendentes\":").append(fila.tamanho()).append("}");
        return sb.toString();
    }

    public synchronized String visualizarMuralJson(boolean ansi) {
        Mural mural = store.carregar();
        String visual = ansi ? mural.visualizarMuralAnsi() : mural.visualizarMuralTexto();
        return "{\"ok\":true,\"mural\":\"" + JsonUtil.escape(visual) + "\",\"mensagensPendentes\":" + fila.tamanho() + ",\"dados\":" + mural.toJson() + "}";
    }

    public synchronized String visualizarMural(boolean ansi) {
        Mural mural = store.carregar();
        return ansi ? mural.visualizarMuralAnsi() : mural.visualizarMuralTexto();
    }

    public synchronized String statusFilaJson() {
        return "{\"ok\":true,\"eventosPendentes\":" + fila.tamanho() + "}";
    }

    public synchronized boolean processarProximoEvento() {
        EventoFila evento = fila.consumir();
        if (evento == null) {
            return false;
        }

        Mural mural = store.carregar();
        aplicarEventoNoMural(mural, evento);
        store.salvar(mural);

        System.out.println("\n[WORKER] Evento processado: " + evento.getTipo());
        System.out.println(mural.visualizarMuralAnsi());
        System.out.println("[WORKER] Mensagens restantes na fila: " + fila.tamanho());
        return true;
    }

    private void aplicarEventoNoMural(Mural mural, EventoFila evento) {
        switch (evento.getTipo()) {
            case "CRIAR_MURAL":
                mural.criar(evento.getLargura(), evento.getAltura(), evento.getDono());
                break;

            case "PINTAR_PIXEL":
                mural.pintarPixel(evento.getX(), evento.getY(), evento.getCor());
                break;

            case "APAGAR_PIXEL":
                mural.apagarPixel(evento.getX(), evento.getY());
                break;

            case "APLICAR_PINCEL":
                pincel.aplicarArea(mural, evento.getX1(), evento.getY1(), evento.getX2(), evento.getY2(), evento.getCor());
                break;

            case "APLICAR_BORRACHA":
                borracha.aplicar(mural, evento.getX(), evento.getY(), "BRANCO");
                break;

            default:
                System.out.println("[WORKER] Evento desconhecido ignorado: " + evento.getTipo());
        }
    }
}
