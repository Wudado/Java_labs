package bsu.rfe.java.group5.Lab2_Barsukevich.var12B;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ButtonEventListener implements ActionListener {
    Window this$0;
    public ButtonEventListener(final Window this$0) {
        this.this$0 = this$0;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == this.this$0.getButtonCalculate()) {
                Double x = Double.parseDouble(this.this$0.getTextFieldX().getText());
                Double y = Double.parseDouble(this.this$0.getTextFieldY().getText());
                Double z = Double.parseDouble(this.this$0.getTextFieldZ().getText());

                if (x < 0 || (x * z < 0)) {
                    JOptionPane.showMessageDialog(this.this$0, "Попытка вычисление корня из отрицательного значения", "Невозможно посчитать значение функции", 2);

                } else if (x == 0 || y == 0) {
                    JOptionPane.showMessageDialog(this.this$0, "Попытка деления на 0", "Невозможно посчитать значение функции", 2);
                } else {

                    if (this.this$0.getRadioF1().isSelected()) {
                        this.this$0.setResult(this.this$0.calculate1(x, y, z));
                    } else if (this.this$0.getRadioF2().isSelected()) {
                        this.this$0.setResult(this.this$0.calculate2(x, y, z));
                    }

                    this.this$0.getLabelResult().setText("Result: " + this.this$0.getResult().toString());
                }
            }
            else if (e.getSource() == this.this$0.getButtonClear()) {
                this.this$0.getTextFieldX().setText("0");
                this.this$0.getTextFieldY().setText("0");
                this.this$0.getTextFieldZ().setText("0");
                this.this$0.getLabelResult().setText("Result: 0");
            }
            else if (e.getSource() == this.this$0.getButtonMPlus()) {
                if (this.this$0.getRadioV1().isSelected()) {
                    this.this$0.getTextFieldX().setText(String.valueOf(Double.parseDouble(this.this$0.getTextFieldX().getText()) + this.this$0.getResult()));
                }
                else if (this.this$0.getRadioV2().isSelected()) {
                    this.this$0.getTextFieldY().setText(String.valueOf(Double.parseDouble(this.this$0.getTextFieldY().getText()) + this.this$0.getResult()));
                }
                else if (this.this$0.getRadioV3().isSelected()) {
                    this.this$0.getTextFieldZ().setText(String.valueOf(Double.parseDouble(this.this$0.getTextFieldZ().getText()) + this.this$0.getResult()));
                }
                }
            else if (e.getSource() == this.this$0.getButtonMC()) {
                if (this.this$0.getRadioV1().isSelected()) {
                    this.this$0.getTextFieldX().setText("0");
                }
                else if (this.this$0.getRadioV2().isSelected()) {
                    this.this$0.getTextFieldY().setText("0");
                }
                else if (this.this$0.getRadioV3().isSelected()) {
                    this.this$0.getTextFieldZ().setText("0");
                }
            }
        } catch (NumberFormatException var5) {
            JOptionPane.showMessageDialog(this.this$0, "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа", 2);
        }

    }
}
