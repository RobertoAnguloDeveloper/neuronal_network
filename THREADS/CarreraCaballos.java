import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class CarreraCaballos extends JFrame {
    private JProgressBar[] progressBars;
    private JLabel[] caballoLabels;
    private JButton btnIniciar;
    private Timer animationTimer;
    private Thread[] caballos;
    private boolean carreraEnCurso = false;
    private int ganador = -1;
    private int[] caballoImages;
    private String[] imagePaths = {"horse1.png", "horse2.png", "horse3.png", "horse4.png"};

    public CarreraCaballos() {
        setTitle("Carrera de Caballos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        progressBars = new JProgressBar[4];
        caballoLabels = new JLabel[4];
        caballos = new Thread[4];
        caballoImages = new int[4];

        for (int i = 0; i < 4; i++) {
            progressBars[i] = new JProgressBar(0, 100);
            progressBars[i].setValue(0);
            progressBars[i].setStringPainted(true);
            caballoLabels[i] = new JLabel(createImageIcon(imagePaths[i]));
            caballoImages[i] = i;
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
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        add(panel);
    }

    private void iniciarCarrera() {
        carreraEnCurso = true;
        ganador = -1;
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
            while (progressBars[caballoId].getValue() < 100 && ganador == -1) {
                int avance = rand.nextInt(15) + 1;
                int newValue = progressBars[caballoId].getValue() + avance;
                if (newValue >= 100) {
                    newValue = 100;
                    ganador = caballoId;
                    animationTimer.stop();
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

        for (int i = 0; i < 4; i++) {
            caballos[i].interrupt();
            caballoLabels[i].setIcon(createImageIcon(imagePaths[i]));
        }
    }

    private void alternarAnimacion() {
        for (int i = 0; i < 4; i++) {
            if (caballoImages[i] == i) {
                caballoLabels[i].setIcon(createImageIcon("horsemove.png"));
                caballoImages[i] = 4;
            } else {
                caballoLabels[i].setIcon(createImageIcon(imagePaths[i]));
                caballoImages[i] = i;
            }
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