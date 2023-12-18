package bsu.rfe.java.group5.Lab3_Barsukevich.var4B.jlen;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;


public class MainFrame extends JFrame {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private final Double[] coefficients;
    private JFileChooser fileChooser = null;

    private final JMenuItem saveToTextMenuItem;
    private final JMenuItem saveToGraphicsMenuItem;
    private final JMenuItem searchValueMenuItem;
    private final JMenuItem searchRangeMenuItem;
    private final JMenuItem aboutMenuItem;
    private final JMenuItem saveMenuItem;
    private final JTextField textFieldFrom;
    private final JTextField textFieldTo;
    private final JTextField textFieldStep;
    private final Box hBoxResult;
    private final GornerTableCellRenderer renderer = new
            GornerTableCellRenderer();
    private GornerTableModel data;

    private static final String F_NAME_PHOTO = "/mnt/126AE12C6AE10D73/dev/java/labs_2nd_course/extended_gui/src/main/resources/kermit.jpg";

    public MainFrame(Double[] coefficients) {
        super("������������� ���������� �� ������� �� ����� �������");
        this.coefficients = coefficients;
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("����");
        menuBar.add(fileMenu);
        JMenu tableMenu = new JMenu("�������");
        menuBar.add(tableMenu);
        JMenu aboutMenu = new JMenu("About");
        menuBar.add(aboutMenu);

        Action saveAction = new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) ==
                        JFileChooser.APPROVE_OPTION) {
                    new Thread(() ->
                            data.saveTo(fileChooser.getSelectedFile())
                    ).start();
                }
            }
        };

        Action aboutAction = new AbstractAction("About") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame aboutWindow = new JFrame("About");
                aboutWindow.setSize(400, 400);
                aboutWindow.setLocation(
                    getToolkit().getScreenSize().width / 2,
                    getToolkit().getScreenSize().height / 2
                );
                aboutWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                aboutWindow.setLayout(new BorderLayout());
                aboutWindow.setAlwaysOnTop(true);
                aboutWindow.setResizable(false);

                JLabel name = new JLabel();
                name.setText("This program was copied " +
                        "and changed by Dmitry Dubina");

                name.setSize(400, 50);
                name.setLocation(0, 0);

                JLabel group = new JLabel();
                group.setText("5 group");

                group.setSize(100, 20);
                group.setLocation(175, 350);


                JLabel image = new JLabel();
                image.setIcon(new ImageIcon(readImage()));

                image.setSize(200, 200);
                image.setLocation(100, 50);

                aboutWindow.add(name);
                aboutWindow.add(image);
                aboutWindow.add(group);
                aboutWindow.add(new JLabel());

                aboutWindow.setVisible(true);
            }
        };

        Action saveToTextAction = new AbstractAction("��������� � ��������� ����") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) ==
                        JFileChooser.APPROVE_OPTION) {
                    saveToTextFile(fileChooser.getSelectedFile());
                }
            }
        };
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        saveToTextMenuItem.setEnabled(false);
        Action saveToGraphicsAction = new AbstractAction("��������� ������ ��� ���������� �������") {

            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) ==
                        JFileChooser.APPROVE_OPTION) {
                    saveToGraphicsFile(
                            fileChooser.getSelectedFile());
                }
            }
        };
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
// �� ��������� ����� ���� �������� �����������(������ ��? ���)
        saveToGraphicsMenuItem.setEnabled(false);
// ������� ����� �������� �� ������ �������� ����������
        Action searchValueAction = new AbstractAction("����� �������� ����������") {
            public void actionPerformed(ActionEvent event) {
// ��������� ������������ ������ ������� ������
                String value =
                        JOptionPane.showInputDialog(MainFrame.this, "������� �������� ��� ������",
                                "����� ��������", JOptionPane.QUESTION_MESSAGE);
// ���������� ��������� �������� � �������� ������
                renderer.setNeedle(value);
// �������� �������
                getContentPane().repaint();
            }
        };

        Action searchRangeAction = new AbstractAction("����� �������� ���������� � ���������") {
            public void actionPerformed(ActionEvent event) {
// ��������� ������������ ������ ������� ������
                String value =
                        JOptionPane.showInputDialog(
                                MainFrame.this,
                                "������� �������� ��� ������ � ������� (from to)",
                                "����� ��������",
                                JOptionPane.QUESTION_MESSAGE
                        );
// ���������� ��������� �������� � �������� ������
                String[] values = value.split(" ");
                String start = values[0];
                String end = values[1];

                renderer.setNeedleRange(start, end);
// �������� �������
                getContentPane().repaint();
            }
        };

        saveMenuItem = fileMenu.add(saveAction);
        saveMenuItem.setEnabled(false);

        aboutMenuItem = aboutMenu.add(aboutAction);
        aboutMenuItem.setEnabled(true);

// �������� �������� � ���� "�������"
        searchValueMenuItem = tableMenu.add(searchValueAction);
// �� ��������� ����� ���� �������� ����������� (������ ��? ���)
        searchValueMenuItem.setEnabled(false);

        searchRangeMenuItem = tableMenu.add(searchRangeAction);
        searchRangeMenuItem.setEnabled(false);
// ������� ������� � ������ ����� ��� ������ ������� � ����
// ������� ������� ��� ����� ����� ������� �������
        JLabel labelForFrom = new JLabel("X ���������� �� ��������� ��:");
// ������� ��������� ���� ��� ����� �������� ������ � 10 ��������
// �� ��������� �� ��������� 0.0
        textFieldFrom = new JTextField("0.0", 10);
// ���������� ������������ ������ ������ �����������������, �����
// ������������� ���������� ������� ���� �����
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
// ������� ������� ��� ����� ����� ������� �������
        JLabel labelForTo = new JLabel("��:");
// ������� ��������� ���� ��� ����� �������� ������ � 10 ��������
// �� ��������� �� ��������� 1.0
        textFieldTo = new JTextField("1.0", 10);
// ���������� ������������ ������ ������ �����������������, �����
// ������������� ���������� ������� ���� �����
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
// ������� ������� ��� ����� ���� �������������
        JLabel labelForStep = new JLabel("� �����:");
// ������� ��������� ���� ��� ����� �������� ������ � 10 ��������
// �� ��������� �� ��������� 1.0
        textFieldStep = new JTextField("0.1", 10);
// ���������� ������������ ������ ������ �����������������, �����
// ������������� ���������� ������� ���� �����
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
// ������� ��������� 1 ���� "������� � �������������� ��������"
        Box hboxRange = Box.createHorizontalBox();
// ������ ��� ���������� ��� ����� "���?����"
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
// �������� "����" C1-H1
        hboxRange.add(Box.createHorizontalGlue());
// �������� ������� "��"
        hboxRange.add(labelForFrom);
// �������� "��������" C1-H2
        hboxRange.add(Box.createHorizontalStrut(10));
// �������� ���� ����� "��"
        hboxRange.add(textFieldFrom);
// �������� "��������" C1-H3
        hboxRange.add(Box.createHorizontalStrut(20));
// �������� ������� "��"
        hboxRange.add(labelForTo);
// �������� "��������" C1-H4
        hboxRange.add(Box.createHorizontalStrut(10));
// �������� ���� ����� "��"
        hboxRange.add(textFieldTo);
// �������� "��������" C1-H5
        hboxRange.add(Box.createHorizontalStrut(20));
// �������� ������� "� �����"
        hboxRange.add(labelForStep);
// �������� "��������" C1-H6
        hboxRange.add(Box.createHorizontalStrut(10));
// �������� ���� ��� ����� ���� �������������
        hboxRange.add(textFieldStep);
// �������� "����" C1-H7
        hboxRange.add(Box.createHorizontalGlue());
// ���������� ���������������� ������ ������� ������ ����������
// ������������, ����� ��� ���������� ������� ������ �� �������
        hboxRange.setPreferredSize(new Dimension(
                Double.valueOf(hboxRange.getMaximumSize().getWidth())
                        .intValue(),
                Double.valueOf(hboxRange.getMinimumSize().getHeight())
                        .intValue() * 2));
// ���������� ������� � ������� (��������) ����� ����������
        getContentPane().add(hboxRange, BorderLayout.NORTH);
// ������� ������ "���������"
        JButton buttonCalc = new JButton("���������");
// ������ �������� �� ������� "���������" � ��������� � ������
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
// ������� �������� ������ � ����� �������, ����
                    Double from =
                            Double.parseDouble(textFieldFrom.getText());
                    Double to =
                            Double.parseDouble(textFieldTo.getText());
                    Double step =
                            Double.parseDouble(textFieldStep.getText());
// �� ������ ��������� ������ ������� ����� ��������� ������ �������
                    data = new GornerTableModel(from, to, step, coefficients);
// ������� ����� ��������� �������
                    JTable table = new JTable(data);
// ���������� � �������� ������������� ����� ���
// ������ Double ������������� ������������
                    table.setDefaultRenderer(Double.class,
                            renderer);
// ���������� ������ ������ ������� � 30
//        ��������
                    table.setRowHeight(30);
// ������� ��� ��������� �������� �� ����������
//        hBoxResult
                    hBoxResult.removeAll();
// �������� � hBoxResult �������, "��?������" �
//        ������ � �������� ���������
                    hBoxResult.add(new JScrollPane(table));
// �������� ������� ���������� �������� ����
                    getContentPane().validate();
// �������� ��� ��������� ���� ��� ���������
                    saveToTextMenuItem.setEnabled(true);
                    saveToGraphicsMenuItem.setEnabled(true);
                    searchValueMenuItem.setEnabled(true);
                    searchRangeMenuItem.setEnabled(true);
                    saveMenuItem.setEnabled(true);
                } catch (NumberFormatException ex) {
// � ������ ������ �������������� ����� ��������
//        ��������� �� ������
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "������ � ������� ������ ����� � ��������� ������", "��������� ������ �����",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
// ������� ������ "�������� ����"
        JButton buttonReset = new JButton("�������� ����");
// ������ �������� �� ������� "�������� ����" � ��������� � ������
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
// ���������� � ����� ����� �������� �� ���������
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
// ������� ��� ��������� �������� ����������
//        hBoxResult

                hBoxResult.removeAll();
// �������� � ��������� ������ ������
                hBoxResult.add(new JPanel());
// �������� �������� ���� ��� �����������
                saveToTextMenuItem.setEnabled(false);
                saveToGraphicsMenuItem.setEnabled(false);
                searchValueMenuItem.setEnabled(false);
                searchRangeMenuItem.setEnabled(false);
                saveMenuItem.setEnabled(false);
// �������� ������� ���������� �������� ����
                getContentPane().validate();
            }
        });
// ��������� ��������� ������ � ���������
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
// ���������� ���������������� ������ ������� ������ ����������
//        ������������,����� ���
// ���������� ���� ������� ������ �� �������
        hboxButtons.setPreferredSize(new Dimension(
                Double.valueOf(hboxButtons.getMaximumSize().getWidth()).intValue(),
                Double.valueOf(hboxButtons.getMinimumSize().getHeight()).intValue() * 2));
// ���������� ��������� � �������� � ������ (�����) �������
//        ��������� ����������
        getContentPane().add(hboxButtons, BorderLayout.SOUTH);
// ������� ��� ������ ���������� ���� ��� ������
        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());
// ���������� ��������� hBoxResult � ������� (�����������) �������
//        ��������� ����������
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }

    protected void saveToGraphicsFile(File selectedFile) {
        try {
// ������� ����� �������� ����� ������, ������������ �
//        ��������� ����
            DataOutputStream out = new DataOutputStream(new
                    FileOutputStream(selectedFile));
// �������� � ����� ������ ������� �������� X � �����,
//        �������� �/��������� � �����
            for (int i = 0; i < data.getRowCount(); i++) {
                out.writeDouble((Double) data.getValueAt(i, 0));
                out.writeDouble((Double) data.getValueAt(i, 1));
            }
// ������� ����� ������
            out.close();
        } catch (Exception e) {
// �������������� �������� "������������" � ������ ������
//        ����� �� ������������,
// ��� ��� �� ���� �����?�, � �� ��������� ��� ������
        }
    }

    protected void saveToTextFile(File selectedFile) {
        try {
// ������� ����� ���������� ����� ������, ������������ �
//        ��������� ����
            PrintStream out = new PrintStream(selectedFile);
// �������� � ����� ������ ������������ ��������

            out.println("���������� ������������� ���������� �� ����� �������");
            out.print("���������: ");
            for (int i = 0; i < coefficients.length; i++) {
                out.print(coefficients[i] + "*X^" +
                        (coefficients.length - i - 1));
                if (i != coefficients.length - 1)
                    out.print(" + ");
            }
            out.println();
            out.println("�������� �� " + data.getFrom() + " �� " +
                    data.getTo() + " � ����� " + data.getStep());
            out.println("====================================================");
// �������� � ����� ������ �������� � ������
            for (int i = 0; i < data.getRowCount(); i++) {
                out.println("�������� � ����� " + data.getValueAt(i, 0)
                        + " ����� " + data.getValueAt(i, 1));
            }
// ������� �����
            out.close();
        } catch (FileNotFoundException e) {
// �������������� �������� "������������" ����� ��
// ������������, ��� ��� �� ���� �����?�, � �� ���������
        }
    }

    public static void main(String[] args) {
// ���� �� ������ �� ������ ��������� ��������� ������ -
// ���������� ���������� ����������, ����������� ����������
        if (args.length == 0) {
            System.out.println("���������� ������������ ���������, ��� " +
                    "�������� �� ������ �� ������ ������������!");
            System.exit(-1);
        }
// ��������������� ����� � ������� ������������� �������, �������
//        ���������� ��������� ������
        Double[] coefficients = new Double[args.length];
        int i = 0;
        try {
// ��������� ���������, ������� ������������� �� � Double
            for (String arg : args) {
                coefficients[i++] = Double.parseDouble(arg);
            }
        } catch (NumberFormatException ex) {
// ���� �������������� ���������� - �������� �� ������ �
//        �����������
            System.out.println("������ �������������� ������ '" +
                    args[i] + "' � ����� ���� Double");
            System.exit(-2);
        }
// ������� ��������� �������� ����, ������� ��� ������������
        MainFrame frame = new MainFrame(coefficients);
// ������ ��������, ����������� ��� �������� ����
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private Image readImage() {
        try {

            return ImageIO.read(new File(F_NAME_PHOTO))
                    .getScaledInstance(
                            200,
                            200,
                            Image.SCALE_DEFAULT
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}