package backend;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

public class shape {
    private int type;
    private int id;
    private double x;
    private double y;
    private int width;
    private int height;
    private int textNumber;
    private String color;
    private int productsNumber;

    public shape(int type, int id, double x, double y, int width, int height, int textNumber) {
        this.type = type;
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textNumber = textNumber;
        this.color = "#7FFFD4";
        this.productsNumber = 0;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getR() {
        return width;
    }

    public void setR(int r) {
        this.width = r;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void draw(GraphicsContext context){
        if (this.type == 1) {
            context.setFill(Paint.valueOf(color));
            context.strokeOval(this.x, this.y, 50, 50);
            context.fillOval(this.x, this.y, 50, 50);
            context.setTextAlign(TextAlignment.CENTER);
            context.strokeText("M" + this.textNumber, this.x + (this.width/2), this.y + (this.width/2));
        }
        else{
            context.setFill(Paint.valueOf(color));
            context.rect(this.x,this.y, this.width, this.height );
            context.fillRect(this.x, this.y, width, this.height );
            context.setTextAlign(TextAlignment.CENTER);
            context.strokeRect(this.x,this.y,this.width,this.height);
            context.strokeText("Q"+this.textNumber+" ("+this.productsNumber+")", this.x+(this.width/2), this.y+(this.height/2));
        }
    }
    public int click(double clickedX, double clickedY){
        double centerx = this.x;
        double centery = this.y;
        if(this.type == 1) {
            clickedX = clickedX - 25;
            clickedY = clickedY - 25;
            int dis = (int) (25 - Math.sqrt((centerx - clickedX) * (centerx - clickedX) + (centery - clickedY) * (centery - clickedY)));
            if (dis >= 0) return 1;
        }
        else {
            if((clickedX-this.x) <= this.width && (clickedX - this.x) >= 0 && (clickedY-this.y) <= this.height && (clickedY - this.y) >= 0){
                return 1;
            }
        }
        return 0;
    }

    public int getTextNumber() {
        return textNumber;
    }

    public void setTextNumber(int textNumber) {
        this.textNumber = textNumber;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setProductsNumber(int productsNumber) {
        this.productsNumber = productsNumber;
    }

}

