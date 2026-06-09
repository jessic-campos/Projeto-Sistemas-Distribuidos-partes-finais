import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    public static String readBody(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static Map<String, String> parseJsonObject(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null) return map;
        String texto = json.trim();
        if (texto.startsWith("{")) texto = texto.substring(1);
        if (texto.endsWith("}")) texto = texto.substring(0, texto.length() - 1);

        StringBuilder atual = new StringBuilder();
        boolean dentroAspas = false;
        java.util.List<String> partes = new java.util.ArrayList<>();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '"' && (i == 0 || texto.charAt(i - 1) != '\\')) dentroAspas = !dentroAspas;
            if (c == ',' && !dentroAspas) {
                partes.add(atual.toString());
                atual.setLength(0);
            } else {
                atual.append(c);
            }
        }
        if (!atual.isEmpty()) partes.add(atual.toString());

        for (String parte : partes) {
            String[] kv = parte.split(":", 2);
            if (kv.length == 2) {
                String chave = limpar(kv[0]);
                String valor = limpar(kv[1]);
                map.put(chave, valor);
            }
        }
        return map;
    }

    private static String limpar(String valor) {
        valor = valor.trim();
        if (valor.startsWith("\"") && valor.endsWith("\"")) {
            valor = valor.substring(1, valor.length() - 1);
        }
        return valor.replace("\\\"", "\"");
    }

    public static String ok(String mensagem) {
        return "{\"ok\":true,\"mensagem\":\"" + escape(mensagem) + "\"}";
    }

    public static String erro(String mensagem) {
        return "{\"ok\":false,\"erro\":\"" + escape(mensagem) + "\"}";
    }

    public static String escape(String texto) {
    if (texto == null) return "";

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < texto.length(); i++) {
        char c = texto.charAt(i);

        switch (c) {
            case '\\':
                sb.append("\\\\");
                break;

            case '"':
                sb.append("\\\"");
                break;

            case '\n':
                sb.append("\\n");
                break;

            case '\r':
                sb.append("\\r");
                break;

            case '\t':
                sb.append("\\t");
                break;

            default:
                if (c < 32) {
                    sb.append(String.format("\\u%04X", (int) c));
                } else {
                    sb.append(c);
                }
        }
    }

    return sb.toString();
}
}
