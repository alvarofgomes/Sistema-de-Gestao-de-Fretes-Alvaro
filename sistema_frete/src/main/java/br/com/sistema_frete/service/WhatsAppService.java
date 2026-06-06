package br.com.sistema_frete.service;

import br.com.sistema_frete.model.Frete;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

public class WhatsAppService {

    private final String accountSid;
    private final String authToken;
    private final String fromNumber;

    public WhatsAppService() {
        Properties props = new Properties();
        try (InputStream in = WhatsAppService.class.getResourceAsStream("/twilio.properties")) {
            if (in == null) {
                throw new IllegalStateException("Arquivo twilio.properties não encontrado no classpath.");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao carregar twilio.properties.", e);
        }
        this.accountSid = props.getProperty("twilio.account.sid");
        this.authToken  = props.getProperty("twilio.auth.token");
        this.fromNumber = props.getProperty("twilio.whatsapp.from");
    }

    public void enviarFreteEmitido(Frete frete) {
        String telefone = telefoneDestinatario(frete);
        if (telefone == null) return;
        String msg = String.format(
            "📦 *Frete Emitido* — N° %s\n" +
            "Origem: %s/%s → Destino: %s/%s\n" +
            "Previsão de entrega: %s",
            frete.getNumero(),
            frete.getCidadeOrigem(), frete.getUfOrigem(),
            frete.getCidadeDestino(), frete.getUfDestino(),
            frete.getDataPrevisaoEntrega()
        );
        enviar(telefone, msg);
    }

    public void enviarSaidaConfirmada(Frete frete) {
        String telefone = telefoneDestinatario(frete);
        if (telefone == null) return;
        String msg = String.format(
            "🚛 *Saída Confirmada* — N° %s\n" +
            "Seu frete saiu de %s/%s e está a caminho de %s/%s.",
            frete.getNumero(),
            frete.getCidadeOrigem(), frete.getUfOrigem(),
            frete.getCidadeDestino(), frete.getUfDestino()
        );
        enviar(telefone, msg);
    }

    public void enviarFreteEmTransito(Frete frete) {
        String telefone = telefoneDestinatario(frete);
        if (telefone == null) return;
        String msg = String.format(
            "🔄 *Em Trânsito* — N° %s\n" +
            "Seu frete está em trânsito para %s/%s.\n" +
            "Previsão de entrega: %s",
            frete.getNumero(),
            frete.getCidadeDestino(), frete.getUfDestino(),
            frete.getDataPrevisaoEntrega()
        );
        enviar(telefone, msg);
    }

    public void enviarFreteEntregue(Frete frete) {
        String telefone = telefoneDestinatario(frete);
        if (telefone == null) return;
        String msg = String.format(
            "✅ *Entrega Realizada* — N° %s\n" +
            "Seu frete foi entregue em %s/%s.",
            frete.getNumero(),
            frete.getCidadeDestino(), frete.getUfDestino()
        );
        enviar(telefone, msg);
    }

    private void enviar(String toNumber, String mensagem) {
        try {
            String apiUrl = "https://api.twilio.com/2010-04-01/Accounts/"
                    + accountSid + "/Messages.json";

            String body = "From=" + encode(fromNumber)
                    + "&To=" + encode("whatsapp:" + toNumber)
                    + "&Body=" + encode(mensagem);

            byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
            String credenciais = Base64.getEncoder()
                    .encodeToString((accountSid + ":" + authToken)
                    .getBytes(StandardCharsets.UTF_8));

            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic " + credenciais);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(bodyBytes.length));
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(bodyBytes);
            }

            int status = conn.getResponseCode();
            if (status < 200 || status >= 300) {
                System.err.println("[WhatsApp] Twilio retornou HTTP " + status
                        + " para o número " + toNumber);
            }
            conn.disconnect();

        } catch (Exception e) {
            System.err.println("[WhatsApp] Erro ao enviar mensagem: " + e.getMessage());
        }
    }

    private String telefoneDestinatario(Frete frete) {
        if (frete.getDestinatario() == null) return null;
        String tel = frete.getDestinatario().getTelefone();
        if (tel == null || tel.trim().isEmpty()) return null;
        return tel.replaceAll("[^\\d+]", "");
    }

    private String encode(String value) throws java.io.UnsupportedEncodingException {
        return java.net.URLEncoder.encode(value, "UTF-8");
    }
}
