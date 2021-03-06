package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SimpleServer extends Thread {
	
	public volatile int currentCenterX;
	public volatile int currentCenterY;
	private ServerSocket serverSocket;
	private int port = 5805;
	
	public SimpleServer() throws IOException {
		serverSocket = new ServerSocket(port);
		currentCenterX = 0;
		currentCenterY = 0;
		this.start();
	}
	
	public void run() {
		try {
			System.out.println("Here before connect");
			Socket server = serverSocket.accept();
			System.out.println("Here after connect");
			DataOutputStream dOut = new DataOutputStream(server.getOutputStream());
			while (true) {
				//byte[] packet = new byte[8]; //length of two doubles
				//ByteBuffer buffer = ByteBuffer.wrap(packet);
				//buffer.putInt(0, currentCenterX);
				//buffer.putInt(1, currentCenterY);
				String point = currentCenterX + " " + currentCenterY;
				System.out.println(point);
				dOut.writeUTF(point);
				dOut.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
