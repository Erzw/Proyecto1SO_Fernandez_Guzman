package ensambladorasony2;

// https://stackoverflow.com/questions/14584488/import-static-vs-static-final
import ensambladorasony2.Ensamblador;
import ensambladorasony2.Gerente;
import ensambladorasony2.Jefe;
import ensambladorasony2.Productor;
import static ensambladorasony2.Const.*;
import static ensambladorasony2.EnsambladoraSony2.gui;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fabrica extends Thread{

    private volatile boolean activa = true;

    private final int[] init_prod = {10, 10, 10, 10, 10};
    public final int n_fabrica;

    private final int[] ensamblaje = new int[4];
    private volatile int[] tiempo_produccion = new int[5];
    private volatile int[] almacen = {0,0,0,0};

    private final int precio_celular;
    public int getPrecioCelular() {
        return precio_celular;
    }

    private volatile int celulares = 0;
    private volatile Semaphore sem_celulares = new Semaphore(1);

    private volatile Semaphore[] sem_almacen = {
        new Semaphore(1),   //botones
        new Semaphore(1),   //camaras
        new Semaphore(1),   //pantallas
        new Semaphore(1)    //pines
    };
    
    private volatile Semaphore[] sem_capacidad ={
        new Semaphore(45),  //botones
        new Semaphore(20),  //camaras
        new Semaphore(40),  //pantallas
        new Semaphore(15)   //pines
    };

    private volatile Semaphore[] sem_disp ={
        new Semaphore(0),  //botones
        new Semaphore(0),  //camaras
        new Semaphore(0),  //pantallas
        new Semaphore(0)   //pines
    };

    private Jefe jefe;
    private Gerente gerente;

    private ArrayList< ArrayList<Productor> > productores = new ArrayList<>();
    private ArrayList< Ensamblador > ensambladores = new ArrayList<>();

//    public Fabrica(int n_fabrica, int terminalCI, int n_botones, int n_camaras, int n_pantallas, int precio, int p_botones, int p_camaras, int p_pantallas, int p_pines, int ensambladores ){
//
//        setInit( p_botones,  p_camaras,  p_pantallas,  p_pines,  ensambladores );
//        
//        this.n_fabrica = n_fabrica;
//        
//        this.precio_celular = precio;
//
//        for(int i = 0; i<5; i++)
//            init_prod[i] += terminalCI;
//
//        ensamblaje[BOTON] = n_botones;
//        ensamblaje[CAMARA] = n_camaras;
//        ensamblaje[PANTALLA] = n_pantallas;
//        ensamblaje[PIN] = 1;
//
//        if( terminalCI <= 3 ){
//            tiempo_produccion[PANTALLA] = DURACION_DIA;
//            tiempo_produccion[BOTON] = DURACION_DIA/4;
//            tiempo_produccion[PIN] = DURACION_DIA*2;
//        }else if( terminalCI <=6 ){
//            tiempo_produccion[PANTALLA] = DURACION_DIA/2;
//            tiempo_produccion[BOTON] = DURACION_DIA/2;
//            tiempo_produccion[PIN] = DURACION_DIA*3;
//        }else{
//            tiempo_produccion[PANTALLA] = DURACION_DIA/3;
//            tiempo_produccion[BOTON] = DURACION_DIA/3;
//            tiempo_produccion[PIN] = DURACION_DIA;
//        }
//
//        if( terminalCI <= 5 )
//            tiempo_produccion[CAMARA] = DURACION_DIA*2;
//        else
//            tiempo_produccion[CAMARA] = DURACION_DIA*3;
//        
//        tiempo_produccion[ENSAMBLADOR] = 2*DURACION_DIA;        
//        
//        
//        
//    }
    
    public Fabrica(int n_fabrica, int terminalCI, int n_botones, int n_camaras, int n_pantallas, int precio ){

        this.n_fabrica = n_fabrica;
        
        this.precio_celular = precio;

        for(int i = 0; i<5; i++)
            init_prod[i] += terminalCI;

        ensamblaje[BOTON] = n_botones;
        ensamblaje[CAMARA] = n_camaras;
        ensamblaje[PANTALLA] = n_pantallas;
        ensamblaje[PIN] = 1;

        if( terminalCI <= 3 ){
            tiempo_produccion[PANTALLA] = DURACION_DIA;
            tiempo_produccion[BOTON] = DURACION_DIA/4;
            tiempo_produccion[PIN] = DURACION_DIA*2;
        }else if( terminalCI <=6 ){
            tiempo_produccion[PANTALLA] = DURACION_DIA/2;
            tiempo_produccion[BOTON] = DURACION_DIA/2;
            tiempo_produccion[PIN] = DURACION_DIA*3;
        }else{
            tiempo_produccion[PANTALLA] = DURACION_DIA/3;
            tiempo_produccion[BOTON] = DURACION_DIA/3;
            tiempo_produccion[PIN] = DURACION_DIA;
        }

        if( terminalCI <= 5 )
            tiempo_produccion[CAMARA] = DURACION_DIA*2;
        else
            tiempo_produccion[CAMARA] = DURACION_DIA*3;
        
        tiempo_produccion[ENSAMBLADOR] = 2*DURACION_DIA;        
        
        
        
    }

    public void run(){
        
        productores.clear();
        ensambladores.clear();

        jefe = new Jefe(this);
        jefe.start();

        gerente = new Gerente(this);
        gerente.start();

        for(int i = 0; i<init_prod[ENSAMBLADOR]; i++){
            ensambladores.add( new Ensamblador(this) );
            ensambladores.get(i).start();
        }    
        
        for( int i = 0; i<4; i++ ){
            productores.add( new ArrayList<>() );
            for(int j = 0; j<init_prod[i]; j++){
                productores.get(i).add( new Productor(i, this) );
                productores.get(i).get(j).start();
            }
        }
        
        gui.set_prod_botones( productores.get(BOTON).size() , n_fabrica);
        gui.set_prod_cam( productores.get(CAMARA).size() , n_fabrica);
        gui.set_prod_pantallas( productores.get(PANTALLA).size() , n_fabrica);
        gui.set_prod_pin( productores.get(PIN).size() , n_fabrica);
        
        gui.set_ensambladores( ensambladores.size() , n_fabrica);
        
    }}
   
