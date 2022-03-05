package insecure.deserialization;

import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.*;
import java.lang.System;

import org.apache.commons.collections4.*;
import org.apache.commons.collections4.CollectionUtils;


public class Server {

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        final boolean val=true;
        int valInt=6;
        int i;

        System.setSecurityManager(null);

        ServerSocket server = null;
        try {
            server = new ServerSocket(8888);
        } catch (IOException e) {
            System.err.println(e.getMessage()); /* port not available*/ return;
        }

        System.out.println(server.getInetAddress().getHostAddress());
        Socket socket = server.accept();

        // Create example notes
        Note note1 = new Note(1, "Test", "This is the main content");
        Note note2 = new Note(2, "Shopping list", "Milk\nEggs\nSausage");
        Note note3 = new Note(3, "Bills", "Don't forget to pay the bills due on the 11th!");

        // Create list to store all notes
        ArrayList<Note> noteList = new ArrayList<Note>();
        noteList.add(note1);
        noteList.add(note2);
        noteList.add(note3);
        System.out.println();

        // Server loop
        while (true) {
            while(true){
                // Create streams
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());


                try (ObjectInputStream ois2 = new ObjectInputStream(socket.getInputStream())) {     //non trovata da semgrep
                    Object o = ois2.readObject();
                    System.out.println(o.toString());
                } catch (Exception e) {
                    return;
                }

                if (val) {
                    ObjectInputStream ois3 = new ObjectInputStream(socket.getInputStream());
                    Object o3 = ois3.readObject();
                    System.out.println(o3.toString());
                }
                else {
                    ObjectInputStream ois4 = new ObjectInputStream(socket.getInputStream());   //viene trovata da semgrep anche se il codice non arrivera mai qui
                    Object o4 = ois4.readObject();      //Spotbugs does not find the vulnerability in the else because val is always true
                    System.out.println(o4.toString());
                }

                switch (valInt) {
                    case 1:
                        System.out.println(valInt);
                        break;
                    case 6:
                        ObjectInputStream ois5 = new ObjectInputStream(socket.getInputStream());
                        Object o5 = ois5.readObject();
                        System.out.println(o5.toString());
                        break;
                    default:
                        break;
                }


                // convert object from client to String so we can check it
                // not sure if this is safe...
                String message=null;
                try {
                    System.out.println(message);
                }catch (Exception e){
                    System.err.println(e);
                }finally {
                    ObjectInputStream ois6 = new ObjectInputStream(socket.getInputStream());
                    message = (String) ois6.readObject();
                    System.out.println(message);
                }

                ObjectInputStream ois6 = (val) ? new ObjectInputStream(socket.getInputStream()): null;
                message = (!val) ? null: (String)ois6.readObject();
                System.out.println(message);

                ObjectInputStream[] oisArray = new ObjectInputStream[10];
                //oisArray[0] = new ObjectInputStream(socket.getInputStream());  found from semgrep
                for (i=0; i<10; oisArray[i] = new ObjectInputStream(socket.getInputStream())) {     //not found from semgrep
                    Object o= oisArray[i].readObject();
                    i++;
                }

                do {
                    ObjectInputStream ois8 = new ObjectInputStream(socket.getInputStream());
                    message = (String) ois8.readObject();
                    i+=10;
                }while (i<100);

                /*
                ByteArrayOutputStream bOutput = new ByteArrayOutputStream(12);
                byte[] b = bOutput.toByteArray();
                ByteArrayInputStream bytesIn = new ByteArrayInputStream(b);
                ObjectInputStream objIn = new ObjectInputStream(bytesIn);
                Object obj = objIn.readObject();
                System.out.println(obj.toString());
                 */

                if(message.equalsIgnoreCase("GET")){
                    // Client wants all of the saved notes
                    oos.writeObject(noteList);
                    continue;

                }else if (message.equalsIgnoreCase("SAVE")){
                    // Client wants to save a note, accept a Note object
                    Note newNote = (Note) ois.readObject();
                    noteList.add(newNote);
                    continue;

                }else if (message.equalsIgnoreCase("BYE")){
                    break;
                }else{
                    // Ignore
                }
            }
        }
    }
}
