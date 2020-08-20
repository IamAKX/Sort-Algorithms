import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;

public class Main {
    private JPanel panel;
    private JButton selectionSortButton;
    private JButton bubbleSortButton;
    private JButton insertionSortButton;
    private JLabel heading;

    public Main() {
        bubbleSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Application.launch(BubbleSortHistogram.class);

            }
        });
        selectionSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Application.launch(SelectionSortHistogram.class);
            }
        });
        insertionSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Application.launch(InsertionSortHistogram.class);
            }
        });
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Sorting Algorithm");
        jFrame.setContentPane(new Main().panel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }


}
