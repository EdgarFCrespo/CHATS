import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		
	}	
}

class MarcoServidor extends JFrame implements Runnable {
	
	public MarcoServidor(){
		
		setBounds(600,300,280,350);				
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);
		
		Thread mihilo=new Thread(this);
		mihilo.start();
		
		}
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//System.out.println("Ya escucha");
		
		try {
			ServerSocket servidor=new ServerSocket(9999);
			
			ArrayList <String> listap=new ArrayList<String>();
			String nick,ip,mensaje;
			
			Paquete_Envio paquete_recibido;
		
			
			while(true) {
			Socket misocket=servidor.accept();
			
			ObjectInputStream paquete_datos=new ObjectInputStream(misocket.getInputStream());
			
			paquete_recibido = (Paquete_Envio) paquete_datos.readObject();
			
			nick=paquete_recibido.getNick();
			ip=paquete_recibido.getIp();
			mensaje=paquete_recibido.getMensaje();
			
			//DataInputStream flujo_entrada=new DataInputStream(misocket.getInputStream());
			//String mensaje_texto=flujo_entrada.readUTF();
			//areatexto.append("\n" + mensaje_texto);
			
			
			if(!mensaje.equals(" online")) {
			areatexto.append("\n"+nick+":"+mensaje+" para: "+ip);
			
			Socket enviarDestino=new Socket(ip,9090);
			ObjectOutputStream paqueteReenvio=new ObjectOutputStream(enviarDestino.getOutputStream());
			
			paqueteReenvio.writeObject(paquete_recibido);
			paqueteReenvio.close();
			enviarDestino.close();
			
			misocket.close();
			}else {
				
				//=========================EN LINEA========================
				
				InetAddress localizacion=misocket.getInetAddress();
				String ipremota=localizacion.getHostAddress();
				System.out.println(ipremota);
				
				if(listap.contains(ipremota)){
				}else {
					listap.add(ipremota);
				}
				
				paquete_recibido.setIps(listap);
				
				for(String z:listap) {
					System.out.println("Array: " + z);
					Socket enviarDestino=new Socket(z,9090);
					ObjectOutputStream paqueteReenvio=new ObjectOutputStream(enviarDestino.getOutputStream());
					paqueteReenvio.writeObject(paquete_recibido);
					paqueteReenvio.close();
					enviarDestino.close();
					misocket.close();
				}	
							
				//=========================================================
			}
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private	JTextArea areatexto;
}