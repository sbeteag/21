import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 *  Made by Steven Beteag.
 */
public class Controller {
    @FXML private ImageView titleImg = new ImageView();
    @FXML private TextField newPlayerText;
    // All of the player's cards & score labels
    private static String pName;
    @FXML private ImageView pCard1;
    @FXML private ImageView pCard2;
    @FXML private ImageView pCard3;
    @FXML private ImageView pCard4;
    @FXML private ImageView pCard5;
    @FXML private ImageView pCard6;
    @FXML private Label p21Label;
    @FXML private Label playerScoreLabel;
    @FXML private Label newPlayerLabel = new Label();
    @FXML private Button newStart = new Button();
    @FXML private Button startBtn = new Button();

    // "Hit" is disabled once you reach 21+, so the max you can have is 6 cards IF you get 1-6.
    @FXML private Button hitBtn;
    @FXML private Button stayBtn;
    @FXML private Button restartBtn;
    @FXML private ComboBox<String> selectPlayerDrop = new ComboBox<>();

    private Loader load = new Loader();

    private ArrayList<ImageView> playerCards = new ArrayList<>();
    private int playerCardIndex = 0;

    private Random rGen = new Random();
    private int playerScore = 0;
    private ArrayList<Integer> cards = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));

    private int computerScore = 0;
    private int computerCardIndex = 1;

    private ArrayList<ImageView> computerCards = new ArrayList<>();
    @FXML private ImageView cCard1;
    @FXML private ImageView cCard2;
    @FXML private ImageView cCard3;
    @FXML private ImageView cCard4;
    @FXML private ImageView cCard5;
    @FXML private ImageView cCard6;
    private boolean computerHit = true;
    @FXML private Label computerScoreLabel;
    private int computerFirstCard = 0;


    @FXML private Label matchesWonLabel;
    @FXML private Label roundsLabel;

    // All sounds found on freesound.org
    private URL winS = getClass().getResource("sound/win.mp3");
    private AudioClip winSound = new AudioClip(winS.toString());

    private URL loseS = getClass().getResource("sound/lose.wav");
    private AudioClip loseSound = new AudioClip(loseS.toString());

    private URL shuffleS = getClass().getResource("sound/shuffle.wav");
    private AudioClip shuffleSound = new AudioClip(shuffleS.toString());

    private final URL winMatchS = getClass().getResource("sound/matchWin.wav");
    private AudioClip winMatchSound = new AudioClip(winMatchS.toString());

    private final URL loseMatchS = getClass().getResource("sound/loseMatch.wav");
    private AudioClip loseMatchSound = new AudioClip(loseMatchS.toString());

    private boolean soundOn = true;

    private static int roundsWon;
    private static int roundsTotal;
    private static int matchesWon;
    private static int matchesTotal;
    private static int lifeMatchesWon;
    private static int lifeMatchesPlayed;

    @FXML private Label selectPlayerLabel = new Label();

    public void exit(){
        System.exit(1);
    }

    @FXML
    private void hit(){
        updateScore(cards.remove(rGen.nextInt(cards.size())), true);
        playerCardIndex++;
        if (computerHit){
            updateScore(cards.remove(rGen.nextInt(cards.size())), false);
            computerCardIndex++;
        }
    }

    @FXML
    private void stay(){
        roundsTotal++;
        String path = "CardImages/" + computerFirstCard + ".png";
        Image tempImage = new Image(path);
        computerCards.get(0).setImage(tempImage);
        computerScoreLabel.setText(String.valueOf(computerScore) + "/21");
        while (computerHit){
            updateScore(cards.remove(rGen.nextInt(cards.size())), false);
            computerCardIndex++;
            computerScoreLabel.setText(String.valueOf(computerScore) + "/21");
        }
        if (playerScore > 21){
            if(soundOn) loseSound.play();
            p21Label.setText("You went over 21! You lose.");
            increaseRound(0);
        }
        else if (computerScore > 21){
            roundsWon++;
            p21Label.setText("You win!");
            if(soundOn) winSound.play();
            increaseRound(1);
        }
        else if (playerScore > computerScore || playerScore == 21){
            roundsWon++;
            p21Label.setText("You win!");
            if(soundOn) winSound.play();
            increaseRound(1);
        }

        else if (playerScore == computerScore){
            p21Label.setText("You tied (counts as a win).");
            roundsWon++;
            increaseRound(1);
            if(soundOn) winSound.play();
        }
        else{
            if(soundOn) loseSound.play();
            p21Label.setText("You lose!");
            increaseRound(0);
        }
        restartBtn.setDisable(false);
        hitBtn.setDisable(true);
        stayBtn.setDisable(true);
        roundsLabel.setText(roundsWon + "/" + roundsTotal );
    }

    @FXML
    private void restart(){
        if(soundOn) shuffleSound.play();
        Image tempImage = new Image("CardImages/back.png");
        computerCards.get(0).setImage(tempImage);
        p21Label.setText("");
        computerScoreLabel.setText("?/21");
        resetCards();
        hitBtn.setDisable(false);
        stayBtn.setDisable(false);
        updateScore(0, true);
        updateScore(0, false);
        computerFirstCard = cards.remove(rGen.nextInt(cards.size()));
        computerScore += computerFirstCard;
        computerHit = false;
        hit();
        computerHit = true;
        hit();
        restartBtn.setText("Next Round");
        restartBtn.setDisable(true);
        if (playerScore == 21){
            stay();
        }
    }

    @FXML
    private void updateScore(int i, boolean player){
        if (player){
            if (i == 0) {
                playerScore = 0;
                playerCardIndex = 0;
            } else {
                String path = "CardImages/" + i + ".png";
                Image tempImage = new Image(path);
                playerCards.get(playerCardIndex).setImage(tempImage);
                playerScore += i;
                if (playerScore > 21) {
                    stay();
                    hitBtn.setDisable(true);
                }
                if (playerScore == 21){
                    p21Label.setText("You hit 21!");
                    stay();
                }
            }
            playerScoreLabel.setText(String.valueOf(playerScore) + "/21");
        }
        else {
            if (i == 0) {
                computerScore = 0;
                computerCardIndex = 1;
            } else {
                String path = "CardImages/" + i + ".png";
                Image tempImage = new Image(path);
                computerCards.get(computerCardIndex).setImage(tempImage);
                computerScore += i;
                if (computerScore >= 16) {
                    computerHit = false;
                }
            }
        }
    }

    private void resetCards() {
        // Resets the ArrayList of available cards
        cards = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));

        // Resets the images to be blank
        for (ImageView iv : playerCards
                ) {
            iv.setImage(null);
        }
        for (ImageView iv : computerCards
                ) {
            if (computerCards.indexOf(iv) != 0) {
                iv.setImage(null);
            }
        }
    }

    public void initialize(){
        // GIF generated from http://textanim.com/
        titleImg.setImage(new Image("CardImages\\title.gif"));
        this.playerCards = new ArrayList<>();
        this.playerCards.add(pCard1);
        this.playerCards.add(pCard2);
        this.playerCards.add(pCard3);
        this.playerCards.add(pCard4);
        this.playerCards.add(pCard5);
        this.playerCards.add(pCard6);

        this.computerCards = new ArrayList<>();
        this.computerCards.add(cCard1);
        this.computerCards.add(cCard2);
        this.computerCards.add(cCard3);
        this.computerCards.add(cCard4);
        this.computerCards.add(cCard5);
        this.computerCards.add(cCard6);
        playerCardIndex = 0;
        computerCardIndex = 1;
        setPlayerDrop();
    }

        @FXML
    private void startGame(ActionEvent event) throws IOException {
        if (Objects.equals(selectPlayerDrop.getValue(), null)) {
            return;
        } else if (Objects.equals(selectPlayerDrop.getValue(), "Create New")) {
            newPlayerText.setVisible(true);
            newPlayerLabel.setVisible(true);
            newStart.setVisible(true);

            selectPlayerDrop.setVisible(false);
            selectPlayerLabel.setVisible(false);
            startBtn.setVisible(false);
            return;
        }
        pName = selectPlayerDrop.getValue();
        BufferedReader br = new BufferedReader(new FileReader("Saves\\" + pName + ".txt"));
        String[] score = br.readLine().split("\t");
        lifeMatchesWon = Integer.parseInt(score[0]);
        lifeMatchesPlayed = Integer.parseInt(score[1]);
        Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("game.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private void setPlayerDrop(){
        Loader load = new Loader();
        ArrayList<String> playerNames = new ArrayList<>();
        playerNames.addAll(load.getNames());
        ObservableList<String>  data = FXCollections.observableArrayList(playerNames);
        selectPlayerDrop.getItems().add("Create New");
        selectPlayerDrop.getItems().addAll(data);
    }
    @FXML
    private void mute(){
        soundOn = !soundOn;
    }

    private void increaseRound(int i){
        if (i==1){
            if (roundsWon == 3){
                matchesWon++;
                lifeMatchesWon++;
                matchesTotal++;
                lifeMatchesPlayed++;
                p21Label.setText("You won the match!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have won the match!\nYou have won " + lifeMatchesWon + " out of " + lifeMatchesPlayed + " total matches");
                alert.setTitle("You won!");
                alert.setHeaderText("Great Job!");
                alert.show();
                if (soundOn) {
                    winMatchSound.play();
                }
                roundsWon = 0;
                roundsTotal = 0;
                roundsLabel.setText("");
                restartBtn.setText("New Match");
                saveStats();
            }
        }
        else{
            if (roundsTotal - roundsWon == 3){
                matchesTotal++;
                lifeMatchesPlayed++;
                p21Label.setText("You lost the match");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have lost the match.\nYou have won " + lifeMatchesWon + " out of " + lifeMatchesPlayed + " total matches");
                alert.setTitle("You lose!");
                alert.setHeaderText("Try again!");
                alert.show();
                if (soundOn) {
                    loseMatchSound.play();
                }
                roundsWon = 0;
                roundsTotal = 0;
                roundsLabel.setText("");
                restartBtn.setText("New Match");
                saveStats();
            }
        }
        if (matchesTotal > 0) {
            matchesWonLabel.setText(matchesWon + " (" + matchesTotal + ")");
        }
    }

    public void newStart(ActionEvent event) throws IOException {
        String tempName = newPlayerText.getText();
        if (tempName.length() < 1){
            newPlayerLabel.setText("You must enter a name:");
        }
        pName = tempName;
        if (load.getNames().contains(tempName)){
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Overwrite save?");
            a.setHeaderText(tempName + " already exists. Select 'load' to use this profile, or 'overwrite' to replace.");
            ButtonType continueBtn = new ButtonType("Load");
            ButtonType overwriteBtn = new ButtonType("Overwrite");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            a.getButtonTypes().setAll(continueBtn, overwriteBtn, buttonTypeCancel);
            Optional<ButtonType> result = a.showAndWait();
            if (result.get() == continueBtn){
                BufferedReader br = new BufferedReader(new FileReader("Saves\\" + tempName + ".txt"));
                String[] score = br.readLine().split("\t");
                lifeMatchesWon = Integer.parseInt(score[0]);
                lifeMatchesPlayed = Integer.parseInt(score[1]);
            } else if (result.get() == overwriteBtn) {
                BufferedWriter bw = new BufferedWriter(new FileWriter("Saves\\" + tempName + ".txt", false));
                bw.write("0\t0");
                bw.close();
            } else {
                return;
            }
        }
        else{
            BufferedWriter bw = new BufferedWriter(new FileWriter("Saves\\" + tempName + ".txt", false));
            bw.write("0\t0");
            bw.close();
            lifeMatchesWon = 0;
            lifeMatchesPlayed = 0;
        }
        Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("game.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void saveStats() {
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter("Saves\\" + pName + ".txt", false));
            bw.write(lifeMatchesWon + "\t" + lifeMatchesPlayed);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
