package ensambladorasony2;

import ensambladorasony2.Fabrica;
import java.util.concurrent.Semaphore;
import static ensambladorasony2.Const.*;
import static ensambladorasony2.EnsambladoraSony2.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Contador extends Thread {
    
    private volatile int
        costo = 0,
        ingreso_bruto = 0,
        pago_jefe = 0,
        dias_transcurridos = 0,
        lanzamientos = 0;
    
    private volatile XYSeries record_costo = new XYSeries("Costo");
    private volatile XYSeries record_ingreso = new XYSeries("Ingreso");
    private volatile XYSeries record_ganancia = new XYSeries("Ganancia");
    
    private Semaphore sem_costo = new Semaphore(1);
    Fabrica f;

    public Contador(Fabrica f){
        this.f = f;
    }
    
    @Override
    public void run(){

        record_costo.add(0, 0);
        record_ingreso.add(0, 0);
        record_ganancia.add(0, 0);
        
        try {
            
            int horas = 0;

            while( f.isActive() ){
                
                gui.set_costo(costo, f.n_fabrica);
                gui.set_pago_jefe(pago_jefe, f.n_fabrica);
                
                Thread.sleep(DURACION_HORA);
                horas++;

                sem_costo.acquire();
                for(int i = 0; i<4; i++)
                    costo += f.getCantidadProductor(i)*getSalario(i);
                costo += f.getCantidadEnsambladores()*getSalario(ENSAMBLADOR);
                costo += getSalario(JEFE);
                pago_jefe += getSalario(JEFE);
                
                if( horas%24 == 0 ){
                    costo += getSalario(GERENTE);
                    dias_transcurridos++;
                }
                
                sem_costo.release();


            }

        } catch (Exception e) {
            //TODO: handle exception
        }

    }

    public void penalizarJefe(){
        try{
            sem_costo.acquire();
            costo -= 2;
            pago_jefe -= 2;
            gui.set_pago_jefe(pago_jefe, f.n_fabrica);
            sem_costo.release();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void lanzamiento(int n_celulares){

        try {
            lanzamientos++;
            sem_costo.acquire();
            int c = costo;
            sem_costo.release();

            ingreso_bruto += n_celulares*f.getPrecioCelular();
            int ganancia = ingreso_bruto - c;
            
            gui.set_ganancia(ganancia, f.n_fabrica);
            gui.set_ingreso_bruto(ingreso_bruto, f.n_fabrica);
            
            record_costo.add(lanzamientos, c );
            XYSeriesCollection dataset_costo = new XYSeriesCollection();
            dataset_costo.addSeries(record_costo);
            JFreeChart chart_costo = ChartFactory.createXYLineChart("Costos", "Lanzamiento", "Monto en $", dataset_costo);
            ChartPanel panel_costo = new ChartPanel( chart_costo );
            gui.get_plt_costo( f.n_fabrica ).setLayout( new java.awt.BorderLayout() );
            gui.get_plt_costo(f.n_fabrica).add(panel_costo);
            gui.get_plt_costo(f.n_fabrica).validate();

            record_ingreso.add(lanzamientos, ingreso_bruto);
            XYSeriesCollection dataset_ingreso = new XYSeriesCollection();
            dataset_ingreso.addSeries(record_ingreso);
            JFreeChart chart_ingreso = ChartFactory.createXYLineChart("Ingresos", "Lanzamiento", "Monto en $", dataset_ingreso);
            ChartPanel panel_ingreso = new ChartPanel( chart_ingreso );
            gui.get_plt_ingreso(f.n_fabrica).setLayout( new java.awt.BorderLayout() );
            gui.get_plt_ingreso(f.n_fabrica).add(panel_ingreso);
            gui.get_plt_ingreso(f.n_fabrica).validate();

            record_ganancia.add(lanzamientos, ganancia);
            XYSeriesCollection dataset_ganancia = new XYSeriesCollection();
            dataset_ganancia.addSeries(record_ganancia);
            JFreeChart chart_ganancia = ChartFactory.createXYLineChart("Ganancias", "Lanzamiento", "Monto en $", dataset_ganancia);
            ChartPanel panel_ganancia = new ChartPanel( chart_ganancia );
            gui.get_plt_ganancia(f.n_fabrica).setLayout( new java.awt.BorderLayout() );
            gui.get_plt_ganancia(f.n_fabrica).add(panel_ganancia);
            gui.get_plt_ganancia(f.n_fabrica).validate();

        } catch (Exception e) {
            //TODO: handle exception
        }

    }

}
