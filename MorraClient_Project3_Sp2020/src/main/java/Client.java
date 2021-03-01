//import com.sun.corba.se.spi.activation.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;


public class Client extends Thread {

    // Initialzie Variables
    Socket socketClient;
    int port;
    String ip;
    ObjectOutputStream out;
    ObjectInputStream in;
    private Consumer<Serializable> callback;
    ArrayList<ServerSocket> clients = new ArrayList<ServerSocket>();
    String temp;
    Logic gameInfo;
    




    //Client constructor
    Client(Consumer<Serializable> call, int portNum, String ipAd){
        port = portNum;
        callback = call;
        gameInfo = new Logic();
        ip = ipAd;

    }




    public void run() {
        try {
            //socketClient
            socketClient= new Socket("127.0.0.1",port);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);

            while(true) {
                System.out.print("start\n");
                Serializable data = (Serializable) in.readObject();
                temp = data.toString();
                System.out.print(data + "\n");
                callback.accept(data);
                System.out.print("end\n");
            }
        }
        catch(Exception e) {}
    }

    // Send player Object
    public void send(player p) {

        try {
            out.writeObject(p);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
