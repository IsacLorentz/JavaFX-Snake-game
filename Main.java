package src.application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {

    static Snake snake;
    static boolean game_over = false;
    static boolean restartGame = true;
    static boolean close_game = false;
    static boolean borders = false;
    static Random random = new Random();

    static int speed = 1;
    static int chosenDifficulty=1;
    static int board_width = 30;
    static int board_height = 30;

    static int food_x_cord = 0;
    static int food_y_cord = 0;

    public enum Direction {
        left, right, up, down
    }

    public void start(Stage primaryStage) {
        snake= new Snake(25,new ArrayList<>(),Direction.right);
        try {

            //placerar mat ingame
            placeNewFood();
            speed--;

            //skapar scene för spelet
            VBox vbox = new VBox();
            Canvas canvas = new Canvas(board_width * snake.snake_square_size, board_height * snake.snake_square_size);
            GraphicsContext graph_con = canvas.getGraphicsContext2D();
            vbox.getChildren().add(canvas);

            new AnimationTimer() {
                long last_tick = 0;

                public void handle(long now) {
                    if (last_tick == 0) {
                        last_tick = now;
                        tick(graph_con);
                        return;
                    }
                    if(speed!=0){
                        if (now - last_tick > 1000000000 / speed) {
                            System.out.println("lmao now?");
                            last_tick = now;
                            tick(graph_con);
                        }
                    }
                }

            }.start();

            Scene scene = new Scene(vbox, board_width * snake.snake_square_size, board_height * snake.snake_square_size);
            //Option menu scene

            //checkbox to choose with/without borders, default is without
            CheckBox allowWindows = new CheckBox("Play with with Borders");
            allowWindows.setSelected(false);

            //Choiceboxes to choose difficulty
            ChoiceBox<String> difficulty = new ChoiceBox<>();
            difficulty.getItems().addAll("Easy","Medium","Hard");
            difficulty.setValue("Easy");

            //Button that starts the
            //game and changes the scene
            Button startGame = new Button("Start Game");
            startGame.setOnAction(e->{
                restartGame();
                setDifficulty(difficulty.getValue());
                borders = allowWindows.isSelected();
                primaryStage.setScene(scene);
            });

            VBox layout = new VBox(20);
            layout.getChildren().addAll(startGame,allowWindows, difficulty);
            Scene scene2 = new Scene(layout,board_width * snake.snake_square_size,board_height * snake.snake_square_size);
            scene.addEventFilter(KeyEvent.KEY_PRESSED, key_p -> {
                if (key_p.getCode() == KeyCode.DOWN && snake.direction != Direction.up ) {
                    snake.direction = Direction.down;
                }
                if (key_p.getCode() == KeyCode.UP && snake.direction != Direction.down) {
                    snake.direction = Direction.up;
                }
                if (key_p.getCode() == KeyCode.LEFT && snake.direction != Direction.right) {
                    snake.direction = Direction.left;
                }
                if (key_p.getCode() == KeyCode.RIGHT && snake.direction != Direction.left) {
                    snake.direction = Direction.right;
                }
                if (key_p.getText().equals("q")||key_p.getText().equals("Q")) {
                    game_over = true;
                }
                if (key_p.getText().equals("r")&& (game_over) || key_p.getText().equals("R") && (game_over)) {
                    restartGame=true;
                    primaryStage.setScene(scene2);
                }
                if (key_p.getText().equals("l")||key_p.getText().equals("L")) {
                    close_game = true;
                }

            });

            // add snake squares at the start men logiken ville inte samarbeta så jag var tvungen att lägga en
            // clear när man startade ett nytt game
            snake.snakeList.add(new Snake.SnakeSquare(board_width / 2, board_height / 2));
            snake.snakeList.add(new Snake.SnakeSquare(board_width / 2, board_height / 2));

            primaryStage.setScene(scene2);
            primaryStage.setTitle("George Malki's SNEK SIMULATOR 1");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void restartGame(){
        if(restartGame){
            restartGame = false;

            snake.snakeList.clear();
            snake.snakeList.add(new Snake.SnakeSquare(board_width / 2, board_height / 2));
            snake.snakeList.add(new Snake.SnakeSquare(board_width / 2, board_height / 2));
            speed = 0;
            game_over=false;
        }
    }
    public static void setDifficulty(String chosen){
        switch (chosen){
            case "Easy":
                chosenDifficulty = 1;
                speed = 6;
                break;

            case "Medium":
                chosenDifficulty = 2;
                speed = 10;
                break;

            case "Hard":
                chosenDifficulty = 3;
                speed = 14;
                break;
        }
    }

    public static void tick(GraphicsContext graph_con) {
        if (game_over) {
            graph_con.setFill(Color.ORANGERED);
            graph_con.setFont(new Font("", 50));
            graph_con.fillText("RIP, LOL", 100, 250);
            graph_con.setFont(new Font("", 25));
            graph_con.setFill(Color.WHITE);
            graph_con.fillText("Press r to restart", 130, 30);
            restartGame();
            return;
        }




        for (int i = snake.snakeList.size() - 1; i >= 1; i--) {
            snake.snakeList.get(i).x = snake.snakeList.get(i-1).x;
            snake.snakeList.get(i).y = snake.snakeList.get(i-1).y;
        }

        switch (snake.direction) {
            case left -> {
                snake.snakeList.get(0).x--;
                if (!borders) {
                    if (snake.snakeList.get(0).x < 0) {
                        snake.snakeList.get(0).x = board_width - 1;
                    }
                } else {
                    if (snake.snakeList.get(0).x < 0) {
                        game_over = true;
                    }
                }
            }
            case right -> {
                snake.snakeList.get(0).x++;
                if (!borders) {
                    if (snake.snakeList.get(0).x >= board_width) {
                        snake.snakeList.get(0).x = 0;
                    }
                } else {
                    if (snake.snakeList.get(0).x > board_width) {
                        game_over = true;
                    }
                }
            }
            case up -> {
                snake.snakeList.get(0).y--;
                if (!borders) {
                    if (snake.snakeList.get(0).y < 0) {
                        snake.snakeList.get(0).y = board_height - 1;
                    }
                } else {
                    if (snake.snakeList.get(0).y < 0) {
                        game_over = true;
                    }
                }
            }
            case down -> {
                snake.snakeList.get(0).y++;
                if (!borders) {
                    if (snake.snakeList.get(0).y >= board_height) {
                        snake.snakeList.get(0).y = 0;
                    }
                } else {
                    if (snake.snakeList.get(0).y > board_height) {
                        game_over = true;
                    }
                }
            }
        }

        // snake eats food
        if (food_x_cord == snake.snakeList.get(0).x && food_y_cord == snake.snakeList.get(0).y) {
            snake.snakeList.add(new Snake.SnakeSquare(-1, -1));
            placeNewFood();
        }

        // destroy if snake is hitting itself
        for (int i = 1; i < snake.snakeList.size(); i++) {
            if (snake.snakeList.get(0).x == snake.snakeList.get(i).x && snake.snakeList.get(0).y == snake.snakeList.get(i).y) {
                game_over = true;
            }
        }

        graph_con.setFill(Color.BLACK);
        graph_con.fillRect(0, 0, board_width * snake.snake_square_size, board_height * snake.snake_square_size);

        graph_con.setFill(Color.WHITE);
        graph_con.setFont(new Font("", 30));

        graph_con.fillText("Score: " + (snake.snakeList.size()-2), 10, 30);

        //set border only if borders is true
        if(borders) {
            graph_con.setStroke(Color.WHITESMOKE);
        }
        else{
            graph_con.setStroke(Color.BLACK);
        }
        graph_con.setLineWidth(10);
        graph_con.moveTo(0, 0);
        graph_con.lineTo(board_width * snake.snake_square_size, 0);
        graph_con.moveTo(board_width * snake.snake_square_size, 0);
        graph_con.lineTo(board_width * snake.snake_square_size, board_width * snake.snake_square_size);
        graph_con.moveTo(board_width * snake.snake_square_size, board_width * snake.snake_square_size);
        graph_con.lineTo(0, board_width * snake.snake_square_size);
        graph_con.moveTo(0, board_width * snake.snake_square_size);
        graph_con.lineTo(0, 0);
        graph_con.stroke();

        Color food_color = Color.PURPLE;
        graph_con.setFill(food_color);
        graph_con.fillOval(food_x_cord * snake.snake_square_size, food_y_cord * snake.snake_square_size, snake.snake_square_size, snake.snake_square_size);

        // snake coloring
        for (Snake.SnakeSquare c : snake.snakeList) {
            graph_con.setFill(Color.LAWNGREEN);
            graph_con.fillRect(c.x * snake.snake_square_size, c.y * snake.snake_square_size, snake.snake_square_size - 2, snake.snake_square_size - 2);

        }

    }

    // food placing function
    public static void placeNewFood() {
        place_food: while (true) {
            food_x_cord = random.nextInt(board_width);
            food_y_cord = random.nextInt(board_height);

            for (Snake.SnakeSquare c : snake.snakeList) {
                if (c.x == food_x_cord && c.y == food_y_cord) {
                    continue place_food;
                }
            }
            speed += chosenDifficulty;
            break;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}