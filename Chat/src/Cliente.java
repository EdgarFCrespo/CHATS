public class Cliente {
    public static void main(String[] args){
        
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
        
    }
}
