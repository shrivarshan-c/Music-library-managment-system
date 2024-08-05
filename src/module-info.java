module Music {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.media;
	requires javafx.graphics;
	requires java.desktop;
	
	
	opens application to javafx.graphics, javafx.fxml;
}
