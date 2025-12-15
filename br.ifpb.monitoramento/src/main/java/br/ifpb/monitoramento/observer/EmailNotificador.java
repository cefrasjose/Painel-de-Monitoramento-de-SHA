package br.ifpb.monitoramento.observer;

import br.ifpb.monitoramento.model.Usuario;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class EmailNotificador implements IObservadorAlerta {

    @Override
    public void notificar(Usuario usuario, double consumoAtual) {
        System.out.println("   📧 [EMAIL] Preparando envio para: " + usuario.getEmail());
        System.out.println("      MOTIVO: Consumo de " + consumoAtual + "m3 excedeu o limite de " + usuario.getLimiteConsumoMensal());

        //Simulacao de envio real (Para funcionar, precisaria de um SMTP real)
        try {
            SimpleEmail email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com"); //Exemplo
            email.setSmtpPort(465);
            email.setAuthentication("seu-email@gmail.com", "sua-senha");
            email.setSSLOnConnect(true);
            email.setFrom("sistema@cagepa.com.br");
            email.setSubject("ALERTA DE CONSUMO: " + usuario.getNome());
            email.setMsg("Prezado cliente, seu consumo atual de " + consumoAtual + " atingiu o limite configurado.");
            email.addTo(usuario.getEmail());

            //email.send(); //Comentado para nao travar sem internet/senha
            System.out.println("      ✅ [EMAIL] Enviado com sucesso (Simulado).");

        } catch (EmailException e) {
            System.err.println("      ❌ [EMAIL] Erro ao enviar: " + e.getMessage());
        }
    }
}