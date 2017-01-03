import java.io.*;
import java.net.*;
import javax.imageio.*;
import java.awt.image.*;
import javax.swing.*;



public class photoreceiver {
	public static void main(String[] args) throws IOException {
	
	ServerSocket serverSock = new ServerSocket(6013);
	
	while (true) {
		System.out.println("Waiting");
		Socket client = serverSock.accept();
		System.out.println("Connected");
		
		
		DataInputStream dis = new DataInputStream(client.getInputStream());
		//int filesize = dis.readInt();
		byte[] bytearray = new byte[12000000];
		dis.read(bytearray);
		
		InputStream in = new ByteArrayInputStream(bytearray);
		BufferedImage bi = ImageIO.read(in);
		
		ImageIO.write(bi, "png", new File("SentPhoto.png"));
		
		JFrame f = new JFrame("Server");
		ImageIcon icon = new ImageIcon(bi);
		JLabel l = new JLabel();
		
		l.setIcon(icon);
		f.add(l);
		f.pack();
		f.setVisible(true);
		
		client.close();
		}
	}
}