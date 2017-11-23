//importacion de librerias para identificar lo que hace el raton y lo que sucede en la ventana
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WinowAdapter;
import java.awt.event.WindowEvent;
//importacion de librerias para salida de datos, entrada, salida y conversion a bits de objetos y sus excepciones
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
//importacion de librerias para obtener y poder usar la IP, crear servidores y sockets con su excepcion
import java.net.InetAdrress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
//librerias para listas de arreglos
import java.util.ArrayList;
import javax.swing.*;

//inicio clase Cliente
public class Cliente {
	//metodo main para mandar llamar la clase 
    public static void main(String[] args){
        MarcoCliente mimarco = new MarcoCliente();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}//fin clase Cliente

//incio clase MarcoCliente
class MarcoCliente extends JFrame{

	//Constructor para la ventana principal
	public MarcoCliente(){
        setBounds(600,300,280,350);
        LaminaMarcoCliente milamina = new LaminaMarcoCliente();
        add(milamina);
        setVisible(true);
        addWindowListener(new EnvioLine());
    }
}//fin clase MarcoCliente

//=============Envia señal=====================
class EnvioLine extends WindowAdapter{
    public void windowOpened(WindowEvent e){
        try{
            Socket misocket = new Socket("192.68.0.13",9999);
            Paquete_Envio datos = new Paquete_Envio();
            datos.setMensaje("online");
            ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOuputStream());
            paquete_datos.writeObject(datos);
        }catch(Exception e2){}
    }
}

class LaminaMarcoCliente extends JPanel implements Runnable{
    public LaminaMarcoCliente(){
        String nick_usuario = JOptionPane.showInputDialog("CUAL ES SU NICK:");
        JLabel n_nick = new JLabel("Nick: ");
        
        nick = new JLabel();
        nick.setText(nick_usuario);
        add(nick);
        
        JLabel texto = new JLabel("| En linea: ");
        add(texto);
        
        ip = new JComboBox();
        //ip.addItem("1");
		//ip.addItem("2");
		//ip.addItem("3");
		//ip.addItem("192.168.0.14");
		add(ip);

	    
        campochat = new JTextArea(12,20);
        add(campochat);
        
        campo1  = new JTextField(20);
        add(campo1);
        
        miboton = new JButton("Enviar");
        EnviaTexto mievento = new EnviaTexto();
        miboton.addActionListener(mievento);

	    add(miboton);
        
        Thread mihilo = new Thread(this);
        mihilo.start();        
    }
    
    private class EnviaTexto implements ActionListener{
        @Override
	public void actionPerformed(ActionEvent arg0){
		campochat.append("\n" + campo1.getText());
		
		try{
			//servidor
			Socket misocket = new Socket("192.168.0.13", 9999);
			Paquete_Envio datos = new Paquete_Envio();
			datos.setNick(nick.getText());
			datos.setMensaje(campo1.getText());
			datos.setIp(ip.getSelectedItem().toString());
			ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());
			paquete_datos.writeObject(datos);
			misocket.close();
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
    }
	//Declaracion de componentes
	private JTextArea campochat;
	private JtextField campo1;
	private JComboBox ip;
	private JLabel nick;
	private JButton miboton;
	private String mia;
	
	@Override
	public void run(){
		try{
			mia = InetAddress.getLocalHost().getHostAddress();
		}catch(UnknownHostException e1){
			e1.printStackTrace();
		}
		
		try{
			ServerSocket servidor_cliente= new ServerSocket(9090);
			Socket cliente;
			Paquete_Envio PaqueteRecibido;
			
			while(true){
				cliente = servidor_cliente.accept();
				ObjectInputStream flujoentrada = new ObjectInputStream(cliente.getInputStream());
				PaqueteRecibido = (Paquete_Envio)flujoentrada.readObject();
				if(!PaqueteRecibido.getMensaje().equals("online")){
					campochat.append("\n" + PaqueteRecibido.getNick() + ": " + PaqueteRecibido.getMensaje());
				}else{
					ArrayList <String> ipsMenu = new ArrayList<String>();
					ipsMenu = PaqueteRecibido.getIps();
					ip.removeAllItems();
					
					for(String z:ipsMenu){
						if(z.equals(mia)){
							System.out.println("Ya entre");
						}else{
							ip.addItem(z);
						}
					}
				}
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}

class Paquete_Envio implements Serializable{
	private String nick, ip, mensaje;
	private ArrayList <String> ips;
	
	public ArrayList<String> getIps(){
		return ips;
	}
	public void setIps(ArrayList<String> ips){
		this.ips = ips;
	}
	public String getNick(){
		return nick;
	}
	public void setNick(String nick){
		this.nick = nick;
	}
	public String getIp(){
		return ip;
	}
	public void setIp(String ip){
		this.ip = ip;
	}
	public String getMensaje(){
		return mensaje;
	}
	public void setMensaje(String mensaje){
		this.mensaje = mensaje;
	}
}
