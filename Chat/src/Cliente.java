import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WinowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAdrress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.*;

public class Cliente {
    public static void main(String[] args){
        MarcoCliente mimarco = new MarcoCliente();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class MarcoCliente extends JFrame{
    public MarcoCliente(){
        setBounds(600,300,280,350);
        LaminaMarcoCliente milamina = new LaminaMarcoCliente();
        add(milamina);
        setVisible(true);
        addWindowListener(new EnvioLine());
    }
}

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
    
}
