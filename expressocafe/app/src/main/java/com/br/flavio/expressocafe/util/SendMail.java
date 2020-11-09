package com.br.flavio.expressocafe.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/*
Flávio Oliveira
https://github.com/oliveiradeflavio
 */

public class SendMail extends AsyncTask<Void,Void,Void> {


    //Declarando as variáveis
    private Context context;
    private Session session;

    String nomeCliente, nomeConferente, data, mensagem, caminhoPedido;
    // private String message;


    //Progressdialog será mostrando enquanto envia o email
    private ProgressDialog progressDialog;

    //Construtor da Classe
    public SendMail(Context context, String nomeConferente, String data, String mensagem, String caminhoPedido) {

        //Inicializando as variáveis
        this.context = context;
        this.nomeCliente = nomeCliente;
        this.nomeConferente = nomeConferente;
        this.data = data;
        this.mensagem = mensagem;
        this.caminhoPedido = caminhoPedido;

        // this.email = email;
        // this.subject = subject;
        //this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Exibe o progressDialog enquando envia a mensagem
        progressDialog = ProgressDialog.show(context, "Enviando pedido", "Por favor aguarde...", false, false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Finalizando o progressDialog
        progressDialog.dismiss();
        //Mostra a messagem, caso for sucesso
        Toast.makeText(context, "Seu pedido foi enviado para o email  " +
                "no App", Toast.LENGTH_LONG).show();

    }

    @Override
    protected Void doInBackground(Void... params) {
        //Criando as propriedades
        Properties props = new Properties();

        //configurando as propriedades do gmail
        //se você não usa gmail você precisa alterar as configurações
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Criando uma nova sessão
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Autenticando o password do e-mail
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                    }
                });

        try {
            //Criando o objeto MimeMessage
            MimeMessage mm = new MimeMessage(session);
            MimeBodyPart mb = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();

            //Configurando o endereço de envio
            mm.setFrom(new InternetAddress(Config.EMAIL));


            //Adicionando o e-mail do destinatário
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress("emaildestinatario@destinario.com.br"));

            //Adicionando o Assunto
            mm.setSubject("Pedido pelo App Expresso Café");

            mb.attachFile(new File(caminhoPedido));


            //anexo
//            mm.setFileName(caminhoPedido);

            //Adicionando a mensagem
            mm.setText( "Nome: " + nomeCliente + "\n" +
                    "Conferente: " +  nomeConferente+ "\n" +
                    "Data: " + data + "\n" +
                    "Pedidos: " + mensagem + "\n");

            multipart.addBodyPart(mb);
            mm.setContent(multipart);

            //Enviando email
            Transport.send(mm);


        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}