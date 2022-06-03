package ensambladorasony2;

import ensambladorasony2.Fabrica;
import static ensambladorasony2.Const.*;
import static ensambladorasony2.EnsambladoraSony2.gui;

import java.util.Random;

public class Gerente extends Thread{
    
    private int celulares_lanzados = 0;

    public int getCelulares_lanzados() {
        return celulares_lanzados;
    }

    private Fabrica f;
    private String estado;
    private Contador contador;

    public void lanzamiento(){
        int cel = f.lanzamiento();
        celulares_lanzados += cel;
        contador.lanzamiento( cel );
        
        gui.set_deployed(celulares_lanzados, f.n_fabrica);
    }

    public Gerente(Fabrica f){
        this.f = f;
    }
    
    @Override
    public void run(){

        contador = new Contador(f);
        contador.start();

        Random r = new Random();
        int duracion_supervicion, duracion_lanzamiento;

        try {
            
            estado = "Durmiendo...";
            gui.set_estado_gerente( estado, f.n_fabrica);
            celulares_lanzados = 0;

            while( f.isActive() ){
                duracion_supervicion = r.nextInt(30,90);
                duracion_lanzamiento = r.nextInt(12, 18) * DURACION_HORA;

                Thread.sleep( DURACION_DIA - duracion_lanzamiento - (duracion_supervicion*DURACION_MINUTO) );
                estado = "Supervisando Jefe...";
                gui.set_estado_gerente( estado, f.n_fabrica);
                for (int i = 0; i < duracion_supervicion; i++) {
                    
                    Thread.sleep(DURACION_MINUTO);
                    if( f.getEstadoJefe() == 0 )
                        contador.penalizarJefe();

                }

                if( f.getDiasRestantes() == 0 )
                    estado = "Lanzamiento...";
                else
                    estado = "Durmiendo...";
                gui.set_estado_gerente( estado, f.n_fabrica);
                
                Thread.sleep(duracion_lanzamiento);

                if( "Lanzamiento...".equals(estado) ){
                    lanzamiento();
                }

            }


        } catch (Exception e) {
            //TODO: handle exception
        }

    }

    void detener() {
        contador.interrupt();
    }

}
