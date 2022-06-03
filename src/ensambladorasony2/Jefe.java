package ensambladorasony2;

import ensambladorasony2.Fabrica;
import static ensambladorasony2.Const.*;
import static ensambladorasony2.EnsambladoraSony2.gui;

import java.util.concurrent.Semaphore;

public class Jefe extends Thread{
    
    private final int
        DURACION_CAMBIO = 3*DURACION_HORA,
        DURACION_CLASH = 15*DURACION_MINUTO;

    private Fabrica f;

    private int dias_restantes = DIAS_PARA_LANZAMIENTO;
    private Semaphore sem_dia = new Semaphore(1);
    
    private int estado_actual = 0;

    public Jefe(Fabrica f){
        this.f = f;
    }

    public int getDiasRestantes(){
        int ans = -1;        
        
        try {
            
            sem_dia.acquire();
            ans = dias_restantes;
            sem_dia.release();
            
        } catch (Exception e) {
            //TODO: handle exception
        }

        return ans;

    }

    public int getEstado(){
        return estado_actual;
    }

    @Override
    public void run(){

        int breaks_in_day = (DURACION_DIA - DURACION_CAMBIO)/DURACION_CLASH;

        try {
            
            while( f.isActive() ){

                for (int i = 0; i < breaks_in_day; i++) {
                    estado_actual = i%2;
                    gui.set_estado_jefe( getEstadoJefe(estado_actual), f.n_fabrica);
                    Thread.sleep( DURACION_CLASH );                    
                }

                sem_dia.acquire();
                estado_actual = 2;
                Thread.sleep( DURACION_CAMBIO );
                dias_restantes = dias_restantes >= 1? dias_restantes -1 : DIAS_PARA_LANZAMIENTO;
                gui.set_dia(dias_restantes, f.n_fabrica);
                sem_dia.release();

            }

        } catch (Exception e) {
            //TODO: handle exception
        }


    }

}
