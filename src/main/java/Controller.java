import io.netty.handler.codec.MessageAggregationException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

public class Controller {

    public Button shootButton;
    public ImageView imageView;
    public ImageView snapchatLogo;

    public void takeAPicture(ActionEvent actionEvent) throws Exception {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File imageFile = fileChooser.showOpenDialog(new Stage());
        FileInputStream inputStream = new FileInputStream(imageFile);
        shootButton.setOpacity(0);
        snapchatLogo.setOpacity(0);
        imageView.setImage(new Image(inputStream));
        MessageBox.display(VisionAPI.runVisionAPI(imageFile.getAbsolutePath()));
    }

    private void configureFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Cuisine");
        fileChooser.setInitialDirectory(new File("D:\\nexus 5\\pics"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }
}
