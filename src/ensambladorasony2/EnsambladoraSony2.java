package ensambladorasony2;

import ensambladorasony2.Fabrica;
import ensambladorasony2.GUI_Simulacion;

public class EnsambladoraSony2 {

    public static Fabrica f1 = new Fabrica(1, 0, 2, 2, 1, 600 );
    public static Fabrica f2 = new Fabrica(2, 6, 3, 4, 2, 1050 );
    public static boolean activo = false;
    public static GUI_Simulacion gui = new GUI_Simulacion();
    
    public static void detenerSimulacion(){
        activo = false;
        f1.detener();
        f2.detener();
        f1.interrupt();
        f2.interrupt();
        f1 = new Fabrica(1, 0, 2, 2, 1, 600 );
        f2 = new Fabrica(2, 6, 3, 4, 2, 1050 );
    }
    
    public static void empezarSimulacion(){
        activo = true;
        
        f1.start();
        f2.start();
    }
    
    public static void main(String[] args) {
        
        gui.setVisible(true);
               
    }
    
}
