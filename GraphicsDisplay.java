package bsu.rfe.java.group5.Lab4_Barsukevich.var4B;


import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;


public class GraphicsDisplay extends JPanel {
    // Список координат точек для построения графика
    private double[][] graphicsData;
    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean showFilling = false;
    private boolean isRotated = false;
    private double minX = 0;
    private double maxX = 0;
    private double minY = 0;
    private double maxY = 0;
    private double[][] viewport = new double[2][2];
    private double scaleX;
    private double scaleY;
    private final BasicStroke graphicsStroke;
    private final BasicStroke axisStroke;
    private final BasicStroke gridStroke;
    private final BasicStroke markerStroke;
    private final BasicStroke selectionStroke;
    private boolean scaleMode = false;
    // Различные шрифты отображения надписей
    private final Font axisFont;

    private int selectedMarker = -1;
    private boolean changeMode = false;
    private final Rectangle2D.Double selectionRect = new Rectangle2D.Double();

    private final ArrayList<double[][]> undoHistory = new ArrayList<>();
    private double[] originalPoint = new double[2];

    public GraphicsDisplay() {
        setBackground(Color.GRAY);
        float[] dashPattern = {
                5.0f, 5.0f,
                5.0f, 5.0f,
                5.0f, 5.0f,
                25.0f, 5.0f,
                10.0f, 5.0f,
                10.0f, 5.0f
        };
        graphicsStroke = new BasicStroke(
                2.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND,
                10.0f,
                dashPattern,
                0.0f
        );
        axisStroke = new BasicStroke(
                2.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f,

                null,
                0.0f
        );
        gridStroke = new BasicStroke(
                1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f,
                null,
                0.0f
        );

        markerStroke = new BasicStroke(
                1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f,
                null,
                0.0f
        );

        selectionStroke = new BasicStroke(
                1.0F,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0F,
                new float[]{10.0F, 10.0F},
                0.0F
        );
// Шрифт для подписей осей координат
        axisFont = new Font("Serif", Font.BOLD, 36);
        addMouseMotionListener(new MouseMotionHandler());
        addMouseListener(new MouseHandler());
    }
// Данный метод вызывается из обработчика
// элемента меню "Открыть файл с графиком"
// главного окна приложения в случае успешной загрузки данных

    // Методы-модификаторы для изменения параметров отображения графика
// Изменение любого параметра приводит к перерисовке области
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    public void setShowFilling(boolean showFilling) {
        this.showFilling = showFilling;
        repaint();
    }

    public void setRotated(boolean selected) {
        isRotated = selected;
        repaint();
    }

    // Метод отображения всего компонента, содержащего график
    public void paintComponent(Graphics g) {
        /* Шаг 1 - Вызвать метод предка для заливки области цветом заднего фона
         * Эта функциональность - единственное, что осталось в наследство от
         * paintComponent класса JPanel
         */
        super.paintComponent(g);
// Шаг 2 - Если данные графика не загружены (при показе компонента
//        при запуске программы) - ничего не делать
        if (graphicsData == null || graphicsData.length == 0) return;
// Шаг 3 - Определить минимальное и максимальное значения для
//        координат X и Y
// Это необходимо для определения области пространства, подлежащей
//        отображению
// Еѐ верхний левый угол это (minX, maxY) - правый нижний это
//        (maxX, minY)


/* Шаг 4 - Определить (исходя из размеров окна) масштабы по осям X
и Y - сколько пикселов
* приходится на единицу длины по X и по Y
*/

        if (!isRotated) {
            scaleX = getSize().getWidth() / (viewport[1][0] - viewport[0][0]);
            scaleY = getSize().getHeight() / (viewport[0][1] - viewport[1][1]);
        }

// Шаг 7 - Сохранить текущие настройки холста
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();
// Шаг 8 - В нужном порядке вызвать методы отображения элементов
//        графика
// Порядок вызова методов имеет значение, т.к. предыдущий рисунок
//        будет затираться последующим
// Первыми (если нужно) отрисовываются оси координат.


        if (isRotated) {
            canvas.rotate(-Math.PI / 2);
            //but we should do it here because we don't want it to
            //influence rotation
            //do exact opposite, since we've rotated it
            this.scaleX = this.getSize().getHeight() / (this.viewport[1][0] - this.viewport[0][0]);
            this.scaleY = this.getSize().getWidth() / (this.viewport[0][1] - this.viewport[1][1]);

            zoomToNoUpdate(
                    viewport[0][0] + (this.viewport[1][0] - this.viewport[0][0]),
                    viewport[0][1],
                    viewport[1][0] - (this.viewport[1][0] - this.viewport[0][0]),
                    viewport[1][1]
            );
        }

        if (showFilling) {
            fillGraphics(canvas);
        }

        if (showAxis) {
            paintAxis(canvas);
            paintGrid(canvas);
        }
        paintGraphics(canvas);

        if (showMarkers) {
            paintMarkers(canvas);
        }
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
        paintSelection(canvas);
    }

    private void paintSelection(Graphics2D canvas) {
        if (scaleMode) {
            canvas.setStroke(selectionStroke);
            canvas.setColor(Color.BLACK);
            canvas.draw(this.selectionRect);
        }
    }

    // Отрисовка графика по прочитанным координатам
    protected void paintGraphics(Graphics2D canvas) {
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.LIGHT_GRAY);
        Double currentX = null;
        Double currentY = null;


        for (double[] point : graphicsData) {
            if (!(point[0] < this.viewport[0][0]) && !(point[1] > this.viewport[0][1]) && !(point[0] > this.viewport[1][0]) && !(point[1] < this.viewport[1][1])) {
                if (currentX != null) {
                    canvas.draw(new Line2D.Double(this.xyToPoint(currentX, currentY), this.xyToPoint(point[0], point[1])));
                }

                currentX = point[0];
                currentY = point[1];
            }
        }
    }

    public void displayGraphics(double[][] graphicsData) {
        this.graphicsData = graphicsData;

        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;


// Найти минимальное и максимальное значение функции
        for (int i = 1; i < graphicsData.length; i++) {
            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
            }
        }

        zoomTo(this.minX, this.maxY, this.maxX, this.minY);
    }

    protected void fillGraphics(Graphics2D canvas) {
        boolean regionStarted = false;
        boolean last = graphicsData[0][1] > 0;
        double areaSum = 0d;
        int start = 0;

        for (int i = 0; i < graphicsData.length; i++) {

            if (!regionStarted && last != graphicsData[i][1] > 0) {
                regionStarted = true;
                last = graphicsData[i][1] > 0;
                start = i;
                areaSum = 0.0;
            }

            if (last != graphicsData[i][1] > 0) {

                regionStarted = false;
                last = graphicsData[i][1] > 0;
                areaSum += calculateArea(Arrays.copyOfRange(
                        graphicsData,
                        start,
                        i
                ));

                Point2D.Double startPoint = xyToPoint(
                        graphicsData[start][0],
                        graphicsData[start][1]
                );

                Point2D.Double currentPoint = xyToPoint(
                        graphicsData[i][0],
                        graphicsData[i][1]
                );

                canvas.setColor(new Color(0, 128, 255));
                GeneralPath regionPath = new GeneralPath();
                regionPath.moveTo(startPoint.getX(), startPoint.getY());
                for (int j = start; j < i; j++) {
                    currentPoint = xyToPoint(
                            graphicsData[j][0],
                            graphicsData[j][1]
                    );
                    regionPath.lineTo(currentPoint.getX(), currentPoint.getY());
                }
                //if it is the end of graph
                //we should fill anyway
                if (i == graphicsData.length - 1) {
                    currentPoint = xyToPoint(
                            graphicsData[i][0],
                            graphicsData[i][1]
                    );
                    regionPath.lineTo(currentPoint.getX(), currentPoint.getY());
                }
                Point2D.Double yZero = xyToPoint(graphicsData[i][0], 0);
                Point2D.Double yZeroStart = xyToPoint(
                        graphicsData[start][0],
                        0
                );
                regionPath.lineTo(yZero.getX(), yZero.getY());
                regionPath.lineTo(yZeroStart.getX(), yZeroStart.getY());
                regionPath.lineTo(startPoint.getX(), startPoint.getY());
                regionPath.closePath();
                canvas.fill(regionPath);

                // Display the area value in the center of the region
                DecimalFormat df = new DecimalFormat("#.##");
                String areaValue = df.format(areaSum);
                int centerX = (int) ((startPoint.getX() + currentPoint.getX()) / 2);
                canvas.setColor(Color.BLACK);
                canvas.drawString(areaValue, centerX, (int) startPoint.getY());
            }
        }
    }

    // Отображение маркеров точек, по которым рисовался график
    protected void paintMarkers(Graphics2D canvas) {
// Шаг 1 - Установить специальное перо для черчения контуров
//        маркеров
        canvas.setStroke(markerStroke);

// Шаг 2 - Организовать цикл по всем точкам графика
        int currentIt = 0;
        for (double[] point : graphicsData) {

            if (currentIt++ == selectedMarker) {
                canvas.setColor(Color.BLACK);

                Point2D.Double currentPoint = xyToPoint(point[0], point[1]);
                DecimalFormat format = new DecimalFormat("#.##");
                canvas.drawString(
                        format.format(point[0]) +
                                "   " +
                                format.format(point[1]),
                        (int) currentPoint.getX(),
                        (int) currentPoint.getY()
                );
            }
/* Эллипс будет задаваться посредством указания координат
его центра
и угла прямоугольника, в который он вписан */
// Центр - в точке (x,y)

// Задать эллипс по центру и диагонали
            canvas.setColor(Color.BLACK);
            if (isAscending(point)) {
//                System.out.`println(Arrays.toString(point));
                canvas.setColor(Color.GREEN);
            }

            Line2D.Double verticalLine = new Line2D.Double();
            verticalLine.setLine(
                    constructPoint(xyToPoint(point[0], point[1]), -5, -5),
                    constructPoint(xyToPoint(point[0], point[1]), 5, -5)
            );
            Line2D.Double horizontalLine = new Line2D.Double();
            horizontalLine.setLine(
                    constructPoint(xyToPoint(point[0], point[1]), -5, -5),
                    constructPoint(xyToPoint(point[0], point[1]), 0, 5)
            );
            Line2D.Double diagonalLine = new Line2D.Double();
            diagonalLine.setLine(
                    constructPoint(xyToPoint(point[0], point[1]), 5, -5),
                    constructPoint(xyToPoint(point[0], point[1]), 0, 5)
            );


            canvas.draw(verticalLine);
            canvas.draw(horizontalLine);
            canvas.draw(diagonalLine);
        }
    }

    private boolean isAscending(double[] point) {
        double average = 0.0D;
        for (double[] graphicsDatum : graphicsData) {
            average = average + graphicsDatum[1];
        }

        average = average / graphicsData.length;

        return point[1] > average;
    }

    private Point2D.Double constructPoint(
            Point2D.Double point,
            double dx,
            double dy
    ) {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(
                point.getX() + dx,
                point.getY() + dy
        );
        return dest;
    }

    // Метод, обеспечивающий отображение осей координат
    protected void paintAxis(Graphics2D canvas) {
        canvas.setStroke(this.axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setFont(this.axisFont);
        FontRenderContext context = canvas.getFontRenderContext();
        Rectangle2D bounds;
        Point2D.Double labelPos;
        if (this.viewport[0][0] <= 0.0 && this.viewport[1][0] >= 0.0) {
            canvas.draw(new Line2D.Double(this.xyToPoint(0.0, this.viewport[0][1]), this.xyToPoint(0.0, this.viewport[1][1])));
            canvas.draw(new Line2D.Double(this.xyToPoint(-(this.viewport[1][0] - this.viewport[0][0]) * 0.0025, this.viewport[0][1] - (this.viewport[0][1] - this.viewport[1][1]) * 0.015), this.xyToPoint(0.0, this.viewport[0][1])));
            canvas.draw(new Line2D.Double(this.xyToPoint((this.viewport[1][0] - this.viewport[0][0]) * 0.0025, this.viewport[0][1] - (this.viewport[0][1] - this.viewport[1][1]) * 0.015), this.xyToPoint(0.0, this.viewport[0][1])));
            bounds = this.axisFont.getStringBounds("y", context);
            labelPos = this.xyToPoint(0.0, this.viewport[0][1]);
            canvas.drawString("y", (float)labelPos.x + 10.0F, (float)(labelPos.y + bounds.getHeight() / 2.0));
        }

        if (this.viewport[1][1] <= 0.0 && this.viewport[0][1] >= 0.0) {
            canvas.draw(new Line2D.Double(this.xyToPoint(this.viewport[0][0], 0.0), this.xyToPoint(this.viewport[1][0], 0.0)));
            canvas.draw(new Line2D.Double(this.xyToPoint(this.viewport[1][0] - (this.viewport[1][0] - this.viewport[0][0]) * 0.01, (this.viewport[0][1] - this.viewport[1][1]) * 0.005), this.xyToPoint(this.viewport[1][0], 0.0)));
            canvas.draw(new Line2D.Double(this.xyToPoint(this.viewport[1][0] - (this.viewport[1][0] - this.viewport[0][0]) * 0.01, -(this.viewport[0][1] - this.viewport[1][1]) * 0.005), this.xyToPoint(this.viewport[1][0], 0.0)));
            bounds = this.axisFont.getStringBounds("x", context);
            labelPos = this.xyToPoint(this.viewport[1][0], 0.0);
            canvas.drawString("x", (float)(labelPos.x - bounds.getWidth() - 10.0), (float)(labelPos.y - bounds.getHeight() / 2.0));
        }
    }

    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - this.viewport[0][0];
        double deltaY = this.viewport[0][1] - y;
        return new Point2D.Double(deltaX * this.scaleX, deltaY * this.scaleY);
    }

    protected double[] translatePointToXY(int x, int y) {
        return new double[]{this.viewport[0][0] + (double)x / this.scaleX, this.viewport[0][1] - (double)y / this.scaleY};
    }


    private static double calculateArea(double[][] points) {
        int n = points.length;
        if (n < 2) {
            throw new IllegalArgumentException("At least two points are required.");
        }

        double area = 0.0;
        for (int i = 1; i < n; i++) {
            double x0 = points[i - 1][0];
            double y0 = points[i - 1][1];
            double x1 = points[i][0];
            double y1 = points[i][1];

            area += (y0 + y1) * (x1 - x0) / 2;
        }

        return Math.abs(area);
    }

    private void zoomTo(double x1, double y1, double x2, double y2) {
        zoomToNoUpdate(x1, y1, x2, y2);
        repaint();
    }

    private void zoomToNoUpdate(double x1, double y1, double x2, double y2) {
        viewport[0][0] = x1;
        viewport[0][1] = y1;
        viewport[1][0] = x2;
        viewport[1][1] = y2;
    }

    private int findSelectedPoint(double x, double y) {
        if (graphicsData == null) {
            return -1;
        }
        for (int i = 0; i < graphicsData.length; i++) {
            Point2D.Double point = xyToPoint(
                    graphicsData[i][0],
                    graphicsData[i][1]
            );
            double distance = (point.getX() - x) *
                    (point.getX() - x) +
                    (point.getY() - y) *
                            (point.getY() - y);
            if (distance <= 100) {
                return i;
            }
        }
        return -1;
    }

    private void paintGrid(Graphics2D canvas) {
        canvas.setStroke(this.gridStroke);
        canvas.setColor(Color.WHITE);

        canvas.draw(new Line2D.Double(this.xyToPoint(this.viewport[0][0], this.viewport[0][1] + (this.viewport[1][1] - this.viewport[0][1]) * 0.1), this.xyToPoint(this.viewport[1][0], this.viewport[0][1] + (this.viewport[1][1] - this.viewport[0][1]) * 0.1)));
        canvas.draw(new Line2D.Double(this.xyToPoint(this.viewport[0][0], this.viewport[0][1] + (this.viewport[1][1] - this.viewport[0][1]) * 0.5), this.xyToPoint(this.viewport[1][0], this.viewport[0][1] + (this.viewport[1][1] - this.viewport[0][1]) * 0.5)));
        canvas.draw(new Line2D.Double(this.xyToPoint(this.viewport[0][0], this.viewport[0][1] + (this.viewport[1][1] - this.viewport[0][1]) * 0.9), this.xyToPoint(this.viewport[1][0], this.viewport[0][1] + (this.viewport[1][1] - this.viewport[0][1]) * 0.9)));
        canvas.drawString("90%", 20, (int) (getHeight() * 0.1));
        canvas.drawString("50%", 20, (int) (getHeight() * 0.5));
        canvas.drawString("10%", 20, (int) (getHeight() * 0.9));
    }

    public class MouseHandler extends MouseAdapter {
        public MouseHandler() {
        }

        public void mouseClicked(MouseEvent ev) {
            if (ev.getButton() == 3) {
                if (!undoHistory.isEmpty()) {
                    viewport = undoHistory.get(undoHistory.size() - 1);
                    undoHistory.remove(undoHistory.size() - 1);
                } else {
                    zoomTo(minX, maxY, maxX, minY);
                }

                repaint();
            }

        }

        public void mousePressed(MouseEvent ev) {
            if (ev.getButton() == 1) {
                selectedMarker = findSelectedPoint(ev.getX(), ev.getY());
                originalPoint = translatePointToXY(ev.getX(), ev.getY());
                if (selectedMarker >= 0) {
                    changeMode = true;
                    setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                } else {
                    scaleMode = true;
                    setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    selectionRect.setFrame(ev.getX(), ev.getY(), 1.0, 1.0);
                }

            }
        }

        public void mouseReleased(MouseEvent ev) {
            if (ev.getButton() == 1) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if (changeMode) {
                    changeMode = false;
                } else {
                    scaleMode = false;
                    double[] finalPoint = translatePointToXY(ev.getX(), ev.getY());
                    undoHistory.add(viewport);
                    viewport = new double[2][2];
                    zoomTo(originalPoint[0], originalPoint[1], finalPoint[0], finalPoint[1]);
                    repaint();
                }

            }
        }
    }

    public class MouseMotionHandler implements MouseMotionListener {
        public MouseMotionHandler() {
        }

        public void mouseMoved(MouseEvent ev) {
            selectedMarker = findSelectedPoint(ev.getX(), ev.getY());
            if (selectedMarker >= 0) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            repaint();
        }

        public void mouseDragged(MouseEvent ev) {

            double width = (double) ev.getX() - selectionRect.getX();
            if (width < 5.0) {
                width = 5.0;
            }

            double height = (double) ev.getY() - selectionRect.getY();
            if (height < 5.0) {
                height = 5.0;
            }

            selectionRect.setFrame(selectionRect.getX(), selectionRect.getY(), width, height);
            repaint();


        }
    }

}
