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
        super("Табулирование многочлена на отрезке по схеме Горнера");
        this.coefficients = coefficients;
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        JMenu tableMenu = new JMenu("Таблица");
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

        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
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
        Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {

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
// По умолчанию пункт меню является недоступным(данных ещ? нет)
        saveToGraphicsMenuItem.setEnabled(false);
// Создать новое действие по поиску значений многочлена
        Action searchValueAction = new AbstractAction("Найти значение многочлена") {
            public void actionPerformed(ActionEvent event) {
// Запросить пользователя ввести искомую строку
                String value =
                        JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска",
                                "Поиск значения", JOptionPane.QUESTION_MESSAGE);
// Установить введенное значение в качестве иголки
                renderer.setNeedle(value);
// Обновить таблицу
                getContentPane().repaint();
            }
        };

        Action searchRangeAction = new AbstractAction("Найти значение многочлена в диапазоне") {
            public void actionPerformed(ActionEvent event) {
// Запросить пользователя ввести искомую строку
                String value =
                        JOptionPane.showInputDialog(
                                MainFrame.this,
                                "Введите значение для поиска в формате (from to)",
                                "Поиск значения",
                                JOptionPane.QUESTION_MESSAGE
                        );
// Установить введенное значение в качестве иголки
                String[] values = value.split(" ");
                String start = values[0];
                String end = values[1];

                renderer.setNeedleRange(start, end);
// Обновить таблицу
                getContentPane().repaint();
            }
        };

        saveMenuItem = fileMenu.add(saveAction);
        saveMenuItem.setEnabled(false);

        aboutMenuItem = aboutMenu.add(aboutAction);
        aboutMenuItem.setEnabled(true);

// Добавить действие в меню "Таблица"
        searchValueMenuItem = tableMenu.add(searchValueAction);
// По умолчанию пункт меню является недоступным (данных ещ? нет)
        searchValueMenuItem.setEnabled(false);

        searchRangeMenuItem = tableMenu.add(searchRangeAction);
        searchRangeMenuItem.setEnabled(false);
// Создать область с полями ввода для границ отрезка и шага
// Создать подпись для ввода левой границы отрезка
        JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
// Создать текстовое поле для ввода значения длиной в 10 символов
// со значением по умолчанию 0.0
        textFieldFrom = new JTextField("0.0", 10);
// Установить максимальный размер равный предпочтительному, чтобы
// предотвратить увеличение размера поля ввода
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
// Создать подпись для ввода левой границы отрезка
        JLabel labelForTo = new JLabel("до:");
// Создать текстовое поле для ввода значения длиной в 10 символов
// со значением по умолчанию 1.0
        textFieldTo = new JTextField("1.0", 10);
// Установить максимальный размер равный предпочтительному, чтобы
// предотвратить увеличение размера поля ввода
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
// Создать подпись для ввода шага табулирования
        JLabel labelForStep = new JLabel("с шагом:");
// Создать текстовое поле для ввода значения длиной в 10 символов
// со значением по умолчанию 1.0
        textFieldStep = new JTextField("0.1", 10);
// Установить максимальный размер равный предпочтительному, чтобы
// предотвратить увеличение размера поля ввода
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
// Создать контейнер 1 типа "коробка с горизонтальной укладкой"
        Box hboxRange = Box.createHorizontalBox();
// Задать для контейнера тип рамки "объ?мная"
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
// Добавить "клей" C1-H1
        hboxRange.add(Box.createHorizontalGlue());
// Добавить подпись "От"
        hboxRange.add(labelForFrom);
// Добавить "распорку" C1-H2
        hboxRange.add(Box.createHorizontalStrut(10));
// Добавить поле ввода "От"
        hboxRange.add(textFieldFrom);
// Добавить "распорку" C1-H3
        hboxRange.add(Box.createHorizontalStrut(20));
// Добавить подпись "До"
        hboxRange.add(labelForTo);
// Добавить "распорку" C1-H4
        hboxRange.add(Box.createHorizontalStrut(10));
// Добавить поле ввода "До"
        hboxRange.add(textFieldTo);
// Добавить "распорку" C1-H5
        hboxRange.add(Box.createHorizontalStrut(20));
// Добавить подпись "с шагом"
        hboxRange.add(labelForStep);
// Добавить "распорку" C1-H6
        hboxRange.add(Box.createHorizontalStrut(10));
// Добавить поле для ввода шага табулирования
        hboxRange.add(textFieldStep);
// Добавить "клей" C1-H7
        hboxRange.add(Box.createHorizontalGlue());
// Установить предпочтительный размер области равным удвоенному
// минимальному, чтобы при компоновке область совсем не сдавили
        hboxRange.setPreferredSize(new Dimension(
                Double.valueOf(hboxRange.getMaximumSize().getWidth())
                        .intValue(),
                Double.valueOf(hboxRange.getMinimumSize().getHeight())
                        .intValue() * 2));
// Установить область в верхнюю (северную) часть компоновки
        getContentPane().add(hboxRange, BorderLayout.NORTH);
// Создать кнопку "Вычислить"
        JButton buttonCalc = new JButton("Вычислить");
// Задать действие на нажатие "Вычислить" и привязать к кнопке
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
// Считать значения начала и конца отрезка, шага
                    Double from =
                            Double.parseDouble(textFieldFrom.getText());
                    Double to =
                            Double.parseDouble(textFieldTo.getText());
                    Double step =
                            Double.parseDouble(textFieldStep.getText());
// На основе считанных данных создать новый экземпляр модели таблицы
                    data = new GornerTableModel(from, to, step, coefficients);
// Создать новый экземпляр таблицы
                    JTable table = new JTable(data);
// Установить в качестве визуализатора ячеек для
// класса Double разработанный визуализатор
                    table.setDefaultRenderer(Double.class,
                            renderer);
// Установить размер строки таблицы в 30
//        пикселов
                    table.setRowHeight(30);
// Удалить все вложенные элементы из контейнера
//        hBoxResult
                    hBoxResult.removeAll();
// Добавить в hBoxResult таблицу, "об?рнутую" в
//        панель с полосами прокрутки
                    hBoxResult.add(new JScrollPane(table));
// Обновить область содержания главного окна
                    getContentPane().validate();
// Пометить ряд элементов меню как доступных
                    saveToTextMenuItem.setEnabled(true);
                    saveToGraphicsMenuItem.setEnabled(true);
                    searchValueMenuItem.setEnabled(true);
                    searchRangeMenuItem.setEnabled(true);
                    saveMenuItem.setEnabled(true);
                } catch (NumberFormatException ex) {
// В случае ошибки преобразования чисел показать
//        сообщение об ошибке
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
// Создать кнопку "Очистить поля"
        JButton buttonReset = new JButton("Очистить поля");
// Задать действие на нажатие "Очистить поля" и привязать к кнопке
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
// Установить в полях ввода значения по умолчанию
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
// Удалить все вложенные элементы контейнера
//        hBoxResult

                hBoxResult.removeAll();
// Добавить в контейнер пустую панель
                hBoxResult.add(new JPanel());
// Пометить элементы меню как недоступные
                saveToTextMenuItem.setEnabled(false);
                saveToGraphicsMenuItem.setEnabled(false);
                searchValueMenuItem.setEnabled(false);
                searchRangeMenuItem.setEnabled(false);
                saveMenuItem.setEnabled(false);
// Обновить область содержания главного окна
                getContentPane().validate();
            }
        });
// Поместить созданные кнопки в контейнер
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
// Установить предпочтительный размер области равным удвоенному
//        минимальному,чтобы при
// компоновке окна область совсем не сдавили
        hboxButtons.setPreferredSize(new Dimension(
                Double.valueOf(hboxButtons.getMaximumSize().getWidth()).intValue(),
                Double.valueOf(hboxButtons.getMinimumSize().getHeight()).intValue() * 2));
// Разместить контейнер с кнопками в нижней (южной) области
//        граничной компоновки
        getContentPane().add(hboxButtons, BorderLayout.SOUTH);
// Область для вывода результата пока что пустая
        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());
// Установить контейнер hBoxResult в главной (центральной) области
//        граничной компоновки
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }

    protected void saveToGraphicsFile(File selectedFile) {
        try {
// Создать новый байтовый поток вывода, направленный в
//        указанный файл
            DataOutputStream out = new DataOutputStream(new
                    FileOutputStream(selectedFile));
// Записать в поток вывода попарно значение X в точке,
//        значение м/ногочлена в точке
            for (int i = 0; i < data.getRowCount(); i++) {
                out.writeDouble((Double) data.getValueAt(i, 0));
                out.writeDouble((Double) data.getValueAt(i, 1));
            }
// Закрыть поток вывода
            out.close();
        } catch (Exception e) {
// Исключительную ситуацию "ФайлНеНайден" в данном случае
//        можно не обрабатывать,
// так как мы файл созда?м, а не открываем для чтения
        }
    }

    protected void saveToTextFile(File selectedFile) {
        try {
// Создать новый символьный поток вывода, направленный в
//        указанный файл
            PrintStream out = new PrintStream(selectedFile);
// Записать в поток вывода заголовочные сведения

            out.println("Результаты табулирования многочлена по схеме Горнера");
            out.print("Многочлен: ");
            for (int i = 0; i < coefficients.length; i++) {
                out.print(coefficients[i] + "*X^" +
                        (coefficients.length - i - 1));
                if (i != coefficients.length - 1)
                    out.print(" + ");
            }
            out.println();
            out.println("Интервал от " + data.getFrom() + " до " +
                    data.getTo() + " с шагом " + data.getStep());
            out.println("====================================================");
// Записать в поток вывода значения в точках
            for (int i = 0; i < data.getRowCount(); i++) {
                out.println("Значение в точке " + data.getValueAt(i, 0)
                        + " равно " + data.getValueAt(i, 1));
            }
// Закрыть поток
            out.close();
        } catch (FileNotFoundException e) {
// Исключительную ситуацию "ФайлНеНайден" можно не
// обрабатывать, так как мы файл созда?м, а не открываем
        }
    }

    public static void main(String[] args) {
// Если не задано ни одного аргумента командной строки -
// Продолжать вычисления невозможно, коэффиценты неизвестны
        if (args.length == 0) {
            System.out.println("Невозможно табулировать многочлен, для " +
                    "которого не задано ни одного коэффициента!");
            System.exit(-1);
        }
// Зарезервировать места в массиве коэффициентов столько, сколько
//        аргументов командной строки
        Double[] coefficients = new Double[args.length];
        int i = 0;
        try {
// Перебрать аргументы, пытаясь преобразовать их в Double
            for (String arg : args) {
                coefficients[i++] = Double.parseDouble(arg);
            }
        } catch (NumberFormatException ex) {
// Если преобразование невозможно - сообщить об ошибке и
//        завершиться
            System.out.println("Ошибка преобразования строки '" +
                    args[i] + "' в число типа Double");
            System.exit(-2);
        }
// Создать экземпляр главного окна, передав ему коэффициенты
        MainFrame frame = new MainFrame(coefficients);
// Задать действие, выполняемое при закрытии окна
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