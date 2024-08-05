package application;
import java.io.File;
import java.io.File;
import java.util.ArrayList;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.layout.Pane;
import java.util.ArrayList; 
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.application.Platform;

import javafx.beans.value.ObservableValue;
import java.util.Timer;
import java.util.TimerTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class playlistController implements Initializable{
	
		//Database operation for storing songs in a playlist
		 @FXML
		    TextField plname1;
		    @FXML
		    TextField searchsong11;

		    @FXML
		    String name;
		    String song;

		    public void s_done1() {
		        name = plname1.getText();
		        song = searchsong11.getText();

		        if (name.isEmpty() || song.isEmpty()) {
		            System.out.println("Invalid category name");
		        } else {
		            try {
		                Class.forName("com.mysql.jdbc.Driver");
		                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/musicdatabase", "root", "shri@123")) {
		                    // Inserting data into Playlist table
		                    String insertPlaylistQuery = "INSERT INTO Playlist (playlist_name) VALUES (?)";
		                    try (PreparedStatement insertPlaylistStatement = connection.prepareStatement(insertPlaylistQuery, Statement.RETURN_GENERATED_KEYS)) {
		                        insertPlaylistStatement.setString(1, name);
		                        insertPlaylistStatement.executeUpdate();

		                        // Retrieving the generated playlist_id
		                        ResultSet generatedKeys = insertPlaylistStatement.getGeneratedKeys();
		                        int playlistId = -1;
		                        if (generatedKeys.next()) {
		                            playlistId = generatedKeys.getInt(1);
		                        }

		                        // Inserting data into Song table
		                        String insertSongQuery = "INSERT INTO Song (song_name) VALUES (?)";
		                        try (PreparedStatement insertSongStatement = connection.prepareStatement(insertSongQuery, Statement.RETURN_GENERATED_KEYS)) {
		                            insertSongStatement.setString(1, song);
		                            insertSongStatement.executeUpdate();

		                            // Retrieving the generated song_id
		                            ResultSet generatedSongKeys = insertSongStatement.getGeneratedKeys();
		                            int songId = -1;
		                            if (generatedSongKeys.next()) {
		                                songId = generatedSongKeys.getInt(1);
		                            }

		                            // Inserting the relationship into Playlist_Song table
		                            String insertPlaylistSongQuery = "INSERT INTO Playlist_Song (playlist_id, song_id) VALUES (?, ?)";
		                            try (PreparedStatement insertPlaylistSongStatement = connection.prepareStatement(insertPlaylistSongQuery)) {
		                                insertPlaylistSongStatement.setInt(1, playlistId);
		                                insertPlaylistSongStatement.setInt(2, songId);
		                                insertPlaylistSongStatement.executeUpdate();
		                            }

		                            System.out.println("Data inserted");
		                        }
		                    }
		                }
		            } catch (SQLException | ClassNotFoundException e) {
		                e.printStackTrace();
		            }
		        }
		    }
		}

@FXML

//Database operation for fetching the playlist	
public void fetchSongsForPlaylist(int playlistId) {
    try {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/musicdatabase", "root", "waseefasad1976")) {
            String selectSongsQuery = "SELECT s.song_name FROM Song s JOIN Playlist_Song ps ON s.song_id = ps.song_id WHERE ps.playlist_id = ?";
            
            try (PreparedStatement selectSongsStatement = connection.prepareStatement(selectSongsQuery)) {
                selectSongsStatement.setInt(1, playlistId);

                ResultSet resultSet = selectSongsStatement.executeQuery();
                while (resultSet.next()) {
                    String songName = resultSet.getString("song_name");
                    System.out.println("Song Name: " + songName);
                    // You can process the retrieved songs here as needed
                }
            }
        }
    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}

int playlistIdToFetch = 1; // Replace with the actual playlist ID

//Create an instance of your playlistController class
playlistController controller = new playlistController();

//Call the fetchSongsForPlaylist method
controller.fetchSongsForPlaylist(playlistIdToFetch);


	    @FXML
	    private ComboBox<String> pllist;

	    @FXML
	    private Button create2;

	    @FXML
	    private TextField searchbar;
	    

	    @FXML
	    private TableView<String> songlist;

	    @FXML
	    private TableColumn<String, String> songlist2;
		@FXML
	    private Label songLabel2;
		@FXML
		private ProgressBar songProgressBar;
		@FXML
		private TextField searchsong;
		@FXML
		private ComboBox<String> comboBox;
		@FXML
	    private RadioButton rbtn1;
		@FXML
	    private TextField enterplname;

	    @FXML
	    private TextField searchbar1;

	    @FXML
	    private TextField searchsong1;

	    @FXML
	    private TextField plname;
	    
	    
		private Media media;
		
		private MediaPlayer mediaPlayer;
		
		private File directory;
		private File[] files;
		
		private ArrayList<File> songs2;
		
		private int songNumber;
		
		private ArrayList<String> searchedSongs = new ArrayList<>();
		
	
		private Timer timer;
		private TimerTask task;
		private boolean running;

		//For Playlist Combo box
        @FXML
	    private void showpl(MouseEvent event) {

        	pllist.getItems().clear();

        	System.out.println("Playlist works");

        	// Retrieve subfolders from the "music" directory
        	File[] subfolders = new File("music").listFiles(File::isDirectory);

        	if (subfolders == null) {
        	    System.err.println("Music directory not found!");
        	} else {
        	    // Print subfolder names for debugging
        	    System.out.println(Arrays.stream(subfolders).map(File::getName).collect(Collectors.toList()));

        	    // Add subfolder names to the ComboBox
        	    pllist.setItems(FXCollections.observableArrayList(
        	            Arrays.stream(subfolders).map(File::getName).collect(Collectors.toList())
        	    ));

        	    System.out.println("Playlist works");
        	}

	    }
        
        //For FXML loader to next page
        @FXML
	    private void createpl2(MouseEvent event) throws IOException {
	        // Handle Create New Playlist button action
    
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("createplaylist.fxml"));
	        Parent root = loader.load();
	        
	        Scene scene = new Scene(root);
	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.setScene(scene);
	        stage.show(); 
	    }
        
        //File nam
        @Override
		public void initialize(URL arg0, ResourceBundle arg1) {
        	// songLabel2 = (Label) findViewById(R.id.songLabel2);
    		songs2=new ArrayList<File>();
    		directory=new File("music");
    		files = directory.listFiles();
    		if(files !=null) {
    			for(File file: files) {
    				songs2.add(file);
    				System.out.println("song added");
    		}
    		media = new Media(songs2.get(songNumber).toURI().toString());
    		mediaPlayer = new MediaPlayer(media);
    		}
    
        
        @FXML
        void rbtnclk(MouseEvent event) {
        	 String textFieldValue = searchsong11.getText();
                 searchedSongs.add(textFieldValue);
        	    // Print the value to the console
        	    System.out.println("Value in TextField: " + textFieldValue);
        	    rbtn1.setSelected(false);
        }

        
        @FXML
        TextField searchbar11;
    
        @FXML
        void search34(MouseEvent event) {
        	String searchTerm = searchbar11.getText().trim();
            
            String foundSongName = "not_found";

            // Iterate through the songs and find the first matching song
            for (File song : songs2) {
                String songName = song.getName();
                if (songName.contains(searchTerm)) {
                    foundSongName = songName;
                    break; // Stop searching once a match is found
                }
           }
            
            // Display the found song in the text field
            System.out.println(foundSongName);
           // searchbar.setText(foundSongName);
            searchsong11.setText(foundSongName);
        }
        
        
       
        @FXML
        public void s_done() {
       	 System.out.println("Searched Song Names:");
            for (String song : searchedSongs) {
                System.out.println(song);
            }
        }
        @FXML
        void enterplname(ActionEvent event) throws IOException {
        	String s_plname = plname1.getText();
        	System.out.println(s_plname);
       }


	///---------------------------------------------------------- 
	    @FXML
	    private void ssong() {
	    	String searchTerm = searchbar.getText().trim();
	        
	        String foundSongName = "not_found";

	        // Iterate through the songs and find the first matching song
	        for (File song : songs2) {
	            String songName = song.getName();
	            if (songName.contains(searchTerm)) {
	                foundSongName = songName;
	                break; // Stop searching once a match is found
	            }
	        }
	        
	        // Display the found song in the text field
	        System.out.println(foundSongName);
	       // searchbar.setText(foundSongName);
	        searchsong11.setText(foundSongName);

	    }
	   
	    public void search2() {
	    	
	    }
		
	}