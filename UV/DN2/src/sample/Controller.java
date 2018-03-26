package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Controller {

    public MenuItem fileOpen;
    public ImageView imageView;
    public TextField urlInputField;
    public ProgressBar progressBar;

    @FXML
    public void openFromFile() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterAll = new FileChooser.ExtensionFilter("All supported files", "*.jpg", "*.jpeg", "*.png");
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG/JPEG files", "*.jpg", "*.jpeg");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files", "*.png");
        fileChooser.getExtensionFilters().addAll(extFilterAll, extFilterJPG, extFilterPNG);

        //Show open file dialog
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            loadImg(file);
        }
        syncProgressBar();
    }

    private void loadImg(File file) {
        try {
            loadImg(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void loadImg(String path) {
        double requestedSize = Main.stageWidth * 0.6;
        imageView.setImage(new Image(path, requestedSize, requestedSize, true, false));
    }

    @FXML
    public void openFromURL() {
        String url = urlInputField.getText();

        if(isValidURL(url)){
            loadImg(url);
        } else{
            errorWindow("Not a valid URL");
        }
    }

    private boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private void syncProgressBar() {
        if (progressBar == null){
            progressBar = new ProgressBar();
        }

        progressBar.setProgress(100.0 / 200.0);
    }

    private void errorWindow(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR, error, ButtonType.OK);
        alert.showAndWait();

    }
}
