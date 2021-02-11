package src.application;

import java.util.ArrayList;
import java.util.List;


public class Snake {
    int snake_square_size;
    List<SnakeSquare> snakeList;
    Main.Direction direction;

    public static class SnakeSquare {
        int x;
        int y;

        public SnakeSquare(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public Snake(int snakeSize, ArrayList<SnakeSquare> snakeLista, Main.Direction dirr){
        this.snake_square_size = snakeSize;
        this.snakeList = snakeLista;
        this.direction=dirr;

    }
}