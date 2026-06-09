import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FilaPersistente {
    private static final Path FILA = Paths.get("fila_eventos.txt");
    private static final Path LOCK = Paths.get("fila_eventos.lock");

    public FilaPersistente() {
        try {
            if (!Files.exists(FILA)) {
                Files.createFile(FILA);
            }
            if (!Files.exists(LOCK)) {
                Files.createFile(LOCK);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar fila persistente: " + e.getMessage(), e);
        }
    }

    public void publicar(EventoFila evento) {
        executarComLock(() -> {
            try {
                Files.writeString(
                    FILA,
                    evento.toJson() + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
                );
                return null;
            } catch (IOException e) {
                throw new RuntimeException("Erro ao publicar evento na fila: " + e.getMessage(), e);
            }
        });
    }

    public EventoFila consumir() {
        return executarComLock(() -> {
            try {
                List<String> linhas = Files.readAllLines(FILA, StandardCharsets.UTF_8);

                if (linhas.isEmpty()) {
                    return null;
                }

                String primeira = linhas.get(0);
                List<String> restantes = new ArrayList<>();

                for (int i = 1; i < linhas.size(); i++) {
                    restantes.add(linhas.get(i));
                }

                Files.write(FILA, restantes, StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE
                );

                return EventoFila.fromJson(primeira);

            } catch (IOException e) {
                throw new RuntimeException("Erro ao consumir evento da fila: " + e.getMessage(), e);
            }
        });
    }

    public int tamanho() {
        return executarComLock(() -> {
            try {
                if (!Files.exists(FILA)) return 0;
                return Files.readAllLines(FILA, StandardCharsets.UTF_8).size();
            } catch (IOException e) {
                throw new RuntimeException("Erro ao consultar tamanho da fila: " + e.getMessage(), e);
            }
        });
    }

    private <T> T executarComLock(Supplier<T> acao) {
        try (FileChannel channel = FileChannel.open(
                LOCK,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE
        )) {
            try (FileLock lock = channel.lock()) {
                return acao.get();
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro na fila persistente: " + e.getMessage(), e);
        }
    }
}