module Biblioteca {
	requires javafx.controls;
	requires javafx.base;
	requires javafx.fxml;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
	
	opens view to javafx.fxml;
	
	opens model to javafx.base;
	
	exports view to javafx.fxml;
}
