package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class Controller  implements  Initializable{

    @FXML private TableView<wordCount> table;
    @FXML private TableColumn<wordCount,String> Word;
    @FXML private TableColumn<wordCount,Integer> Frequency;

    Map<String,Integer>map=new HashMap<>();

    public void browseFile(){

        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text File","*.txt"));
        File file=fileChooser.showOpenDialog(null);
        if(file==null) return;

        String addWord;
        map.clear();

        try(Scanner scanner=new Scanner(file)){

            scanner.useDelimiter("[^A-Za-z0-9']+");
            while(scanner.hasNext()){
                addWord=scanner.next().toLowerCase();
                try{
                    map.put(addWord,map.get(addWord)+1);
                }catch (NullPointerException n){
                    map.put(addWord,1);
                }
            }

        }catch (FileNotFoundException e){
            System.out.println("File Not Found!");
        }
    }

    public void updateTable(){
        ObservableList<wordCount>wordList=FXCollections.observableArrayList();
        map.keySet().forEach((s)->{
            wordList.add(new wordCount(s,map.get(s)));
        });
        table.setItems(wordList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Word.setCellValueFactory(new PropertyValueFactory<>("word"));
        Frequency.setCellValueFactory(new PropertyValueFactory<>("count"));
    }
}
