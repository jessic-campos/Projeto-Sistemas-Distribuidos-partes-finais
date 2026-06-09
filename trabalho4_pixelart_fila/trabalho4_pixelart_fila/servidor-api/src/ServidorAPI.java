import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class ServidorAPI {
    public static void main(String[] args) throws Exception {
        int porta = 8080;
        PixelArtService service = new PixelArtService();

        MuralResource muralResource = new MuralResource(service);
        PixelResource pixelResource = new PixelResource(service);
        FerramentaResource ferramentaResource = new FerramentaResource(service);
        FilaResource filaResource = new FilaResource(service);

        HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);

        server.createContext("/mural", muralResource::handle);
        server.createContext("/pixel", pixelResource::handlePixel);
        server.createContext("/pixels", pixelResource::handlePixels);
        server.createContext("/pincel", ferramentaResource::handlePincel);
        server.createContext("/borracha", ferramentaResource::handleBorracha);
        server.createContext("/fila/status", filaResource::handle);

        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();

        System.out.println("Servidor Pixel Art API iniciado em http://localhost:" + porta);
        System.out.println("Comunicacao indireta: API publica eventos na fila persistente.");
        System.out.println("Objetos distribuidos: MuralResource, PixelResource, FerramentaResource e FilaResource.");
        System.out.println("Inicie o consumidor em outro terminal com: java -cp bin WorkerFila");
    }
}
