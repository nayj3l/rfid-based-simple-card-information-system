package rfid.scanner;

//import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class RFIDScannerController implements Initializable {
    
    private String INPUT_COLLETOR = "";
    private static boolean _Shown = false;
    private String Collected_Number= "";
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private boolean CustomerFound = false;
    
    @FXML private Button btn_purchase;
    @FXML private Button btn_reg;
    @FXML private Pane pane_reg;
    @FXML private Pane pane_purchase;
    @FXML private TextField fld_card_number;
    @FXML private AnchorPane pane_body;
    @FXML private Pane pane_home;
    @FXML private TextField fld_customer_name;
    @FXML private Button btn_addAmount;
    @FXML private Button btn_reduceAmount;
    @FXML private Button btn_register;
    @FXML private TextField fld_customer_amount;
    @FXML private TextField fld_amount;
    @FXML private Label lbl_status;
//    @FXML private FontAwesomeIconView close;
    @FXML private TextField fld_reg_card_number;
    @FXML private TextField fld_reg_name;
    @FXML private TextField fld_reg_amount;
    @FXML private Label lbl_reg_status;
    @FXML private Label lbl_reg_error;
    @FXML private Button btn_reg_clear;
//    @FXML private FontAwesomeIconView close_reg;
    @FXML private Button btn_purchase_clear;
    @FXML private Label lbl_purchase_error;
    @FXML private TableColumn<Table_Users, String> col_cardNumber;
    @FXML private TableColumn<Table_Users, String> col_name;
    @FXML private TableColumn<Table_Users, String> col_amount;
    @FXML private TableView<Table_Users> tbl_user;
    @FXML private TextField fld_search;
    @FXML private Button btn_delete;
    @FXML private Pane pane_side;
    @FXML private Button btn_home;
    @FXML private Separator separator;
  
    @Override 
    public void initialize(URL url, ResourceBundle rb) {
        Listener();
        Design();
        DisplayUsers();
        FilterDisplayUsers();
    }    
    
    private void Design(){
        pane_side.setPrefHeight(pane_side.getPrefHeight()+10);
        
        btn_purchase_clear.setOnMouseEntered(e -> btn_purchase_clear.setStyle("-fx-background-color: wheat; -fx-background-radius: 100;"));
        btn_purchase_clear.setOnMouseExited(e -> btn_purchase_clear.setStyle("-fx-background-color: transparent; -fx-background-radius: 100;"));
//        btn_purchase_clear.setOnMousePressed(e -> { btn_purchase_clear.setStyle("-fx-background-color: red; -fx-background-radius: 100;"); close.setFill(Color.WHEAT); pane_body.requestFocus(); });
//        btn_purchase_clear.setOnMouseClicked(e -> { btn_purchase_clear.setStyle("-fx-background-color: wheat; -fx-background-radius: 100;"); close.setFill(Color.RED); });
//        btn_purchase_clear.setOnMouseReleased(e -> { btn_purchase_clear.setStyle("-fx-background-color: transparent; -fx-background-radius: 100;"); close.setFill(Color.RED); });
        
        btn_reg_clear.setOnMouseEntered(e -> btn_reg_clear.setStyle("-fx-background-color: wheat; -fx-background-radius: 100;"));
        btn_reg_clear.setOnMouseExited(e -> btn_reg_clear.setStyle("-fx-background-color: transparent; -fx-background-radius: 100;"));
//        btn_reg_clear.setOnMousePressed(e -> { btn_reg_clear.setStyle("-fx-background-color: red; -fx-background-radius: 100;"); close_reg.setFill(Color.WHEAT); pane_body.requestFocus(); });
//        btn_reg_clear.setOnMouseClicked(e -> { btn_reg_clear.setStyle("-fx-background-color: wheat; -fx-background-radius: 100;"); close_reg.setFill(Color.RED); });
//        btn_reg_clear.setOnMouseReleased(e -> { btn_reg_clear.setStyle("-fx-background-color: transparent; -fx-background-radius: 100;"); close_reg.setFill(Color.RED); });
        
        btn_purchase.setOnMouseEntered(e -> btn_purchase.setStyle("-fx-background-color: #47B881;"));
        btn_purchase.setOnMouseExited(e -> btn_purchase.setStyle("-fx-background-color: #99DDCC;"));
        btn_purchase.setOnMouseClicked(e -> btn_purchase.setStyle("-fx-background-color: #47B881;"));
        
        btn_home.setOnMouseEntered(e -> btn_home.setStyle("-fx-background-color: #47B881;"));
        btn_home.setOnMouseExited(e -> btn_home.setStyle("-fx-background-color: #99DDCC;"));
        btn_home.setOnMouseClicked(e -> btn_home.setStyle("-fx-background-color: #47B881;"));
        
        btn_reg.setOnMouseEntered(e -> btn_reg.setStyle("-fx-background-color: #47B881;"));
        btn_reg.setOnMouseExited(e -> btn_reg.setStyle("-fx-background-color: #99DDCC;"));
        btn_reg.setOnMouseClicked(e -> btn_reg.setStyle("-fx-background-color: #47B881;"));
        
        btn_reduceAmount.setOnMousePressed(e ->  pane_body.requestFocus());
        btn_addAmount.setOnMousePressed(e ->  pane_body.requestFocus());
        btn_purchase.setOnMousePressed(e -> { pane_body.requestFocus(); btn_purchase.setStyle("-fx-background-color: #00783E;");});
        btn_reg.setOnMousePressed(e -> { pane_body.requestFocus(); btn_reg.setStyle("-fx-background-color: #00783E;"); });
        btn_register.setOnMousePressed(e -> pane_body.requestFocus());
        btn_home.setOnMousePressed(e -> pane_body.requestFocus());
    }
    
    private void KeyboardInput(KeyEvent event){
        if(event.getCode() != KeyCode.ENTER) {
            INPUT_COLLETOR = INPUT_COLLETOR.concat(event.getText());
            Collected_Number = INPUT_COLLETOR;
            fld_card_number.setText(INPUT_COLLETOR);
            fld_reg_card_number.setText(INPUT_COLLETOR);
        }
    }
    
    private void DisplayUsers(){
        ObservableList<Table_Users> list = FXCollections.observableArrayList();
        try {
            con = getDBConnection();
            pst = con.prepareStatement("SELECT * FROM `users` ORDER BY `amount` DESC");
            rs = pst.executeQuery();
            while(rs.next()){
                list.add(new Table_Users(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
            con.close();
            col_cardNumber.setCellValueFactory(new PropertyValueFactory<>("Card_Number"));
            col_name.setCellValueFactory(new PropertyValueFactory<>("Name"));
            col_amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            tbl_user.setItems(list);
        } catch (SQLException ex) {
            System.out.println("Error in DisplayUsers");
        }
    }
    
    private void FilterDisplayUsers(){
        ObservableList data =  tbl_user.getItems();
        fld_search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length())) {
                tbl_user.setItems(data);
            }
            String value = newValue.toLowerCase(); // yung iniinput sa search box
            ObservableList<Table_Users> subentries = FXCollections.observableArrayList();
            long count = tbl_user.getColumns().stream().count(); // number of columns
            for (int i = 0; i < tbl_user.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = "" + tbl_user.getColumns().get(j).getCellData(i);
                    if (entry.toLowerCase().contains(value)) {
                        subentries.add(tbl_user.getItems().get(i));
                        break;
                    }
                }
            }
            tbl_user.setItems(subentries);
        });
    }
    
    private void DisplayCustomer(){
        try {
            con = getDBConnection();
            pst = con.prepareStatement("SELECT * FROM `users` WHERE Card_number = ?");
            pst.setString(1, fld_card_number.getText());
            rs = pst.executeQuery();
            if(rs.next()){
                fld_customer_name.setText(rs.getString(2));
                fld_customer_amount.setText(rs.getString(3));
                CustomerFound = true;
            }
            else {
                fld_customer_name.clear();
                fld_customer_amount.clear();
                CustomerFound = false;
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error in DisplayCustomer\n" + ex);
        }
    }
    
    private void Listener(){
        pane_body.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown) {
//                System.out.println("Shown: " + onShown);
//                System.out.println("Hidden: " + onHidden);
                if(onShown){
                    _Shown = onShown;
                    lbl_reg_status.setText("Accepting Card Number");
                    lbl_reg_status.setTextFill(Color.web("#03fc03"));
                    lbl_status.setText("Accepting Card Number");
                    lbl_status.setTextFill(Color.web("#03fc03"));
                }
                if(onHidden){
                    _Shown = false;
                    lbl_status.setText("Not accepting card number");
                    lbl_status.setTextFill(Color.RED);
                    lbl_reg_status.setText("Not accepting card number");
                    lbl_reg_status.setTextFill(Color.RED);
                }
            }
        });
    }
    
    private Connection getDBConnection(){
        try {            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/rfid","root","");
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error in DBConnection()\n" + ex);
        }
        return con;
    }
    
    private void Register(){
        try {
            con = getDBConnection();
            pst = con.prepareStatement("INSERT INTO `users` (`Card_number`, `name`, `amount`) "
                    + "VALUES (?, ?, ?);");
            pst.setString(1, Collected_Number);
            pst.setString(2, fld_reg_name.getText());
            pst.setString(3, fld_reg_amount.getText());
            if(pst.executeUpdate()>0){
                RFIDScanner.show("Message", "Registered Successfully!", Alert.AlertType.INFORMATION);
                DisplayUsers();
                Clear();
            }
            else {
                RFIDScanner.show("Error", "Error occured in register", Alert.AlertType.INFORMATION);
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error registering\n" + ex);
        }
    }
    
    private void Clear(){
        fld_card_number.clear();
        fld_customer_name.clear();
        fld_customer_amount.clear();
        fld_amount.clear();
        INPUT_COLLETOR = "";
        fld_reg_name.clear();
        pane_body.requestFocus();
        fld_reg_amount.clear();
        fld_reg_card_number.clear();
        lbl_reg_error.setText(null);
        lbl_purchase_error.setText(null);
    }
    
    @FXML private void pane_body_handleKeyAction(KeyEvent event) {
        if(_Shown && event.getText().matches("[0-9]+")){
            KeyboardInput(event);
            DisplayCustomer();
            lbl_reg_error.setText(null);
        }
        if(event.getCode() == KeyCode.ENTER) {
            INPUT_COLLETOR = "";
        }
    } // Main Body is focused

    @FXML private void btn_home_setOnAction(ActionEvent event) {
        pane_home.toFront();
        separator.setLayoutY(136);
    } // User Home Page Button
    
    @FXML private void btn_purchase_setOnMouseClicked(ActionEvent event) {
        pane_purchase.toFront();
        pane_body.requestFocus();
        separator.setLayoutY(191);
        Clear();
    } // Purchase Home Page Button

    @FXML private void btn_reg_setOnMouseClicked(ActionEvent event) {
        pane_reg.toFront();
        separator.setLayoutY(246);
        Clear();
    } // Register Home Page Button
    
    @FXML private void btn_addAmount_SetOnAction(ActionEvent event) {
        pane_body.requestFocus();
        DisplayCustomer();
        if(CustomerFound){
            try {
                if(fld_amount.getText().matches("[0-9]+")){
                    con = getDBConnection();
                    pst = con.prepareStatement("UPDATE `users` SET `amount` = ? WHERE `Card_number` = ?;");
                    pst.setDouble(1, Double.valueOf(fld_customer_amount.getText()) + Double.valueOf(fld_amount.getText()));
                    pst.setString(2, Collected_Number);
                    if(pst.executeUpdate()>0){
                        RFIDScanner.show("Message", "Balance successfully updated", Alert.AlertType.INFORMATION);
                        DisplayUsers();
                        Clear();
                    }
                    else {
                        RFIDScanner.show("Error", "Error occured in adding amount", Alert.AlertType.ERROR);
                    }
                }
                con.close();
            } catch (SQLException ex) {
                System.out.println("Error occured when adding amount. ");
            }
        }
    } // Add Amount 

    @FXML private void btn_reduceAmount_SetOnAction(ActionEvent event) {
        pane_body.requestFocus();
        DisplayCustomer();
        if(CustomerFound){
            try {
                if(fld_amount.getText().matches("[0-9]+")){
                    lbl_purchase_error.setText(null);
                    if(Double.valueOf(fld_customer_amount.getText()) < Double.valueOf(fld_amount.getText())){
                        lbl_purchase_error.setText("Expected amount is larger than the balance.");
                    }
                    else {
                        lbl_purchase_error.setText(null);
                        con = getDBConnection();
                        pst = con.prepareStatement("UPDATE `users` SET `amount` = ? WHERE `Card_number` = ?;");
                        pst.setDouble(1,  Double.valueOf(fld_customer_amount.getText()) - Double.valueOf(fld_amount.getText()));
                        pst.setString(2, Collected_Number);
                        if(pst.executeUpdate()>0){
                            RFIDScanner.show("Message", "Balance successfully updated", Alert.AlertType.INFORMATION);
                            DisplayUsers();
                            Clear();
                        }
                        else {
                            RFIDScanner.show("Error", "Error occured in reducing amount", Alert.AlertType.ERROR);
                        } 
                        con.close();
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Error occured when reducing amount. ");
            }
        }
    } // Reduce Amount

    @FXML private void pane_purchase_setMouseClicked(MouseEvent event) {
        pane_body.requestFocus();
    } // Pane Purchase  

    @FXML private void pane_reg_setMouseClicked(MouseEvent event) {
        pane_body.requestFocus();
    } // Pane Register 

    @FXML private void btn_register_setOnAction(ActionEvent event) {
        DisplayCustomer();
        if(!fld_reg_card_number.getText().isEmpty()){
            lbl_reg_error.setText(null);
            if(!CustomerFound){
                lbl_reg_error.setText(null);
                if(!fld_reg_name.getText().isEmpty() && !fld_reg_amount.getText().isEmpty()){
                    Register();
                }
                else {
                    lbl_reg_error.setText("Please fill all the fields");
                }
            }
            else {
                lbl_reg_error.setText("Card number already registered");
            }
        }
        else {
            lbl_reg_error.setText("Please tap your card");   
        }
    } // Register User here

    @FXML private void btn_reg_clear_setOnAction(ActionEvent event) {
        Clear();
    } // Clear Fields in Register Pane

    @FXML private void btn_purchase_clear_SetOnAction(ActionEvent event) {
        Clear();
    } // Clear Purchase Fields

    @FXML private void btn_delete_setOnAction(ActionEvent event) {
        Table_Users selected = tbl_user.getSelectionModel().getSelectedItem();
        RFIDScanner.show("Message", "Are you sure you want to delete " + selected.getName() + "?", Alert.AlertType.CONFIRMATION);
        if(RFIDScanner.go){
            try {
                con = getDBConnection(); 
                pst = con.prepareStatement("DELETE FROM `users` WHERE `Card_number` = ?");
                pst.setString(1, selected.getCard_Number());
                if(pst.executeUpdate()>0){
                    DisplayUsers();
                    RFIDScanner.show("Message", selected.getName() + " deleted successfully!", Alert.AlertType.INFORMATION);
                }
                con.close();
            } catch (SQLException ex) {
                System.out.println("error deleting\n" + ex);
            }
        }
    } // Delete from users

}