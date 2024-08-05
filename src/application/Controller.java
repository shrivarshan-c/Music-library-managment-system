package application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ProgressBar; 
import javafx.scene.layout.Pane;
import java.util.ArrayList; 
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {

	
	@FXML
	private Pane pane;
	@FXML
    private Label songLabel;
	@FXML
	private Button prev1;
	@FXML
	private Button pause1;
	@FXML
	private Button play1;
	@FXML
	private Button resetButton;
	@FXML
	private Button next1;
	@FXML
	private Slider volumeSlider;
	@FXML
	private ProgressBar songProgressBar;
	
	
	private Media media;
	
	private MediaPlayer mediaPlayer;
	
	private File directory;
	private File[] files;
	
	private ArrayList<File> songs;
	
	private int songNumber;
	
	
	private Timer timer;
	private TimerTask task;
	private boolean running;
	private Stage stage;
	private Scene scene;
	private Parent root1;
	
    public void switchtoplaylist(MouseEvent event) throws IOException {
		FXMLLoader loaderB = new FXMLLoader(getClass().getResource("playlist.fxml"));
       Parent root1 = loaderB.load();
     	
       stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      scene = new Scene(root1);
      stage.setScene(scene);
      stage.show();
    }
	
	@Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        songs = new ArrayList<File>();
        directory = new File("music");
        files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                songs.add(file);
            }
        }

        if (!songs.isEmpty()) {
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());

            volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                    mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
                }
            });

            songProgressBar.setStyle("-fx-accent: #00FF00");
        } else {
            System.out.println("No songs available.");
        }
    }

	
	
	
	
	public void beginTimer() {
		timer=new Timer();
		task=new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Platform.runLater(()->{
				running=true;
				double current=mediaPlayer.getCurrentTime().toSeconds();
				double end=media.getDuration().toSeconds();
				System.out.println(current/end);
				songProgressBar.setProgress(current/end);
				
				if(current/end ==1) {
					cancelTimer();
				}
				});
				
			}
			
		};
		timer.scheduleAtFixedRate(task, 1000, 1000);
		
	}
	
	public void pausemedia() {
		Platform.runLater(()-> {
		cancelTimer();
       mediaPlayer.pause();
		});
    }
	
	public void playmedia() {
		Platform.runLater(()->{
       beginTimer();
		mediaPlayer.play();
		songProgressBar.setProgress(0); 
		});
    }
	
	public void nextmedia() {
       
		if(songNumber<songs.size()-1) {
			songNumber++;
			mediaPlayer.stop();
			if(running) {
				cancelTimer();
			}
			media = new Media(songs.get(songNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			songLabel.setText(songs.get(songNumber).getName());
			playmedia();
		}
		else {
			songNumber=0;
			mediaPlayer.stop();
			if(running) {
				cancelTimer();
			}
			media = new Media(songs.get(songNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			songLabel.setText(songs.get(songNumber).getName());
			playmedia();
		}
       
    }
	public void prevmedia() {
		if(songNumber>0) {
			songNumber--;
			mediaPlayer.stop();
			if(running) {
				cancelTimer();
			}
			media = new Media(songs.get(songNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			songLabel.setText(songs.get(songNumber).getName());
			playmedia();
		}
		else {
			songNumber=songs.size()-1;
			mediaPlayer.stop();
			if(running) {
				cancelTimer();
			}
			media = new Media(songs.get(songNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			songLabel.setText(songs.get(songNumber).getName());
			playmedia();
		}
    }
        public void resetMedia() {
		songProgressBar.setProgress(0);
		mediaPlayer.seek(Duration.seconds(0));
	}	

	
	public void cancelTimer() {
		running=false;
		timer.cancel();
	}    
	
}
	
	

