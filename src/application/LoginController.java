package application;

import java.io.IOException;
import javafx.application.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.Scene;
import javafx.stage.*;
import javafx.scene.Node;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import java.sql.*;
import java.util.*;


public class LoginController implements Initializable{
	

	// Event Listener on Hyperlink[#si_createAccount].onAction
	@FXML
	   public TextField userInput;
	   @FXML
	   public PasswordField userPassword;
	 //  public Button enter;
	   
	   
	   
	  	public void initialize(URL arg0, ResourceBundle arg1) {
	  		// TODO Auto-generated method stub
	  	
	  		userInput.setOnKeyPressed(event->{
	  			if(event.getCode()==KeyCode.ENTER) {
	  				userPassword.requestFocus();
	  			}
	  		});
	  		userPassword.setOnKeyPressed(event->{
	  			if(event.getCode()==KeyCode.ENTER) {
	  				
	  			}
	  		});
	  		
	  	}
	   
	   
	   
	   
	   @FXML
		public String username;
		public String password;
		
		@FXML
		public void enter(MouseEvent e) throws IOException {
			username=userInput.getText();
			password=userPassword.getText();
			if(username.isEmpty()|| password.isEmpty()) {System.out.println("no empty fields");}
			else {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					try(Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/musicdatabase","root","waseefasad1976")){
						String insertDataQuery="SELECT * from register WHERE UserName=? AND Password =?";
						
						try(PreparedStatement insertDataStatement=connection.prepareStatement(insertDataQuery)){
							insertDataStatement.setString(1,username);
							insertDataStatement.setString(2,password);
							
							ResultSet resultSet=insertDataStatement.executeQuery();
							if(resultSet.next()) {
								System.out.println(resultSet.next());
								System.out.println("Login");
								FXMLLoader loader=new FXMLLoader(getClass().getResource("home.fxml"));
								Parent root=loader.load();
								
								Scene scene=new Scene(root);
								Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
								stage.setScene(scene);
								stage.show();
								
							}else {
								System.out.println("Invalid Credentials");
								
							}
//							insertDataStatement.executeUpdate();
//							System.out.println("Data inserted");
							
						}
					}
					
				}catch(SQLException|ClassNotFoundException ev) {
					ev.printStackTrace();
				}			
			}
			
			
		}
	   
	   
	   
	   
	   
	   
	
	
	
	
	
	
	
	
	@FXML
	public void GotoRegister(MouseEvent e) throws IOException {
		
		FXMLLoader loader=new FXMLLoader(getClass().getResource("Register.fxml"));
		Parent root=loader.load();
		
		Scene scene=new Scene(root);
		Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();	
		
	}
	
//	@FXML
//	public void GotoMainScene(MouseEvent e)throws IOException {
//		
//		System.out.println("hello World");
//		FXMLLoader loader=new FXMLLoader(getClass().getResource("mainScene.fxml"));
//		Parent root=loader.load();
//	
//		Scene scene=new Scene(root);
//		Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
//		stage.setScene(scene);
//	stage.show();
//	}
	
}