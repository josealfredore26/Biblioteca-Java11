package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import view.BibliotecaController;

public class Main extends Application {
	
	private static Stage stagePrincipal;
	private BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	this.stagePrincipal = primaryStage;
    	inicializarLayoutRaiz();
    	inicializarBibliotecaView();
    }
    
    public void inicializarLayoutRaiz() {
		try {
			// Carga el root layout desde un archivo xml
			FXMLLoader cargador = new FXMLLoader();
			cargador.setLocation(getClass().getResource("/view/LayoutRaiz.fxml"));
			rootLayout = (BorderPane) cargador.load();
			// Muestra la escena que contiene el RootLayout
			Scene scene = new Scene(rootLayout);
			stagePrincipal.setScene(scene);
			stagePrincipal.show();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
    }
    
    public void inicializarBibliotecaView() {
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Biblioteca.fxml"));
	        AnchorPane view = (AnchorPane) loader.load();
//	        view.setPadding(new Insets(10));
//	        HBox hbox = (HBox) view.getChildren().get(1);
//	        view.getChildren().remove(hbox);
//	        hbox.setPadding(new Insets(10));
//	        view.getChildren().add(hbox);
	        
	        BibliotecaController controller = loader.getController();
	
	        rootLayout.setCenter(view);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

