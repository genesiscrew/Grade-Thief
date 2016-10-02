package f_server;

        import java.awt.BorderLayout;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.io.EOFException;
        import java.io.IOException;
        import java.io.ObjectInputStream;
        import java.io.ObjectOutputStream;
        import java.io.PrintStream;
        import java.net.InetAddress;
        import java.net.ServerSocket;
        import java.net.Socket;
        import java.util.Scanner;

        import javax.swing.JScrollPane;
        import javax.swing.JTextArea;
        import javax.swing.JTextField;
        import javax.swing.SwingUtilities;
        import javax.tools.DocumentationTool.Location;

        import characters.Player;
        import gui.GameController;
        import model.Game;

public class Client {
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
    private PrintStream p;
    private Scanner sc;

    private GameController player1;
    private GameController guard1= new GameController(true);
    private Scanner getInput = new Scanner(System.in);

    // Constructor
    public Client(String host) {
        serverIP = host;
    }

    // Connect to server
    public void startRunning() {
        try {

            connectionToServer();
            setupStreams();
            // whileGame();
            update();

        } catch (EOFException e) {
            System.out.println("Client terminated connection");
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            closeCrap();
        }
    }

    // Connecting to Server
    private void connectionToServer() throws IOException {
        System.out.println("Attempting connection...");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        System.out.println("Connected to: " + connection.getInetAddress().getHostName());
    }

    // set up the stream to send and recieve message!
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        System.out.println("You are now connected and you can send message!");
    }

    // While game
    private void whileGame() throws IOException {
        boolean continueType = true;
        do {
            while (continueType) {
                System.out.print("Write anything man: ");
                String message = getInput.nextLine();
                sendMessage(message);
                if (message.equalsIgnoreCase("END")) {
                    continueType = false;
                }
            }

            appearMessage();
        } while (true);
    }

    private void update() {
        while (true) {
            try {
                sendData();
                receievData();
            } catch (IOException e) {

            }

        }
    }

    // close after its done
    private void closeCrap() {
        System.out.println("Closing the chat");
        try {

            output.close();
            input.close();
            connection.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) throws IOException {
        p = new PrintStream(connection.getOutputStream());
        //p.println(player.getCharacterLocation().toString());
    }

    private void appearMessage() throws IOException {
        sc = new Scanner(connection.getInputStream());
        System.out.println(sc.nextLine());
    }

    // Send data tu server
    private void sendData() throws IOException {
        // After each movement it send the coordinates of the client to the
        // Server
        double []guard = guard1.getOtherPlayersPosition(true);
        output.writeObject(guard);


        p.println(guard[1]);
        output.flush();
        //double []player = player1.getOtherPlayersPosition(false);
        //output.writeObject(player);
        //output.flush();
    }

    // recieve data from server
    private void receievData() throws IOException {
        // Server will send the updated board to the client
        try {
            /*double []updatedGuard = (double []) input.readObject();
            this.guard1.updatePosition(true, updatedGuard);*/
            double []updatedplayer = (double []) input.readObject();
            this.player1.updatePosition(false, updatedplayer);

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

    }



}

