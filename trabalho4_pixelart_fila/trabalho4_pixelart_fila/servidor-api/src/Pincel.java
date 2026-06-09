public class Pincel extends Ferramenta {
    public Pincel() {
        super("Pincel");
    }

    @Override
    public void aplicar(Mural mural, int x, int y, String cor) {
        mural.pintarPixel(x, y, cor);
    }

    public void aplicarArea(Mural mural, int x1, int y1, int x2, int y2, String cor) {
        int inicioX = Math.min(x1, x2);
        int fimX = Math.max(x1, x2);
        int inicioY = Math.min(y1, y2);
        int fimY = Math.max(y1, y2);

        for (int y = inicioY; y <= fimY; y++) {
            for (int x = inicioX; x <= fimX; x++) {
                mural.pintarPixel(x, y, cor);
            }
        }
    }
}