
package swingPaint;

import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

import java.util.Vector;

public class Paint extends JPanel {

    int x, y, x2, y2;
    static String shapeState = "Line";
    static String colorState = "#000000";
    static Vector<Coordinates> shapes = new Vector<>();
    JButton lineBtn, rectBtn, ovalBtn,undoBtn,clearBtn, redBtn,blueBtn,blackBtn;

    public static void main(String[] args) {
        new Paint().init();
    }

    ActionListener shapeAction = e -> {
        shapeState = e.getActionCommand();
    };
    ActionListener colorAction = e -> {
        colorState = e.getActionCommand();
    };
    ActionListener clearAction = e -> {
        if (e.getActionCommand().equals("Clear"))
            shapes.clear();
        else if (e.getActionCommand().equals("Undo")) {
            shapes.remove(shapes.size()-1);
        }
        try {
            Robot robot = new Robot();
            robot.mouseMove(100,100);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

        } catch (AWTException ex) {
            throw new RuntimeException(ex);
        }
    };



    public void init(){
        JFrame frame = new JFrame("Paint");
        JPanel shape = new JPanel();
        JPanel colors = new JPanel();
        lineBtn = new JButton("Line");
        lineBtn.addActionListener(shapeAction);
        shape.add(lineBtn);

        rectBtn = new JButton("Rectangle");
        rectBtn.addActionListener(shapeAction);
        shape.add(rectBtn);

        ovalBtn = new JButton("Oval");
        ovalBtn.addActionListener(shapeAction);
        shape.add(ovalBtn);

        clearBtn = new JButton("Clear");
        clearBtn.addActionListener(clearAction);
        colors.add(clearBtn);

        undoBtn = new JButton("Undo");
        undoBtn.addActionListener(clearAction);
        colors.add(undoBtn);

        blackBtn = new JButton("black");
        blackBtn.setActionCommand("#000000");
        blackBtn.setForeground(Color.black);
        blackBtn.addActionListener(colorAction);
        colors.add(blackBtn);

        blueBtn = new JButton("blue");
        blueBtn.setActionCommand("#0000FF");
        blueBtn.setForeground(Color.blue);
        blueBtn.addActionListener(colorAction);
        colors.add(blueBtn);

        redBtn = new JButton("red");
        redBtn.setActionCommand("#FF0000");
        redBtn.setForeground(Color.red);
        redBtn.addActionListener(colorAction);
        colors.add(redBtn);




        frame.setContentPane(new Paint());
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(shape,BorderLayout.NORTH);
        content.add(colors,BorderLayout.SOUTH);

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    Paint() {
        x = y = x2 = y2 = 0; //
        MyMouseListener listener = new MyMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public void setStartPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setEndPoint(int x, int y) {
        this.x2 = (x);
        this.y2 = (y);
    }

    public void drawRect(Graphics g, int x, int y, int x2, int y2) {
        int px = Math.min(x,x2);
        int py = Math.min(y,y2);
        int pw=Math.abs(x-x2);
        int ph=Math.abs(y-y2);
        g.drawRect(px, py, pw, ph);
    }

    public void drawOval (Graphics g, int x, int y, int x2, int y2){
        int width = x2 - x;
        int height = y2 - y;
        int xTemp = x;
        int yTemp = y;
        if (width < 0) {
            xTemp = x2;
            width = -width;
        }
        if (height < 0) {
            yTemp = y2;
            height = -height;
        }
        g.drawOval(xTemp, yTemp, width, height);
    }

    class MyMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            setStartPoint(e.getX(), e.getY());
        }

        public void mouseDragged(MouseEvent e) {
            setEndPoint(e.getX(), e.getY());
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            setEndPoint(e.getX(), e.getY());
            shapes.add(new Coordinates(x,y,x2,y2,shapeState,colorState));
            repaint();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Coordinates shape : shapes) {
            g.setColor(Color.decode(shape.color));
            switch (shape.name) {
                    case "Line":
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "Rectangle":
                        drawRect(g, shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "Oval":
                        drawOval(g, shape.x, shape.y, shape.x2, shape.y2);
                        break;
                }
            }
            g.setColor(Color.decode(colorState));
            switch (shapeState) {
            case "Line":
                g.drawLine(x, y, x2, y2);
                break;
            case "Rectangle":
                drawRect(g, x, y, x2, y2);
                break;
            case "Oval":
                drawOval(g, x, y, x2, y2);
                break;
            default:
        }

        }
    }

