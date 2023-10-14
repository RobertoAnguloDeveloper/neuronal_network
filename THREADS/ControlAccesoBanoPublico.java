package THREADS;

import java.util.concurrent.Semaphore;

public class ControlAccesoBanoPublico {
    private Semaphore semaforoHombres;
    private Semaphore semaforoMujeres;

    public ControlAccesoBanoPublico() {
        // Inicializamos los semáforos con una cantidad de permisos igual a 1,
        // lo que significa que solo un hilo puede adquirir el permiso a la vez.
        semaforoHombres = new Semaphore(1);
        semaforoMujeres = new Semaphore(1);
    }

    public void usarBano(String genero) {
        try {
            if (genero.equals("hombre")) {
                semaforoHombres.acquire(); // Un hombre adquiere el permiso para usar el baño de hombres.
                System.out.println("Hombre está usando el baño.");
                Thread.sleep(2000); // Simulamos el tiempo que un hombre pasa en el baño.
                System.out.println("Hombre ha terminado de usar el baño.");
                semaforoHombres.release(); // El hombre libera el permiso.
            } else if (genero.equals("mujer")) {
                semaforoMujeres.acquire(); // Una mujer adquiere el permiso para usar el baño de mujeres.
                System.out.println("Mujer está usando el baño.");
                Thread.sleep(2000); // Simulamos el tiempo que una mujer pasa en el baño.
                System.out.println("Mujer ha terminado de usar el baño.");
                semaforoMujeres.release(); // La mujer libera el permiso.
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ControlAccesoBanoPublico controlAcceso = new ControlAccesoBanoPublico();

        // Creamos varios hilos para simular múltiples usuarios que intentan usar el baño de manera concurrente.
        Thread hombre1 = new Thread(() -> controlAcceso.usarBano("hombre"));
        Thread mujer1 = new Thread(() -> controlAcceso.usarBano("mujer"));
        Thread hombre2 = new Thread(() -> controlAcceso.usarBano("hombre"));
        Thread mujer2 = new Thread(() -> controlAcceso.usarBano("mujer"));

        hombre1.start();
        hombre2.start();
    }
}

