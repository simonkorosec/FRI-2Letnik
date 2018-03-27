package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Controller {

    public MenuItem fileOpen;
    public ImageView imageView;
    public TextField urlInputField;
    public ProgressBar progressBar;
    public Label labelUsedStorage;
    public TextArea inputArea;
    public MenuItem exitButton;
    public ListView attachmentsListView;

    private SteganographicImage stgImage;
    private int maxStored;
    private int used;
    private File openedFile = null;

    private void errorWindow(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR, error, ButtonType.OK);
        alert.showAndWait();

    }

    public void exitProgram() {
        if (stgImage != null) {
            String msg = "Do you wish to exit? All unsaved progress will be lost!";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                Platform.exit();
            }
        }
        Platform.exit();
    }

    @FXML
    public void openFromFile() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterAll = new FileChooser.ExtensionFilter("All supported files", "*.jpg", "*.jpeg", "*.png");
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG/JPEG files", "*.jpg", "*.jpeg");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files", "*.png");
        fileChooser.getExtensionFilters().addAll(extFilterAll, extFilterJPG, extFilterPNG);

        //Show open file dialog
        openedFile = fileChooser.showOpenDialog(null);
        try {
            stgImage = SteganographicImage.loadFromFile(openedFile.getAbsolutePath());
            loadImg(openedFile);
            syncProgressBar();
        } catch (IOException e) {
            errorWindow("Sorry there was a problem with opening the image. :(");
            return;
        } catch (NullPointerException e) {
            // Pass
        }

    }

    private void loadImg(File file) {
        try {
            loadImg(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Pass
        }
    }

    private void loadImg(String path) {
        imageView.setImage(new Image(path));
        maxStored = stgImage.getTotalStorage();
        used = stgImage.getUsedStorage();

    }

    @FXML
    public void openFromURL() {
        String url = urlInputField.getText();

        if (isValidURL(url)) {
            try {
                stgImage = SteganographicImage.loadFromUrl(url);
            } catch (IOException e) {
                errorWindow("Sorry couldn't access the given URL.");
                return;
            }

            loadImg(url);

            syncProgressBar();

        } else {
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
        if (progressBar == null) {
            progressBar = new ProgressBar();
        }

        if (stgImage != null) {
            used = stgImage.getUsedStorage();
            double d = (double) used / (double) maxStored;
            progressBar.setProgress(d);
            labelUsedStorage.setText(String.format("%d / %d", used, maxStored));
        }
    }

    @FXML
    public void setMessage() {
        if (stgImage != null) {
            setMessage(inputArea.getText());
        } else {
            errorWindow("No image opened. Please open an image.");
        }
    }

    @FXML
    public void addMessage() {
        if (stgImage != null) {
            String msg = stgImage.getMessage();
            if (!msg.equals("")) {
                msg += "\n";
            }

            msg += inputArea.getText();
            setMessage(msg);
        } else {
            errorWindow("No image opened. Please open an image.");
        }
    }

    private void setMessage(String msg) {
        try {
            stgImage.setMessage(msg);
            syncProgressBar();
        } catch (SteganographicImage.SteganographicImageException e) {
            errorWindow(e.getMessage());
        }
    }

    @FXML
    public void showMessage() {
        if (stgImage != null) {
            String msg = stgImage.getMessage();
            Alert alert = new Alert(Alert.AlertType.NONE, msg, ButtonType.OK);
            alert.showAndWait();

        } else {
            errorWindow("No image opened. Please open an image.");
        }
    }

    @FXML
    public void clearMessage() {
        if (stgImage != null) {
            setMessage("");
            Alert alert = new Alert(Alert.AlertType.NONE, "Message cleared.", ButtonType.OK);
            alert.showAndWait();

        } else {
            errorWindow("No image opened. Please open an image.");
        }

    }

    private void save(String filename) {
        try {
            stgImage.saveToFile(filename);
        } catch (IOException e) {
            errorWindow("There was an error trying save the image.");
        }
    }

    public void save() {
        String name = openedFile.getName();
        String ext = name.substring(name.lastIndexOf('.') + 1, openedFile.getName().length());

        if (ext.toLowerCase().equals("png")) {
            save(openedFile.getAbsolutePath());
        } else {
            saveAs();
        }
    }

    public void saveAs() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files", "*.png");
        fileChooser.getExtensionFilters().addAll(extFilterPNG);

        //Show save file dialog
        File saveTo = fileChooser.showSaveDialog(null);

        if (saveTo != null) {
            save(saveTo.getAbsolutePath());
        }

    }

    public void clearTextArea() {
        inputArea.clear();
    }


    public void addAttachment() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
//        FileChooser.ExtensionFilter extFilterAll = new FileChooser.ExtensionFilter("All supported files", "*.jpg", "*.jpeg", "*.png");
//        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG/JPEG files", "*.jpg", "*.jpeg");
//        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files", "*.png");
//        fileChooser.getExtensionFilters().addAll(extFilterAll, extFilterJPG, extFilterPNG);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        try {
            if (stgImage != null){
                stgImage.addAttachment(file.getAbsolutePath());
            } else {
                errorWindow("Open an image before adding an attachment");
            }

        } catch (IOException e) {
            errorWindow("Sorry there was a problem with opening the attachment.");
        } catch (NullPointerException e) {
            // Pass
        } catch (SteganographicImage.SteganographicImageException e) {
            errorWindow("Sorry there was a problem with adding the attachment.");
        }

    }

    private void listAttachments() {
        String[] attachments = stgImage.listAttachments();

        for (String attachment : attachments) {
            attachmentsListView.getItems().add(attachment);
        }
    }
}
