package THREADS;

import java.util.concurrent.Semaphore;

public class ControlAccesoBanoPublico {
    private Semaphore semaforoHombres;
    private Semaphore semaforoMujeres;

    public ControlAccesoBanoPublico() {
        semaforoHombres = new Semaphore(1);
        semaforoMujeres = new Semaphore(1);
    }

    public void usarBano(String genero) {
        try {
            if (genero.equals("hombre")) {
                semaforoHombres.acquire();
                System.out.println("Hombre está usando el baño.");
                Thread.sleep(2000);
                System.out.println("Hombre ha terminado de usar el baño.");
                semaforoHombres.release();
            } else if (genero.equals("mujer")) {
                semaforoMujeres.acquire();
                System.out.println("Mujer está usando el baño.");
                Thread.sleep(2000);
                System.out.println("Mujer ha terminado de usar el baño.");
                semaforoMujeres.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ControlAccesoBanoPublico controlAcceso = new ControlAccesoBanoPublico();
        Thread hombre1 = new Thread(() -> controlAcceso.usarBano("hombre"));
        Thread mujer1 = new Thread(() -> controlAcceso.usarBano("mujer"));
        Thread hombre2 = new Thread(() -> controlAcceso.usarBano("hombre"));
        Thread mujer2 = new Thread(() -> controlAcceso.usarBano("mujer"));

        hombre1.start();
        hombre2.start();
    }
}

