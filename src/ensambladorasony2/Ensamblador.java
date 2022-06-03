package ensambladorasony2;

import ensambladorasony2.Fabrica;
import static ensambladorasony2.Const.*;

public class Ensamblador extends Thread{

    private Fabrica f;

    public Ensamblador(Fabrica f){
        this.f = f;
    }

    @Override
    public void run(){

        try {
            
            while (f.isActive()) {

                for (int i = 0; i < 4; i++) 
                    f.consumir(i);

                Thread.sleep( f.getTiempoProduccion( ENSAMBLADOR ) );                

                f.almacenarCelular();

            }        

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }
    
}
