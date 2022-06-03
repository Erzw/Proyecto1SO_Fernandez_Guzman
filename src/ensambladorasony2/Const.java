package ensambladorasony2;

public class Const {

    public static final int
        BOTON       = 0,
        CAMARA      = 1,
        PANTALLA    = 2,
        PIN         = 3,
        ENSAMBLADOR = 4,
        JEFE        = 5,
        GERENTE     = 6;
    
    public static final int DIAS_PARA_LANZAMIENTO = 5;

    private static final int[] salario = {4, 5, 3, 2, 6, 7, 180};
    private static final String[] estados_jefe = {
        "Jugando Clash Royale...",
        "Revisando Papeles...",
        "Actualizando Dia..."
    };

    public static String getEstadoJefe(int estado){
        return estados_jefe[estado];
    }

    public static int getSalario(int i){
        return salario[i];
    }

    public static final int
        DURACION_MINUTO = 1,
        DURACION_HORA = DURACION_MINUTO*60,
        DURACION_DIA = DURACION_HORA*24;
    
}
