module Gestion_almacen {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
	opens controller to javafx.fxml;
}
