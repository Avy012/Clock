package clock;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;





    public class clock extends Application {
        BorderPane borderPane;
        CheckBox formatCheckBox;
        Circle[] clist;
        Text time;
        Text date;
        int decide =0; //decide if am,pm is filled or not.
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        int size; // decide size of text, circle...

        public void start(Stage primaryStage){
            borderPane = new BorderPane();
            // Display the window
            Scene scene = new Scene(borderPane, 1030, 400);
            primaryStage.setTitle("Digital Clock");
            primaryStage.setScene(scene);
            primaryStage.show();

            placeThings();
        }

        private void updateDate(Text date) {
            LocalDate currentDate = LocalDate.now();
            date.setText(currentDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
            date.setFont(Font.font("Times New Roman",50));
            date.setFill(Color.NAVY);
        }

        // Update clock display
        private void updateClock(Text am, Text pm) {
            CheckBox box = new CheckBox("24-Hour Format");
            borderPane.setTop(box);
            box.setPadding(new Insets(10));


            // Update the time label every second
            Thread thread = new Thread(() -> {
                while (true) {
                    try {


                        // Get the current time
                        LocalTime now = LocalTime.now();
                        String ampm = now.format(DateTimeFormatter.ofPattern("a"));

                        // Format the time
                        box.setOnAction(e->{
                            if (box.isSelected()) {
                                // 24h format
                                decide =1;
                                timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                am.setFill(Color.gray(0.9));
                                pm.setFill(Color.gray(0.9));
                            }
                            else{
                                //12h format
                                timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");
                                decide =1;
                                if ("오전".equals(ampm)) { // set like this because my computer system language is in Korean
                                    am.setFill(Color.NAVY);
                                    pm.setFill(Color.gray(0.9));
                                } else if ("오후".equals(ampm)) {
                                    am.setFill(Color.gray(0.9));
                                    pm.setFill(Color.NAVY);
                                }
                            }
                        });


                        Platform.runLater(() -> {
                            String formattedTime = now.format(timeFormatter);
                            time.setText(formattedTime);
                        });



                        if (decide ==0) {
                            if ("오전".equals(ampm)) { // set like this because my computer system language is in Korean
                                am.setFill(Color.NAVY);
                                pm.setFill(Color.gray(0.9));
                            } else if ("오후".equals(ampm)) {
                                am.setFill(Color.gray(0.9));
                                pm.setFill(Color.NAVY);
                            }
                        }
                        // Sleep for 1 second
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.setDaemon(true); // Daemonize the thread to stop it when the application exits
            thread.start();
            time.setFont(Font.font("Times New Roman",200));
            am.setFont(Font.font("Times New Roman",100));
            pm.setFont(Font.font("Times New Roman",100));
            time.setFill(Color.NAVY);

        }
        private void circles(){
            clist = new Circle[59];
            HBox circles = new HBox();

            for (int i=0;i<59;i++){
                clist[i] = new Circle(3);
                clist[i].setFill(Color.gray(0.9));
                circles.getChildren().addAll(clist[i]);
            }

            borderPane.setBottom(circles);
            circles.setAlignment(Pos.CENTER);
            circles.setSpacing(10);
            circles.setPadding(new Insets(15));


            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                LocalTime currentSecond = LocalTime.now();
                String second = currentSecond.format(DateTimeFormatter.ofPattern("ss"));
                int cs = Integer.parseInt(second);


                for(int i=0;i<59;i++) {
                    if (i < cs) {
                        clist[i].setFill(Color.BLACK);
                    } else {
                        clist[i].setFill(Color.gray(0.9));
                    }
                }
            }));

            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }

        private void placeThings(){
            HBox times = new HBox();
            VBox ampm = new VBox();
            VBox all = new VBox();

            Text am = new Text("AM");
            Text pm = new Text("PM");
            time = new Text("time");
            date = new Text("date");

            ampm.getChildren().addAll(am,pm); //vbox
            times.getChildren().addAll(ampm, time); //hbox
            all.getChildren().addAll(times,date); //hbox

            ampm.setAlignment(Pos.CENTER_RIGHT);
            times.setSpacing(10);
            all.setSpacing(30);
            borderPane.setCenter(all);

            times.setAlignment(Pos.CENTER);
            all.setAlignment(Pos.CENTER);

            updateClock(am,pm);
            updateDate(date);
            circles();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }


