package myGamePack;

import java.io.*;
import java.net.Socket;
import java.net.SocketImpl;
import java.util.Arrays;
import java.util.Scanner;

public class Client implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
	private String serverAddress;
	private int port;
	private String username;

    public Client(String serverAddress, int port, String username) {
    	this.serverAddress = serverAddress;
    	this.port = port;
    	this.username = username;
    }

    // Inner class to listen for server messages
    private class ServerListener implements Runnable {
        public void run() {
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    if (serverMessage.contains("COORDSXY")) {
                    	// For now, just set the map of those coordinates to the default player avatar
                    	
                        // Simple one-liner to extract xPos and yPos
                        //String[] parts = serverMessage.split("[(),]");                    	
                    	//Left here becase we are not sure how to test this. But the whole thing is basically done. We will need Zheng for this.
                        //Game.map[(int)Double.parseDouble(parts[2].trim())][(int)Double.parseDouble(parts[1].trim())] = 715; // This is prob gonna be more involved. But the idea is putting sprite in new place, and restoring that there is ntohign where it was
                        //Game.screen.map = Game.map;
                        //Game.screen.numSprites = Game.screen.numSprites+1;
                        //Game.screen.spriteArr = Arrays.copyOf(Game.screen.spriteArr, Game.screen.spriteArr.length + 1);
                        		

                         // Add the new element at the end
                        //Game.screen.spriteArr[Game.screen.numSprites] = new Sprite((int)Double.parseDouble(parts[2].trim()), (int)Double.parseDouble(parts[1].trim())); 
                        		
                        		
                    } else {
                    	System.out.println(serverMessage);
                    }
                	
                	
                }
            } catch (IOException e) {
                System.out.println("Connection closed.");
            }
        }
    }
    
 // Inner class to send our world coordinates and our avatar
    private class CoordSender implements Runnable {
        public void run() {
            //while (true) {
				
			 //   out.println("COORDSXY "+username+": ("+Camera.xPos+","+Camera.yPos+")"); // Send message to server
			//}
        }
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
            socket = new Socket(serverAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start a thread to listen for messages from the server, and sending coords
            new Thread(new ServerListener()).start();
            new Thread(new CoordSender()).start();

            // Main thread for sending user input
            Scanner scanner = new Scanner(System.in);
            System.out.println("Connected to chat. Type messages below:");
            while (true) {
            	System.out.print(username+ ": ");
                String message = username+ ": "+  scanner.nextLine();
                out.println(message); // Send message to server
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
