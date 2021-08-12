package backend;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller {
    @FXML
    private Button rectangle;
    @FXML
    private Button circle;
    @FXML
    private Canvas canvas;
    @FXML
    private GraphicsContext context;
    @FXML
    private TextField product;
    @FXML
    private Button connect;
    @FXML
    private Button replay;
    @FXML
    private Button clear;
    @FXML
    private Button Start;

    public ArrayList<shape> myShapes = new ArrayList<shape>();
    public ArrayList<Point> myConnections = new ArrayList<Point>();
    public int id = 0;
    private int draggedId;
    private Boolean dragging = false;
    private Boolean connecting = false;
    private Boolean editing =false;
    private int Q=0 , M=0;
    private boolean drawRect=false , drawCircle=false;
    private int[] clicksNumber = {-1,-1};
    int counter = 0;
    BlockingQueue<product> products = new LinkedBlockingQueue<product>();
    ExecutorService service = Executors.newCachedThreadPool();
    List<Qproducer> producers = new LinkedList<Qproducer>();
    List<Machine> machines = new LinkedList<Machine>();
    ArrayList<shape> circles = new ArrayList<shape>();
    ArrayList<shape> rects = new ArrayList<shape>();
    private careTaker c = new careTaker();
    private Boolean start = false;

    String[] colors = {"#8A2BE2","#A52A2A","#7FFF00","#FF7F50","#A9A9A9","#BDB76B","#EE82EE","#FFFF00","#D8BFD8",
            "#D2B48C","#00FF7F","#FA8072","#FFF5EE","#FF0000","#6B8E23","#CD853F"};

    Random random = new Random();
    public void start() throws InterruptedException {
        if(start){
            return;
        }
        else {
            start = true;
        }
        for (int i = 1; i <= Integer.parseInt(product.getText()); i++){
            products.put(new product(i,colors[random.nextInt(colors.length)]));
        }
        c.setMemento(machines, producers, products);
        program xprogram = new program(machines, producers, service, products);
        xprogram.setController(this);
        service.execute(new Runnable() {

            @Override
            public void run() {
                xprogram.openThreads();

            }
        });
    }

    public void replay(){
        if (!start){
            return;
        }
        program xprogram = c.getState();
        xprogram.setController(this);
        machines = xprogram.getMachines();
        producers = xprogram.getProducers();
        products = xprogram.getProducts();
        c = new careTaker();
        c.setMemento(machines,producers,products);
        producers.get(producers.size()-1).setqProducts(new LinkedBlockingQueue<product>());
        service = xprogram.getService();
        service.execute(new Runnable() {
            @Override
            public void run() {
                xprogram.openThreads();
            }
        });
    }

    public void clear(){
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        c = new careTaker();
        machines = new LinkedList<Machine>();
        producers = new LinkedList<Qproducer>();
        products = new LinkedBlockingQueue<product>();
        service = Executors.newCachedThreadPool();
        myShapes = new ArrayList<shape>();
        circles = new ArrayList<shape>();
        rects = new ArrayList<shape>();
        myConnections = new ArrayList<Point>();
        clicksNumber = new int[]{-1,-1};
        Q = 0;
        M = 0;
        counter = 0;
        start = false;
        id = 0;
        int draggedId;
        dragging = false;
        connecting = false;
        editing =false;
        drawRect=false;
        drawCircle=false;
    }

    public void createRect(){
        if(editing) {
            drawRect = true;
            drawCircle = false;
            connecting = false;
        }
    }

    public void createCircle(){
        if(editing) {
            drawCircle = true;
            drawRect = false;
            connecting = false;
        }
    }
    public void connect(){
        drawCircle=false;
        drawRect=false;
        connecting = true;
    }
    public void edit(){
        connecting=false;
        editing=true;
    }

    public void connectShapes(MouseEvent e){
        int clickedShape = clicked(e);
        if(clickedShape != -1 && counter < 2){
            clicksNumber[counter] = clickedShape;
            counter++;
        }
        if(counter == 2){
            shape temp1 = myShapes.get(clicksNumber[0]);
            shape temp2 = myShapes.get(clicksNumber[1]);
            if(temp1.getType() != temp2.getType()){
                if (temp1.getType() == 1){//temp 1 is machine
                    int mode = makeConnection(temp1.getId(),temp2.getId());
                    myConnections.add(new Point(temp1.getId(),temp2.getId()));
                    connectMachines(temp1.getTextNumber(),temp2.getTextNumber(),mode);
                }
                else {
                    int mode = makeConnection(temp2.getId(),temp1.getId());
                    myConnections.add(new Point(temp2.getId(),temp1.getId()));
                    connectMachines(temp2.getTextNumber(),temp1.getTextNumber(),mode);
                }
            }
            counter = 0;
        }
    }

    public int makeConnection(int circleId, int rectId){
        int mode;//mode will equal 1 if q to machine and 2 if machine to q
        shape circle = myShapes.get(circleId);
        shape rect = myShapes.get(rectId);
        double x1, y1, x2, y2;
        if (rect.getX() > circle.getX()){//from q to machine
            x1 = rect.getX();
            mode = 1;
        }else {//from machine to q
            x1 = rect.getX() + rect.getWidth();
            mode = 2;
        }
        y1 = rect.getY() + (rect.getHeight()/2);
        x2 = circle.getX()+25;
        y2 = circle.getY()+25;
        context.beginPath();
        context.moveTo(x1, y1);
        context.lineTo(x2, y2);
        context.stroke();
        circle.draw(context);
        return mode;
    }

    public void connectMachines(int machineId, int queueId, int mode){
        if(mode == 1){//from q to machine
            producers.get(queueId).addMachine(machines.get(machineId),machineId);
        }
        else{
            machines.get(machineId).setQto(producers.get(queueId));
            machines.get(machineId).setIdxQto(queueId);
        }
    }

    public void addElement(MouseEvent e) {
        context= canvas.getGraphicsContext2D();
        context.beginPath();
        if(connecting){
            connectShapes(e);
        }
        else {
            if (drawCircle) {
                shape myShape = new shape(1, id, e.getX() - 25, e.getY() - 25, 50, 50, M);
                myShape.draw(context);
                myShapes.add(myShape);
                myShape = new shape(1, M, e.getX() - 25, e.getY() - 25, 50, 50, M);
                circles.add(myShape);
                Machine myMachine = new Machine(random.nextInt(4000)+2000,null,service);
                myMachine.name = String.valueOf(M);
                machines.add(myMachine);
                id++;
                M++;
                drawCircle = false;
            }
            if (drawRect) {
                shape myShape = new shape(2, id, e.getX(), e.getY(), 60, 40, Q);
                myShape.draw(context);
                myShapes.add(myShape);
                myShape = new shape(2, Q, e.getX(), e.getY(), 60, 40, Q);
                rects.add(myShape);
                try {
                    producers.add(new Qproducer(service));
                    producers.get(producers.size()-1).name = String.valueOf(Q);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                id++;
                Q++;
                drawRect = false;
            }
        }
    }

    public int clicked(MouseEvent e){
        for (int i = 0; i < myShapes.size(); i++) {
            shape temp = myShapes.get(i);
            if(temp.click(e.getX(),e.getY()) == 1){
                draggedId = temp.getId();
                dragging = true;
                return draggedId;
            }
        }
        return -1;
    }
    public void update(MouseEvent e){
        if (dragging && !connecting){
            shape temp = myShapes.get(draggedId);
            if(temp.getType() == 1) {
                temp.setX(e.getX() - 25);
                temp.setY(e.getY() - 25);
            }
            else if(temp.getType() == 2){
                temp.setX(e.getX());
                temp.setY(e.getY());
            }
            drawAll();
        }
    }
    synchronized public void drawAll(){
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < myShapes.size(); i++) {
            shape temp = myShapes.get(i);
            if(temp.getType() == 1){
                int machineNumber = temp.getTextNumber();
                if(machines.get(machineNumber).getProductNumber() != 0){// assign the color of the machine to the circle if not 0
                    temp.setColor(machines.get(machineNumber).getCurrentProduct().getColor());
                }
                else{
                    temp.setColor("#7FFFD4");
                }
            }
            else {
                int queueNumber = temp.getTextNumber();
                temp.setProductsNumber(producers.get(queueNumber).getqProducts().size());
                if(producers.get(queueNumber).getqProducts().size() != 0){// assign the color in the queue to the rect if not 0
                    temp.setColor(producers.get(queueNumber).getqProducts().peek().getColor());
                }
                else{
                    temp.setColor("#7FFFD4");
                }
            }
            temp.draw(context);
        }
        for(int i = 0; i < myConnections.size(); i++){
            makeConnection(myConnections.get(i).x,myConnections.get(i).y);
        }
    }
    public void stop(){
        dragging = false;
    }
}



