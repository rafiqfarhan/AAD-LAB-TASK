package com.mycompany.sportsequipmentsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;

public class PrimaryController {

    @FXML private TextField txtStudentName, txtStudentId, txtEquipmentName, txtSearch;
    @FXML private ComboBox<String> cbCategory, cbCondition, cbStatus;
    @FXML private DatePicker dpBorrowDate, dpReturnDate;
    
    @FXML private TableView<BorrowRecord> tableView;
    @FXML private TableColumn<BorrowRecord, Integer> colId;
    @FXML private TableColumn<BorrowRecord, String> colStudentName, colStudentId, colEquipmentName, colCategory, colCondition, colStatus;
    @FXML private TableColumn<BorrowRecord, LocalDate> colBorrowDate, colReturnDate;

    private ObservableList<BorrowRecord> recordList = FXCollections.observableArrayList();
    private int selectedId = -1;

    @FXML
    public void initialize() {
        cbCategory.setItems(FXCollections.observableArrayList("Balls", "Rackets", "Athletics", "Gymnastics", "Protective Gear"));
        cbCondition.setItems(FXCollections.observableArrayList("Good", "Fair", "Slightly Damaged"));
        cbStatus.setItems(FXCollections.observableArrayList("Borrowed", "Returned", "Overdue"));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colEquipmentName.setCellValueFactory(new PropertyValueFactory<>("equipmentName"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("equipmentCategory"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colCondition.setCellValueFactory(new PropertyValueFactory<>("equipmentCondition"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("borrowingStatus"));

        loadData();

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedId = newSelection.getId();
                txtStudentName.setText(newSelection.getStudentName());
                txtStudentId.setText(newSelection.getStudentId());
                txtEquipmentName.setText(newSelection.getEquipmentName());
                cbCategory.setValue(newSelection.getEquipmentCategory());
                dpBorrowDate.setValue(newSelection.getBorrowDate());
                dpReturnDate.setValue(newSelection.getReturnDate());
                cbCondition.setValue(newSelection.getEquipmentCondition());
                cbStatus.setValue(newSelection.getBorrowingStatus());
            }
        });
    }

    private void loadData() {
        recordList.clear();
        String query = "SELECT * FROM borrowing_records";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                recordList.add(new BorrowRecord(
                    rs.getInt("id"),
                    rs.getString("student_name"),
                    rs.getString("student_id"),
                    rs.getString("equipment_name"),
                    rs.getString("equipment_category"),
                    rs.getDate("borrow_date").toLocalDate(),
                    rs.getDate("return_date").toLocalDate(),
                    rs.getString("equipment_condition"),
                    rs.getString("borrowing_status")
                ));
            }
            tableView.setItems(recordList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        String query = "INSERT INTO borrowing_records (student_name, student_id, equipment_name, equipment_category, borrow_date, return_date, equipment_condition, borrowing_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, txtStudentName.getText());
            pstmt.setString(2, txtStudentId.getText());
            pstmt.setString(3, txtEquipmentName.getText());
            pstmt.setString(4, cbCategory.getValue());
            pstmt.setDate(5, Date.valueOf(dpBorrowDate.getValue()));
            pstmt.setDate(6, Date.valueOf(dpReturnDate.getValue()));
            pstmt.setString(7, cbCondition.getValue());
            pstmt.setString(8, cbStatus.getValue());

            pstmt.executeUpdate();
            loadData();
            handleReset();
        } catch (Exception e) {
            showAlert("Error", "Please fill in all required fields completely.");
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedId == -1) {
            showAlert("Warning", "Please select a record from the table first.");
            return;
        }
        String query = "UPDATE borrowing_records SET student_name=?, student_id=?, equipment_name=?, equipment_category=?, borrow_date=?, return_date=?, equipment_condition=?, borrowing_status=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, txtStudentName.getText());
            pstmt.setString(2, txtStudentId.getText());
            pstmt.setString(3, txtEquipmentName.getText());
            pstmt.setString(4, cbCategory.getValue());
            pstmt.setDate(5, Date.valueOf(dpBorrowDate.getValue()));
            pstmt.setDate(6, Date.valueOf(dpReturnDate.getValue()));
            pstmt.setString(7, cbCondition.getValue());
            pstmt.setString(8, cbStatus.getValue());
            pstmt.setInt(9, selectedId);

            pstmt.executeUpdate();
            loadData();
            handleReset();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedId == -1) {
            showAlert("Warning", "Please select a record from the table first.");
            return;
        }
        String query = "DELETE FROM borrowing_records WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, selectedId);
            pstmt.executeUpdate();
            loadData();
            handleReset();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReset() {
        txtStudentName.clear();
        txtStudentId.clear();
        txtEquipmentName.clear();
        cbCategory.setValue(null);
        dpBorrowDate.setValue(null);
        dpReturnDate.setValue(null);
        cbCondition.setValue(null);
        cbStatus.setValue(null);
        selectedId = -1;
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }

        recordList.clear();
        String query = "SELECT * FROM borrowing_records WHERE student_id LIKE ? OR equipment_name LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                recordList.add(new BorrowRecord(
                    rs.getInt("id"),
                    rs.getString("student_name"),
                    rs.getString("student_id"),
                    rs.getString("equipment_name"),
                    rs.getString("equipment_category"),
                    rs.getDate("borrow_date").toLocalDate(),
                    rs.getDate("return_date").toLocalDate(),
                    rs.getString("equipment_condition"),
                    rs.getString("borrowing_status")
                ));
            }
            tableView.setItems(recordList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}