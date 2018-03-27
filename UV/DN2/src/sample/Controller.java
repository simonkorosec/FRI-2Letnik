package sample;

import javafx.application.Platform;
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

    private static final String TMP_DIR = ".randomTestFolder";

    public MenuItem fileOpen;
    public ImageView imageView;
    public TextField urlInputField;
    public ProgressBar progressBar;
    public Label labelUsedStorage;
    public TextArea inputArea;
    public MenuItem exitButton;
    public ListView<String> attachmentsListView;

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
            listAttachments();
        } catch (IOException e) {
            errorWindow("Sorry there was a problem with opening the image. :(");
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
            listAttachments();
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
            alert.setTitle("Message");
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

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        try {
            if (stgImage != null) {
                stgImage.addAttachment(file.getAbsolutePath());
                listAttachments();
                syncProgressBar();
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
        attachmentsListView.getItems().clear();

        String[] attachments = stgImage.listAttachments();

        for (String attachment : attachments) {
            attachmentsListView.getItems().add(attachment);
        }
    }

    public void saveAttachment() {
        String selected = getSelectedItem();

        if (selected == null) {
            errorWindow("No attachment selected.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(selected);

        File file = fileChooser.showSaveDialog(null);
        try {
            stgImage.saveAttachment(selected, file.getAbsolutePath());
        } catch (IOException e) {
            errorWindow("Sorry there was a problem with opening the attachment.");
        } catch (NullPointerException e) {
            // Pass
        } catch (SteganographicImage.SteganographicImageException e) {
            errorWindow("Sorry there was a problem with adding the attachment.");
        }


    }

    private String getSelectedItem() {
        return attachmentsListView.getSelectionModel().getSelectedItem();
    }

    public void deleteAttachment() {
        String selected = getSelectedItem();

        if (selected == null) {
            errorWindow("No attachment selected.");
            return;
        }
        if (openedFile == null) {
            System.out.println("klele");
            return;
        }

        String msg = stgImage.getMessage();
        String[] attachments = stgImage.listAttachments();


        String dir = openedFile.getParent() + File.separator + TMP_DIR;

        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdir();
        }

        try {
            for (String name : attachments) {
                if (!name.equals(selected)) {
                    String filename = dir + File.separator + name;
                    stgImage.saveAttachment(name, filename);
                }
            }
        } catch (IOException | SteganographicImage.SteganographicImageException e) {
            // Pass
        }

        stgImage.clear();
        setMessage(msg);

        File[] listFiles = directory.listFiles();
        try {
            assert listFiles != null;
            for (File f : listFiles) {
                stgImage.addAttachment(f.getAbsolutePath());
            }
        } catch (SteganographicImage.SteganographicImageException | IOException e) {
            // Pass
        }

        deleteDir(directory);
        listAttachments();
        syncProgressBar();
    }

    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    public void showAbout() {
        String about = "Steganography is the practice of concealing a file, message, image, or video within another file, message, image, or video. \nCreated by: Simon Korošec";

        Alert alert = new Alert(Alert.AlertType.NONE, about, ButtonType.OK);
        alert.setTitle("About");
        alert.showAndWait();
    }

    public void showHelp() {
        // TODO create help dialog

        String help = "TODO";

        Alert alert = new Alert(Alert.AlertType.NONE, help, ButtonType.OK);
        alert.setTitle("Help");
        alert.showAndWait();
    }
}
