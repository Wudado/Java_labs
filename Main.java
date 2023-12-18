package bsu.rfe.java.group5.Lab3_Barsukevich.var4B;


import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main extends JFrame {
    // ��������� � �������� �������� ���� ����������
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    // ������ ����������� ���� ��� ������ ������
    // ��������� �� ��������� ����������, �.�. ����� � �� ������������
    // ������������ ���� ��� �� ���������� ��������� ������ � ����
    private JFileChooser fileChooser = null;
    // �������� ���� �������� � ���� ������ ������, ��� ��� ��� ����������
    // �������������� �� ������ ����
    private final JMenuItem saveToTextMenuItem;
    private JMenuItem searchValueMenuItem;
    private final JMenuItem infoMenuItem;

    private JCheckBoxMenuItem showColumnMenuItem;

    // ���� ����� ��� ���������� �������� ����������
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;

    // ������������ ����� �������
    private FunctionTableCellRenderer renderer = new FunctionTableCellRenderer();

    // ������ ������ � ������������ ����������
    private FunctionTableModel data;

    private JTable table;
    private TableColumn bool_column;
    private Double param = -1.0;

    public Main(){
        // ������������ ����� ������������ ������
        super("������������� ������� �� �������");
        // ���������� ������� ����
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        // �������������� ���� ���������� �� ������
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);

        // ������� ����
        JMenuBar menuBar = new JMenuBar();
        // ���������� ���� � �������� �������� ���� ����������
        setJMenuBar(menuBar);
        // �������� � ���� ����� ���� "����"
        JMenu fileMenu = new JMenu("����");
        // �������� ��� � ������� ����
        menuBar.add(fileMenu);
        // ������� ����� ���� "�������"
        JMenu tableMenu = new JMenu("�������");
        // �������� ��� � ������� ����
        menuBar.add(tableMenu);
        // ������� ����� ���� "�������"
        JMenu infoMenu = new JMenu("�������");
        menuBar.add(infoMenu);
        // ������� ����� "��������" �� ���������� � ��������� ����
        Action saveToTextAction = new AbstractAction( "��������� � ��������� ����") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    // ���� ��������� ����������� ���� "������� ����" ��? �� ������,
                    // �� ������� ���
                    fileChooser = new JFileChooser();
                    // � ���������������� ������� �����������
                    fileChooser.setCurrentDirectory(new File("."));
                }
                // �������� ���������� ����
                if (fileChooser.showSaveDialog(Main.this) == JFileChooser.APPROVE_OPTION){
                    // ���� ��������� ��� ������ ��������,
                    // ��������� ������ � ��������� ����
                    saveToTextFile(fileChooser.getSelectedFile());
                }
            }
        };
        // �������� ��������������� ����� ������� � ���� "����"
        saveToTextMenuItem = fileMenu.add(saveToTextAction);

        fileMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                // �� ��������� ����� ���� �������� ����������� (������ ��� ���)
                if (data == null) saveToTextMenuItem.setEnabled(false);
                else saveToTextMenuItem.setEnabled(true);
            }
            @Override
            public void menuDeselected(MenuEvent e) { }
            @Override
            public void menuCanceled(MenuEvent e) { }
        });
        // ������� ����� �������� �� ������ �������� ����������
        Action searchValueAction = new AbstractAction("����� �������� �������") {
            public void actionPerformed(ActionEvent event) {
                // ��������� ������������ ������ ������� ������
                String value = JOptionPane.showInputDialog(Main.this, "������� �������� ��� ������", "����� ��������", JOptionPane.QUESTION_MESSAGE);
                // ���������� ��������� �������� � �������� ������
                renderer.setNeedle(value);
                // �������� �������
                getContentPane().repaint();
            }
        };
        // �������� �������� � ���� "�������"
        searchValueMenuItem = tableMenu.add(searchValueAction);
        // �� ��������� ����� ���� �������� ����������� (������ ��? ���)
        tableMenu.add(new JSeparator());
        showColumnMenuItem = new JCheckBoxMenuItem("�������� ������ �������", true);
        tableMenu.add(showColumnMenuItem);
        showColumnMenuItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == 2) {
                    bool_column = table.getColumnModel().getColumn(2);
                    table.removeColumn(bool_column);
                }if(e.getStateChange() == 1){
                    table.addColumn(bool_column);
                }
            }
        });

        Action aboutProgrammAction = new AbstractAction("� ���������") {
            public void actionPerformed(ActionEvent event) {
                Info info1 = new Info();
                JLabel info = new JLabel(info1.toString());
                info.setHorizontalTextPosition(JLabel.CENTER);
                info.setVerticalTextPosition(JLabel.BOTTOM);
                info.setIconTextGap(10);
                JOptionPane.showMessageDialog(Main.this, info, "� ���������", JOptionPane.PLAIN_MESSAGE);
            }
        };
        infoMenuItem = infoMenu.add(aboutProgrammAction);
        // ������� ��������� ���� ��� ����� �������� ������ � 10 ��������
        // �� ��������� �� ��������� 0.0
        textFieldFrom = new JTextField("0.0", 10);
        // ������������� ���������� ������� ���� �����
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
        // ������� ��������� 1 ���� "������� � �������������� ��������"
        Box hboxXRange = Box.createHorizontalBox();
        // ������ ��� ���������� ��� ����� "���?����"
        hboxXRange.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "���������:"));
        hboxXRange.add(Box.createHorizontalGlue());
        hboxXRange.add(new JLabel("X ���������� �� ��������� ��:"));
        hboxXRange.add(Box.createHorizontalStrut(10));
        hboxXRange.add(textFieldFrom);
        hboxXRange.add(Box.createHorizontalStrut(20));
        hboxXRange.add(new JLabel("��:"));
        hboxXRange.add(Box.createHorizontalStrut(10));
        hboxXRange.add(textFieldTo);
        hboxXRange.add(Box.createHorizontalStrut(20));
        hboxXRange.add(new JLabel("� �����:"));
        hboxXRange.add(Box.createHorizontalStrut(10));
        hboxXRange.add(textFieldStep);
        hboxXRange.add(Box.createHorizontalStrut(20));
        hboxXRange.add(Box.createHorizontalGlue());
        // ���������� ���������������� ������ ������� ������ ����������
        // ������������, ����� ��� ���������� ������� ������ �� �������
        hboxXRange.setPreferredSize(new Dimension((int)(hboxXRange.getMaximumSize().getWidth()),
                (int)(hboxXRange.getMinimumSize().getHeight()*1.5)));
        // ���������� ������� � ������� (��������) ����� ����������
        getContentPane().add(hboxXRange, BorderLayout.NORTH);
        // ������� ������ "���������"
        JButton buttonCalc = new JButton("���������");
        // ������ �������� �� ������� "���������" � ��������� � ������
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    // ������� �������� ������ � ����� �������, ����
                    showColumnMenuItem.setState(true);
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step = Double.parseDouble(textFieldStep.getText());
                    // �� ������ ��������� ������ ������� ����� ��������� ������ �������
                    data = new FunctionTableModel(-10d, 10d, 0.000001, param);
                    // ������� ����� ��������� �������
                    table = new JTable(data);
                    // ���������� � �������� ������������� ����� ��� ������ Double ������������� ������������
                    table.setDefaultRenderer(Double.class, renderer);
                    // ���������� ������ ������ ������� � 30 ��������
                    table.setRowHeight(30);
                    // ������� ��� ��������� �������� �� ���������� hBoxResult
                    hBoxResult.removeAll();
                    // �������� � hBoxResult �������, "��������" � ������ � �������� ���������
                    hBoxResult.add(new JScrollPane(table));
                    // �������� ������� ���������� �������� ����
                    hBoxResult.revalidate();

                }
                // � ������ ������ �������������� ����� �������� ��������� �� ������
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Main.this,
                            "������ � ������� ������ ����� � ��������� ������",
                            "��������� ������ �����", JOptionPane.WARNING_MESSAGE);
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
                // ������� ��� ��������� �������� ���������� hBoxResult
                hBoxResult.removeAll();
                // �������� � ��������� ������ ������
                hBoxResult.repaint();
                data = null;
            }
        });
        // ��������� ��������� ������ � ���������
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createEtchedBorder());
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());

        // ���������� ���������������� ������ ������� ������ ���������� ������������, ����� ���
        // ���������� ���� ������� ������ �� �������
        hboxButtons.setPreferredSize(new Dimension((int)(hboxButtons.getMaximumSize().getWidth()),
                (int)(hboxButtons.getMinimumSize().getHeight() * 2)));
        // ���������� ��������� � �������� � ������ (�����) ������� ��������� ����������
        getContentPane().add(hboxButtons, BorderLayout.SOUTH);
        // ������� ��� ������ ���������� ���� ��� ������
        hBoxResult = Box.createHorizontalBox();
        // ���������� ��������� hBoxResult � ������� (�����������) ������� ��������� ����������
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }

    protected void saveToTextFile(File selectedFile) {// �������  ���������� ����� ������, ������������ � ��������� ����
        try{

            PrintStream out = new PrintStream(selectedFile); // �������� � ����� ������ ������������ ��������
            out.println("���������� ������������� �������:");
            out.println("�������� �� " + data.getFrom() + " �� " + data.getTo()+ " � ����� " +
                    data.getStep() + " � ���������� " + data.getParameter());

            for (int i = 0; i < data.getRowCount(); i++)// �������� � ����� ������ �������� � ������
            {
                out.println("�������� � ����� " + data.getValueAt(i,0)  + " ����� " + data.getValueAt(i,1));
            }
            out.close();// ������� ����� ������
        } catch (FileNotFoundException e){
            // �������������� �������� "������������" � ������ ������ ����� �� ������������,
            // ��� ��� �� ���� ������, � �� ��������� ��� ������
        }
    }

    public static void main(String[] args){ // ������� ��������� �������� ����, ������� ��� ������������
        Main frame = new Main();
        // ������ ��������, ����������� ��� �������� ����
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}