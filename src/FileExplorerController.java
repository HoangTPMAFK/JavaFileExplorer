import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class FileExplorerController {

    public static ArrayList <String> pathList = new ArrayList<>();
    private Path rootPath;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane panel;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private TextArea textArea;

    @FXML
    private Menu editMenu;

    @FXML
    private Menu fileMenu;

    @FXML
    private Menu helpMenu;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem openBtn;

    @FXML
    private MenuItem saveBtn;

    private List<String> getAllSelected() {
        return treeView.getSelectionModel().getSelectedItems()
            .stream()
            .map(TreeItem::getValue)
            .collect(Collectors.toList());
    }
    @FXML
    void selectItem(MouseEvent event) throws IOException {
        String selectedFilePath = getAllSelected().get(0);
        if (System.getProperty("os.name").indexOf("Windows") == -1) {
            for (String objString : FileExplorerController.pathList) {
                if (objString.indexOf(selectedFilePath) != -1 
                    && objString.indexOf(selectedFilePath + "/") == -1 
                    && !(new File(objString)).isDirectory()) {
                    File file = new File(objString);
                    InputStream in = new FileInputStream(file);
                    byte b[] = in.readAllBytes();
                    String fileContent = "";
                    for (byte i : b) {
                        fileContent += (char) i;
                    }
                    textArea.setText(fileContent);
                    in.close();
                    break;
                }
            }
        } else {
            for (String objString : FileExplorerController.pathList) {
                if (objString.indexOf(selectedFilePath) != -1 
                    && objString.indexOf(selectedFilePath + "\\") == -1 
                    && !(new File(objString)).isDirectory()) {
                    // System.out.println(objString);
                    File file = new File(objString);
                    InputStream in = new FileInputStream(file);
                    byte b[] = in.readAllBytes();
                    String fileContent = "";
                    for (byte i : b) {
                        fileContent += (char) i;
                    }
                    textArea.setText(fileContent);
                    in.close();
                    break;
                }
            }
        } 
    }
    @FXML
    void initialize() {
        saveBtn.setOnAction(event -> {
            String selectedFilePath = getAllSelected().get(0);
            for (String objString : FileExplorerController.pathList) {
                if (objString.indexOf(selectedFilePath) != -1
                    && objString.indexOf(selectedFilePath + "/") == -1
                    && !(new File(objString)).isDirectory()) {
                    File file = new File(objString);
                    try {
                        OutputStream out = new FileOutputStream(file);
                        byte b[] = textArea.getText().getBytes();
                        out.write(b);
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        });
        openBtn.setOnAction(event -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.showOpenDialog(null);
            File file = fileChooser.getSelectedFile();
            rootPath = Paths.get(file.getAbsolutePath());
            TreeItem <String> root = new TreeItem<>(rootPath.getFileName().toString());
            treeView.setRoot(root);
            List <Path> files = new ArrayList<>();
            listAllFiles(root, rootPath, files);
        });
    }

    // @FXML
    // void saveFile(ActionEvent event) throws IOException {
    //     String selectedFilePath = getAllSelected().get(0);
    //     for (String objString : FileExplorerController.pathList) {
    //         if (objString.indexOf(selectedFilePath) != -1
    //             && objString.indexOf(selectedFilePath + "/") == -1
    //             && !(new File(objString)).isDirectory()) {
    //             File file = new File(objString);
    //             OutputStream out = new FileOutputStream(file);
    //             byte b[] = textArea.getText().getBytes();
    //             out.write(b);
    //             out.close();
    //             break;
    //         }
    //     }
    // }

    public static void listAllFiles(TreeItem <String> root, Path path, List<Path> files) {
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(path);
            for (Path entry : stream) {
                TreeItem <String> item = new TreeItem<>(entry.getFileName().toString());
                FileExplorerController.pathList.add(entry.toAbsolutePath().toString());
                root.getChildren().add(item);
                if (Files.isDirectory(entry)) { 
                    listAllFiles(item, entry, files); 
                } else { 
                    files.add(entry);
                } 
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @FXML
    // void openFolder(ActionEvent event) {
    //     JFileChooser fileChooser = new JFileChooser();
    //     fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    //     fileChooser.showOpenDialog(null);
    //     File file = fileChooser.getSelectedFile();
    //     rootPath = Paths.get(file.getAbsolutePath());
    //     TreeItem <String> root = new TreeItem<>(rootPath.getFileName().toString());
    //     treeView.setRoot(root);
    //     List <Path> files = new ArrayList<>();
    //     listAllFiles(root, rootPath, files);
    // }
}
