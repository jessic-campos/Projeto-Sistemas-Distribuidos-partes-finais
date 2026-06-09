public class Borracha extends Ferramenta {
    public Borracha() { super("Borracha"); }

    @Override
    public void aplicar(Mural mural, int x, int y, String cor) {
        mural.apagarPixel(x, y);
    }
}
