package THREADS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.ImageIcon;

public class CarreraCaballos extends JFrame {
    private JProgressBar[] progressBars;
    private JLabel[] caballoLabels;
    private JButton btnIniciar;
    private Timer animationTimer;
    private Thread[] caballos;
    private boolean carreraEnCurso = false;
    private boolean ganadorEncontrado = false;
    private int ganador = -1;
    private boolean isHorse2 = false;

    public CarreraCaballos() {
        setTitle("Carrera de Caballos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        progressBars = new JProgressBar[4];
        caballoLabels = new JLabel[4];
        caballos = new Thread[4];

        for (int i = 0; i < 4; i++) {
            progressBars[i] = new JProgressBar(0, 100);
            progressBars[i].setValue(0);
            progressBars[i].setStringPainted(true);
            caballoLabels[i] = new JLabel(createImageIcon("horse3.png"));
        }

        btnIniciar = new JButton("Iniciar Carrera");
        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!carreraEnCurso) {
                    iniciarCarrera();
                }
            }
        });

        animationTimer = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (carreraEnCurso) {
                    alternarAnimacion();
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(5, 1));
        for (int i = 0; i < 4; i++) {
            panel.add(new JLabel("Caballo " + (i + 1)));
            panel.add(caballoLabels[i]);
            panel.add(progressBars[i]);
        }
        panel.add(btnIniciar);

        add(panel);
    }

    private void iniciarCarrera() {
        carreraEnCurso = true;
        ganadorEncontrado = false;
        isHorse2 = false;
        animationTimer.start();

        for (int i = 0; i < 4; i++) {
            progressBars[i].setValue(0);
            caballos[i] = new Thread(new Caballo(i));
            caballos[i].start();
        }
    }

    class Caballo implements Runnable {
        private int caballoId;

        public Caballo(int caballoId) {
            this.caballoId = caballoId;
        }

        @Override
        public void run() {
            Random rand = new Random();
            while (progressBars[caballoId].getValue() < 100 && !ganadorEncontrado) {
                int avance = rand.nextInt(15) + 1;
                int newValue = progressBars[caballoId].getValue() + avance;
                if (newValue >= 100) {
                    newValue = 100;
                    ganadorEncontrado = true;
                    ganador = caballoId;
                    mostrarGanador();
                }
                progressBars[caballoId].setValue(newValue);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private void mostrarGanador() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, "¡Caballo " + (ganador + 1) + " ha ganado!");
                reiniciarCarrera();
            }
        });
    }

    private void reiniciarCarrera() {
        carreraEnCurso = false;
        ganador = -1;
        animationTimer.stop();

        for (int i = 0; i < 4; i++) {
            caballos[i].interrupt();
            caballoLabels[i].setIcon(createImageIcon("horse3.png"));
        }
    }

    private void alternarAnimacion() {
        if (isHorse2) {
            for (int i = 0; i < 4; i++) {
                caballoLabels[i].setIcon(createImageIcon("horse3.png"));
            }
            isHorse2 = false;
        } else {
            for (int i = 0; i < 4; i++) {
                caballoLabels[i].setIcon(createImageIcon("horse2.png"));
            }
            isHorse2 = true;
        }
    }

    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("No se puede encontrar el archivo de imagen: " + path);
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CarreraCaballos carrera = new CarreraCaballos();
                carrera.setVisible(true);
            }
        });
    }
}
