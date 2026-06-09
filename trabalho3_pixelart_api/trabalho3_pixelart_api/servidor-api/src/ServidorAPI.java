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

        HttpServer server = HttpServer.create(
    new InetSocketAddress("192.168.100.208", porta),
    0
);
        server.createContext("/mural", muralResource::handle);
        server.createContext("/pixel", pixelResource::handlePixel);
        server.createContext("/pixels", pixelResource::handlePixels);
        server.createContext("/pincel", ferramentaResource::handlePincel);
        server.createContext("/borracha", ferramentaResource::handleBorracha);
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();

        System.out.println("Servidor Pixel Art API iniciado em http://192.168.100.208:" + porta);
        System.out.println("Objetos distribuidos: MuralResource, PixelResource e FerramentaResource");
    }
}
