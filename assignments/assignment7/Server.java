package assignment7;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Scanner;

public class Server implements Runnable{
    private Socket client;
    private String root;

    public Server(Socket client, String root) {
        this.client = client;
        this.root = root;
    }

    public void run() {
        String reqLine;

        System.out.println("[+] connection accepted, client: " + client.getInetAddress());

        try (
            Scanner in = new Scanner(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream())
        ) {

            if (in.hasNextLine()) {
                reqLine = in.nextLine();
            } else {
                out.close();
                in.close();
                client.close();
                return;
            }

            System.out.println(reqLine);

            StringBuilder response = new StringBuilder();

            String method = reqLine.split(" ")[0];
            String fileName = reqLine.split(" ")[1];
            String httpVersion = reqLine.split(" ")[2];

            if (!method.equals("GET")) {
                out.write(new String(httpVersion + " 405 Method Not Allowed\r\n").getBytes());
                out.close();
                in.close();
                client.close();
                return;
            }

            if (fileName.equals("/")) {
                fileName = "/index.html";
            }

            File file = new File(root+fileName);

            if (!file.exists()) {
                out.write(new String(httpVersion + " 404 Not found\r\n").getBytes());
                out.close();
                in.close();
                client.close();
                return;
            }

            FileInputStream fileStream = new FileInputStream(file);
            response.append(httpVersion + " 200 OK\r\n");

            response.append("Content-Length: " + file.length() + "\r\n");
            response.append("Content-Type: " + Files.probeContentType(file.toPath()) + "\r\n");

            response.append("\r\n");
            
            out.write(response.toString().getBytes());
            out.write(fileStream.readAllBytes());

            fileStream.close();

            out.close();
            in.close();
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
