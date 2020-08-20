import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class InsertionSortHistogram extends Application {

    private Random rng = new Random();

    private ExecutorService exec = Executors.newCachedThreadPool(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });

    @Override
    public void start(Stage primaryStage) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
//        chart.setAnimated(false);
        chart.setCategoryGap(2);
        chart.setBarGap(2);
        xAxis.setLabel("Array Index");
        yAxis.setLabel("Element's Value");

        Series<String, Number> series = generateRandomIntegerSeries(30);
        series.setName("Insertion Sorting Histogram");
        chart.getData().add(series);

        String num = "Element status : ";
        for (int i = 0; i < 30; i++) {
            num += series.getData().get(i).getYValue().intValue() + "\t";
        }

        Label arrayStatus = new Label();
//        arrayStatus.setPadding(new Insets(5,0,0,100));
        arrayStatus.setText(num.replace(" ","\t"));
        arrayStatus.setAlignment(Pos.CENTER);

        Button sort = new Button("Sort");

//        Button reset = new Button("Reset");
//        reset.setOnAction(e -> chart.getData().set(0, generateRandomIntegerSeries(30)));

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(chart, arrayStatus, sort);

//        HBox buttons = new HBox(5, sort, reset);
//        buttons.setAlignment(Pos.CENTER);
//        buttons.setPadding(new Insets(5));

        sort.setOnAction(e -> {
            Task<Void> animateSortTask = createSortingTask(chart.getData().get(0), arrayStatus);
            sort.setDisable(true);
            animateSortTask.setOnSucceeded(event -> sort.setDisable(false));
            exec.submit(animateSortTask);
        });

        StackPane root = new StackPane();
//        root.setBottom(buttons);
        root.getChildren().add(vBox);
        Scene scene = new Scene(root,1200, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Insertion Sort Algorithm");
        primaryStage.show();


    }

    private Task<Void> createSortingTask(Series<String, Number> series, Label arrayStatus) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                ObservableList<Data<String, Number>> data = series.getData();
                int n = data.size();
                for (int j = 1; j < n; j++) {
                    Data<String, Number> key = data.get(j);
                    int i = j - 1;
                    while ((i > -1) && ( data.get(i).getYValue().intValue() > key.getYValue().intValue() )) {

                        Data<String, Number> first = data.get(i + 1);
                        Data<String, Number> second = data.get(i);

                        Platform.runLater(() -> {
                            first.getNode().setStyle("-fx-background-color: green ;");
                            second.getNode().setStyle("-fx-background-color: green ;");
                        });

                        Thread.sleep(300);

                        if (first.getYValue().doubleValue() > second.getYValue().doubleValue()) {
                            CountDownLatch latch = new CountDownLatch(1);
                            Platform.runLater(() -> {


                                Animation swap = createSwapAnimation(first, second);
                                swap.setOnFinished(e -> latch.countDown());
                                swap.play();



                            });
                            latch.await();
                        }
                        Thread.sleep(300);

                        Platform.runLater(() -> {
                            first.getNode().setStyle("");
                            second.getNode().setStyle("");

                            String num = "Element status : ";

                            for (int x = 0; x < 30; x++) {
                                num += String.valueOf(series.getData().get(x).getYValue().intValue()).trim() + "\t";
                            }
                            arrayStatus.setText(num.replace(" ","\t"));
                        });

                        i--;
                    }

                    Data<String, Number> first = data.get(i + 1);
                    Data<String, Number> second = key;

                    Platform.runLater(() -> {
                        first.getNode().setStyle("-fx-background-color: blue ;");
                        second.getNode().setStyle("-fx-background-color: blue ;");
                    });

                    Thread.sleep(400);

                    if (first.getYValue().doubleValue() > second.getYValue().doubleValue()) {
                        CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(() -> {


                            Animation swap = createSwapAnimation(first, second);
                            swap.setOnFinished(e -> latch.countDown());
                            swap.play();



                        });
                        latch.await();
                    }
                    Thread.sleep(400);

                    Platform.runLater(() -> {
                        first.getNode().setStyle("");
                        second.getNode().setStyle("");

                        String num = "Element status : ";

                        for (int x = 0; x < 30; x++) {
                            num += String.valueOf(series.getData().get(x).getYValue().intValue()).trim() + "\t";
                        }
                        arrayStatus.setText(num.replace(" ","\t"));
                    });

                }
                return null;
            }
        };
    }

    private <T> Animation createSwapAnimation(Data<?, T> first, Data<?, T> second) {
        double firstX = first.getNode().getParent().localToScene(first.getNode().getBoundsInParent()).getMinX();
        double secondX = first.getNode().getParent().localToScene(second.getNode().getBoundsInParent()).getMinX();

        double firstStartTranslate = first.getNode().getTranslateX();
        double secondStartTranslate = second.getNode().getTranslateX();

        TranslateTransition firstTranslate = new TranslateTransition(Duration.millis(500), first.getNode());
        firstTranslate.setByX(secondX - firstX);
        TranslateTransition secondTranslate = new TranslateTransition(Duration.millis(500), second.getNode());
        secondTranslate.setByX(firstX - secondX);
        ParallelTransition translate = new ParallelTransition(firstTranslate, secondTranslate);

        translate.statusProperty().addListener((obs, oldStatus, newStatus) -> {
            if (oldStatus == Animation.Status.RUNNING) {
                T temp = first.getYValue();
                first.setYValue(second.getYValue());
                second.setYValue(temp);
                first.getNode().setTranslateX(firstStartTranslate);
                second.getNode().setTranslateX(secondStartTranslate);
            }
        });

        return translate;
    }

    private Series<String, Number> generateRandomIntegerSeries(int n) {
        Series<String, Number> series = new Series<>();
        for (int i = 1; i <= n; i++) {
            series.getData().add(new Data<>(Integer.toString(i), rng.nextInt(90) + 10));
        }
        return series;
    }


}
