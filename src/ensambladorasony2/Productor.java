package ensambladorasony2;

import ensambladorasony2.Fabrica;

public class Productor extends Thread{

    private final int tipo_producto;
    private final Fabrica f;

    public Productor(int tipo_producto, Fabrica f){
        this.f = f;
        this.tipo_producto = tipo_producto;
    }

    @Override
    public void run(){
        try {
            
            while( f.isActive() ){
                Thread.sleep( f.getTiempoProduccion(tipo_producto) );
                f.almacenar(tipo_producto);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
