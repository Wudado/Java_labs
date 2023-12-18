package bsu.rfe.java.group5.Lab2_Barsukevich.var12B;

import java.awt.*;
import javax.swing.*;

public class Window extends JFrame {
    private Double result = 0.0;
    private Double sum = 0.0;
    private final JButton buttonCalculate = new JButton("Calculate");
    private final JButton buttonClear = new JButton("Clear");
    private final JButton buttonMplus = new JButton("M+");
    private final JButton buttonMC = new JButton("MC");
    private final JRadioButton radioF1 = new JRadioButton("Function 1");
    private final JRadioButton radioF2 = new JRadioButton("Function 2");
    private final JRadioButton radioV1 = new JRadioButton("X");
    private final JRadioButton radioV2 = new JRadioButton("Y");
    private final JRadioButton radioV3 = new JRadioButton("Z");
    private final JTextField textFieldX = new JTextField("0", 10);
    private final JTextField textFieldY = new JTextField("0", 10);
    private final JTextField textFieldZ = new JTextField("0", 10);
    private final JLabel labelX = new JLabel("X:");
    private final JLabel labelY = new JLabel("Y:");
    private final JLabel labelZ = new JLabel("Z:");
    private final JLabel labelResult = new JLabel("Result: 0");

    public double calculate1(Double x, Double y, Double z) {
        return Math.pow(Math.cos(Math.exp(x)) + Math.log(Math.pow(1 + y, 2) + Math.sqrt(Math.exp(Math.cos(x)) + Math.pow(Math.sin(Math.PI * z), 2)) + Math.sqrt(1 / x) + Math.cos(Math.pow(y, 2))), Math.sin(z));
    }

    public double calculate2(Double x, Double y, Double z) {
        return (1 + Math.sqrt(z * x)) / (Math.pow(1 + Math.pow(x, 3), 1 / y));
    }

    public Double getResult() {
        return result;
    }
    public Double getSum() {
        return sum;
    }
    public JButton getButtonCalculate() {
        return buttonCalculate;
    }
    public JButton getButtonClear() {
        return buttonClear;
    }
    public JButton getButtonMPlus() {
        return buttonMplus;
    }
    public JButton getButtonMC() {
        return buttonMC;
    }
    public JRadioButton getRadioF1() {
        return radioF1;
    }
    public JRadioButton getRadioF2() {
        return radioF2;
    }
    public JTextField getTextFieldX() {
        return textFieldX;
    }
    public JTextField getTextFieldY() {
        return textFieldY;
    }
    public JTextField getTextFieldZ() {
        return textFieldZ;
    }
    public JLabel getLabelResult() {
        return labelResult;
    }
    public JRadioButton getRadioV1() {
        return radioV1;
    }
    public JRadioButton getRadioV2() {
        return radioV2;
    }
    public JRadioButton getRadioV3() {
        return radioV3;
    }

    public void setResult(Double result) {
        this.result = result;
    }



    public Window() {
        super("Calculator");
        this.setBounds(600, 300, 400, 225);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container container = this.getContentPane();
        container.setLayout(new BoxLayout(container, 1));
        ButtonGroup groupF = new ButtonGroup();
        groupF.add(this.radioF1);
        groupF.add(this.radioF2);
        JPanel panel1 = new JPanel();
        panel1.add(this.radioF1);
        this.radioF1.setSelected(true);
        panel1.add(this.radioF2);
        this.radioF1.addActionListener(new ButtonEventListener(this));
        this.radioF2.addActionListener(new ButtonEventListener(this));
        container.add(panel1);
        ButtonGroup groupV = new ButtonGroup();
        groupV.add(this.radioV1);
        groupV.add(this.radioV2);
        groupV.add(this.radioV3);
        JPanel panel2 = new JPanel();
        panel2.add(this.radioV1);
        this.radioV1.setSelected(true);
        panel2.add(this.radioV2);
        panel2.add(this.radioV3);
        this.radioV1.addActionListener(new ButtonEventListener(this));
        this.radioV2.addActionListener(new ButtonEventListener(this));
        this.radioV3.addActionListener(new ButtonEventListener(this));
        container.add(panel2);
        JPanel panel3 = new JPanel();
        panel3.add(this.labelX);
        panel3.add(this.textFieldX);
        panel3.add(this.labelY);
        panel3.add(this.textFieldY);
        panel3.add(this.labelZ);
        panel3.add(this.textFieldZ);
        container.add(panel3);
        JPanel panel4 = new JPanel();
        panel4.add(this.labelResult);
        container.add(panel4);
        JPanel panel5 = new JPanel();
        panel5.add(this.buttonCalculate);
        panel5.add(this.buttonClear);
        panel5.add(this.buttonMplus);
        panel5.add(this.buttonMC);
        container.add(panel5);
        this.buttonCalculate.addActionListener(new ButtonEventListener(this));
        this.buttonClear.addActionListener(new ButtonEventListener(this));
        this.buttonMplus.addActionListener(new ButtonEventListener(this));
        this.buttonMC.addActionListener(new ButtonEventListener(this));
    }
}

