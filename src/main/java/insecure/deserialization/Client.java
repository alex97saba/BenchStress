package insecure.deserialization;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;

        // Args
        String server_ip = null;
        int port = 0;

        // If you want to launch it locally
        server_ip = "127.0.0.1";
        port = 8888;

        /* If you want to choose server_ip and port
        if (args.length==2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[1] + " must be an integer.");
                System.exit(1);
            }
            server_ip = args[0];
        } else {
            System.err.println("Usage: client <server_ip> <port>");
            System.exit(1);
        }
        */

        // Scanner
        Scanner scan = new Scanner(System.in);
        // Setup socket
        System.out.println("Connecting to server @ " + server_ip + ":" + port + "...");
        socket = new Socket(server_ip, port);
        System.out.println("Connected!\n");
        System.out.println("WELCOME TO NOTE KEEPER CLIENT V1.42\n");

        while (true){
            System.out.println("========= ACTIONS =========");
            System.out.println("1    - Print all notes");
            System.out.println("2    - Add a new note");
            System.out.println("EXIT - Exit this program\n");
            System.out.print("Select an action to perform: ");
            String selection = scan.nextLine();

            // Setup streams
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            // Do action
            if (selection.equals("1")){
                // Print all notes from server
                // Tell server we want all the all the notes
                oos.writeObject("GET");
                oos.flush();
                Thread.sleep(100);
                // Save the data from the server
                ArrayList<Note> noteList = (ArrayList<Note>) ois.readObject();
                // Print all the notes
                System.out.println("=========================================");
                System.out.println("================= NOTES =================");
                System.out.println("=========================================\n");

                for (Note n : noteList){
                    n.print_note();
                }

                System.out.println("\n=========================================");
                System.out.println("=============== END NOTES ===============");
                System.out.println("=========================================\n");
                continue;
            } else if ( selection.equals("2")){
                // Send a new note to the server
                System.out.println("Please enter the following information to create a new note...");
                System.out.print("Title: ");
                String newNoteTitle = scan.nextLine();
                System.out.print("Body: ");
                String newNoteBody = scan.nextLine();
                Note newNote = new Note(42, newNoteTitle, newNoteBody);
                oos.writeObject("SAVE");
                Thread.sleep(100);
                oos.writeObject(newNote);
                System.out.println("The note has been sent to the server!");
            } else if (selection.equalsIgnoreCase("exit")){
                // Quit
                System.out.println("Exiting...");
                break;
            } else {
                // Invalid action
                System.out.println("Invalid Action...");
                continue;
            }
        }
    }
}