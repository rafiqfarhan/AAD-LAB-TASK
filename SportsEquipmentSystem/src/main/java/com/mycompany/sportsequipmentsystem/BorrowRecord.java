package com.mycompany.sportsequipmentsystem;

import java.time.LocalDate;

public class BorrowRecord {
    private int id;
    private String studentName;
    private String studentId;
    private String equipmentName;
    private String equipmentCategory;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String equipmentCondition;
    private String borrowingStatus;

    public BorrowRecord(int id, String studentName, String studentId, String equipmentName, 
                        String equipmentCategory, LocalDate borrowDate, LocalDate returnDate, 
                        String equipmentCondition, String borrowingStatus) {
        this.id = id;
        this.studentName = studentName;
        this.studentId = studentId;
        this.equipmentName = equipmentName;
        this.equipmentCategory = equipmentCategory;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.equipmentCondition = equipmentCondition;
        this.borrowingStatus = borrowingStatus;
    }

    public int getId() { return id; }
    public String getStudentName() { return studentName; }
    public String getStudentId() { return studentId; }
    public String getEquipmentName() { return equipmentName; }
    public String getEquipmentCategory() { return equipmentCategory; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getEquipmentCondition() { return equipmentCondition; }
    public String getBorrowingStatus() { return borrowingStatus; }
}