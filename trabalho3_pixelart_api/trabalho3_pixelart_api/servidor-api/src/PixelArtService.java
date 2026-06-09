import java.util.List;

public class PixelArtService {
    private final Mural mural = new Mural();
    private final Pincel pincel = new Pincel();
    private final Borracha borracha = new Borracha();

    public synchronized String criarMural(int largura, int altura, String dono) {
        mural.criar(largura, altura, dono);
        return JsonUtil.ok("Mural criado com " + largura + "x" + altura + " para " + dono);
    }

    public synchronized String pintarPixel(int x, int y, String cor) {
        mural.pintarPixel(x, y, cor);
        return JsonUtil.ok("Pixel pintado em (" + x + ", " + y + ") com a cor " + cor.toUpperCase());
    }

    public synchronized String apagarPixel(int x, int y) {
        mural.apagarPixel(x, y);
        return JsonUtil.ok("Pixel apagado em (" + x + ", " + y + ")");
    }

    public synchronized String aplicarPincel(int x1, int y1, int x2, int y2, String cor) {
    pincel.aplicarArea(mural, x1, y1, x2, y2, cor);

    return JsonUtil.ok(
        "Pincel aplicado de (" + x1 + ", " + y1 + ") ate (" + x2 + ", " + y2 + ") com a cor " + cor.toUpperCase()
    );
}

    public synchronized String aplicarBorracha(int x, int y) {
        borracha.aplicar(mural, x, y, "BRANCO");
        return JsonUtil.ok("Borracha aplicada em (" + x + ", " + y + ")");
    }

    public synchronized String listarPixelsJson() {
        List<Pixel> pixels = mural.listarPixels();
        StringBuilder sb = new StringBuilder();
        sb.append("{\"ok\":true,\"pixels\":[");
        for (int i = 0; i < pixels.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append(pixels.get(i).toJson());
        }
        sb.append("]}");
        return sb.toString();
    }

    public synchronized String visualizarMuralJson(boolean ansi) {
        String visual = ansi ? mural.visualizarMuralAnsi() : mural.visualizarMuralTexto();
        return "{\"ok\":true,\"mural\":\"" + JsonUtil.escape(visual) + "\",\"dados\":" + mural.toJson() + "}";
    }

    public synchronized String visualizarMural(boolean ansi) {
    return ansi ? mural.visualizarMuralAnsi() : mural.visualizarMuralTexto();
}
}
