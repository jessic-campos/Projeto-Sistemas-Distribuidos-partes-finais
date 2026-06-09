public class WorkerFila {
    public static void main(String[] args) throws Exception {
        PixelArtService service = new PixelArtService();

        System.out.println("WorkerFila iniciado.");
        System.out.println("Este processo consome mensagens da fila persistente e atualiza o mural.");
        System.out.println("Para demonstrar desacoplamento temporal, desligue este worker, envie acoes pelos clientes e ligue-o novamente.");

        while (true) {
            boolean processou = service.processarProximoEvento();

            if (!processou) {
                Thread.sleep(1500);
            } else {
                Thread.sleep(500);
            }
        }
    }
}
