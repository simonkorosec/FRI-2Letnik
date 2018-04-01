import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private static final String TMP_DIR = ".randomTestFolder";

    public MenuItem MenuFileOpen;
    public ImageView imageView;
    public TextField urlInputField;
    public ProgressBar progressBar;
    public Label labelUsedStorage;
    public TextArea inputArea;
    public MenuItem MenuExitButton;
    public ListView<String> attachmentsListView;
    public ListView<ImageView> imageListView;
    public TextArea textAreaShowMessage;

    private ArrayList<SteganographicImage> steganographicImages;
    private ArrayList<Image> images;
    private ArrayList<File> files;
    private SteganographicImage stgImage;
    private int maxStored;
    private int used;
    private File openedFile = null;

    @FXML
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

    @FXML
    public void changeCurrentImage() {

        int index = imageListView.getSelectionModel().getSelectedIndex();

        if (index == -1){
            return;
        }

        save();
        stgImage = steganographicImages.get(index);
        imageView.setImage(images.get(index));
        centerImage();
        openedFile = files.get(index);

        sync();
    }

    @FXML
    public void deleteAttachment() {
        String selected = getSelectedAttachment();

        if (selected == null) {
            errorWindow("No attachment selected.");
            return;
        }
        if (openedFile == null) {
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

        sync();
    }

    @FXML
    public void deleteMessage() {
        if (stgImage != null) {
            setMessage("");
            Alert alert = new Alert(Alert.AlertType.NONE, "Message cleared.", ButtonType.OK);
            alert.showAndWait();

        } else {
            errorWindow("No image opened. Please open an image.");
        }
    }

    @FXML
    public void exitProgram() {
        if (stgImage != null) {
            String msg = "Do you wish to save before you exit? All unsaved progress will be lost!";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                saveAll();
            }
        }
        Platform.exit();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        steganographicImages = new ArrayList<>();
        images = new ArrayList<>();
        files = new ArrayList<>();
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
            steganographicImages.add(stgImage);
            files.add(openedFile);

            loadImg(openedFile);
            sync();
        } catch (IOException e) {
            errorWindow("Sorry there was a problem with opening the image. :(");
        } catch (NullPointerException e) {
            // User closed the chose file window do nothing but still have to catch the Exception
        }

    }

    @FXML
    public void openFromURL() {
        String url = urlInputField.getText();

        if (isValidURL(url)) {
            try {
                stgImage = SteganographicImage.loadFromUrl(url);
                loadImg(url);
                files.add(null);
                openedFile = null;

                sync();
            } catch (IOException e) {
                errorWindow("Sorry couldn't access the given URL.");
            }
        } else {
            errorWindow("Not a valid URL");
        }
    }

    @FXML
    public void resetImage() {
        if (stgImage != null) {
            stgImage.clear();
            sync();
        } else {
            errorWindow("No image opened. Please open an image.");
        }

    }

    @FXML
    public void save() {
        if (openedFile == null) {
            saveAs();
            return;
        }

        String name = openedFile.getName();
        String ext = name.substring(name.lastIndexOf('.') + 1, openedFile.getName().length());

        if (ext.toLowerCase().equals("png")) {
            save(openedFile.getAbsolutePath());
        } else {
            saveAs();
        }
    }

    @FXML
    public void saveAll() {

        for (int i = 0; i < steganographicImages.size(); i++) {
            stgImage = steganographicImages.get(i);
            openedFile = files.get(i);
            save();
        }

    }

    @FXML
    public void saveAs() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files", "*.png");
        fileChooser.getExtensionFilters().addAll(extFilterPNG);

        try {
            String name = openedFile.getName();
            name = name.substring(0, name.lastIndexOf('.')) + ".png";
            fileChooser.setInitialFileName(name);
        } catch (NullPointerException e) {
            // pass
        }

        //Show save file dialog
        File saveTo = fileChooser.showSaveDialog(null);

        if (saveTo != null) {
            save(saveTo.getAbsolutePath());
        }

    }

    @FXML
    public void saveAttachment() {
        String selected = getSelectedAttachment();

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

    @FXML
    public void setMessage() {
        if (stgImage != null) {
            setMessage(inputArea.getText());
        } else {
            errorWindow("No image opened. Please open an image.");
        }
    }

    @FXML
    public void showAbout() {
        String about = "Steganography is the practice of concealing a file, message, image, or video within another file, message, image, or video. \nCreated by: Simon Korošec";

        Alert alert = new Alert(Alert.AlertType.NONE, about, ButtonType.OK);
        alert.setTitle("About");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    @FXML
    public void showHelp() {
        String help = "If you require additional help, contact me at sk7196@student.uni-lj.si";

        Alert alert = new Alert(Alert.AlertType.NONE, help, ButtonType.OK);
        alert.setTitle("Help");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    @FXML
    public void showMessage() {
        if (stgImage != null) {
            String msg = stgImage.getMessage();
            textAreaShowMessage.setText(msg);
        } else {
            errorWindow("No image opened. Please open an image.");
        }
    }

    private void errorWindow(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR, error, ButtonType.OK);
        alert.showAndWait();

    }

    private void loadImg(File file) {
        try {
            loadImg(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException | NullPointerException e) {
            errorWindow("Uppss there was an error opening the image :(");
        }
    }

    private void loadImg(String path) {
        Image img = new Image(path);
        imageView.setImage(img);
        centerImage();

        images.add(img);

        maxStored = stgImage.getTotalStorage();
        used = stgImage.getUsedStorage();
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
            maxStored = stgImage.getTotalStorage();

            double d = (double) used / (double) maxStored;
            progressBar.setProgress(d);
            labelUsedStorage.setText(String.format("%d / %d", used, maxStored));
        }
    }

    private void setMessage(String msg) {
        try {
            stgImage.setMessage(msg);
            sync();
        } catch (SteganographicImage.SteganographicImageException e) {
            errorWindow(e.getMessage());
        }
    }

    private void save(String filename) {
        try {
            stgImage.saveToFile(filename);
        } catch (IOException e) {
            errorWindow("There was an error trying save the image.");
        }
    }

    private void listAttachments() {
        attachmentsListView.getItems().clear();

        String[] attachments = stgImage.listAttachments();

        for (String attachment : attachments) {
            attachmentsListView.getItems().add(attachment);
        }
    }

    private String getSelectedAttachment() {
        return attachmentsListView.getSelectionModel().getSelectedItem();
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

    private void centerImage() {
        Image img = imageView.getImage();
        if (img != null) {
            double w;
            double h;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff;
            if (ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);

        }
    }

    private void updateImageListView() {
        imageListView.getItems().clear();

        for (Image img : images) {
            ImageView tmp = new ImageView();
            tmp.setImage(img);
            tmp.setFitWidth(90);
            tmp.setFitHeight(90);
            tmp.setPreserveRatio(true);

            imageListView.getItems().add(tmp);
        }

    }

    private void sync() {
        syncProgressBar();
        listAttachments();
        updateImageListView();
    }

}
