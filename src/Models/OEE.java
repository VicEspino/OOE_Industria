package Models;

public class OEE {

    private final double tiempoPorTurno;//tiempo total
    private final double tiempoParadas;
    private final double tiempoAislamiento;
    private final double tiempoCambios;
    private final double tiempoEsperas;
    private final double tiempoEstandarFabricacion;
    private final double produccionReal;
    private final double numeroUnidadesDefectuosas;
    private final double numerUnidadesRemanufacturadas;
    private final double tiempoPlaneado;


    public OEE(double tiempoPorTurno, double tiempoParadas,double tiempoPlaneado, double tiempoAislamiento, double tiempoCambios, double tiempoEsperas, double tiempoEstandarFabricacion, double produccionReal, double numeroUnidadesDefectuosas, double numerUnidadesRemanufacturadas) {
        this.tiempoPorTurno = tiempoPorTurno;
        this.tiempoParadas = tiempoParadas;
        this.tiempoPlaneado = tiempoPlaneado;
        this.tiempoAislamiento = tiempoAislamiento;
        this.tiempoCambios = tiempoCambios;
        this.tiempoEsperas = tiempoEsperas;
        this.tiempoEstandarFabricacion = tiempoEstandarFabricacion;
        this.produccionReal = produccionReal;
        this.numeroUnidadesDefectuosas = numeroUnidadesDefectuosas;
        this.numerUnidadesRemanufacturadas = numerUnidadesRemanufacturadas;
    }
//Disponibilidad

    private double getTiempoDisponible(){
        return tiempoPorTurno-tiempoPlaneado;
    }

    private double getTiempoMuerto(){
        return tiempoParadas + tiempoAislamiento + tiempoCambios + tiempoEsperas;
    }

    private double getTiempoProductivo(){
        return getTiempoDisponible()-getTiempoMuerto();
    }

    public double getDisponibilidad(){
        return getTiempoProductivo()/getTiempoDisponible();
    }
//Fin disponibilidad.
//Eficiencia
    private double getCapacidadProductiva(){
        return tiempoEstandarFabricacion*tiempoPorTurno;
    }

    public double getEficiencia(){
        return produccionReal/getCapacidadProductiva();
    }

//Fin eficiencia

//Calidad

    public double getPiezasBuenas(){
        return produccionReal - numeroUnidadesDefectuosas- numerUnidadesRemanufacturadas;
    }

    public double getCalidad(){
        return  getPiezasBuenas()/produccionReal;
    }
//Fin Calidad

    public double getOEE(){
        return ((getDisponibilidad()) * (getEficiencia()) * (getCalidad()));
    }

}
