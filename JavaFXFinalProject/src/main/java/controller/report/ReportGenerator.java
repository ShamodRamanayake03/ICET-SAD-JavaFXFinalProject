
package controller.report;

import entity.OrderEntity;
import entity.ProductEntity;
import entity.SupplierEntity;
import entity.UserEntity;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.util.HashMap;
import java.util.List;

public class ReportGenerator {

    public void generateReceiptReport(OrderEntity order) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport("path/to/receiptReport.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(order));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "path/to/outputReceipt.pdf");
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public void generateInventoryReport(List<ProductEntity> inventoryList) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport("path/to/inventoryReport.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(inventoryList);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "path/to/outputInventory.pdf");
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public void generateEmployeeReport(List<UserEntity> employeeList) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport("path/to/employeeReport.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employeeList);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "path/to/outputEmployee.pdf");
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public void generateSupplierReport(List<SupplierEntity> supplierList) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport("path/to/supplierReport.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(supplierList);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "path/to/outputSupplier.pdf");
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
/**
    public void generateSalesReport(List<SalesData> salesDataList, String reportType) {
        try {
            String reportPath = "";
            if (reportType.equals("Daily")) {
                reportPath = "path/to/dailySalesReport.jrxml";
            } else if (reportType.equals("Monthly")) {
                reportPath = "path/to/monthlySalesReport.jrxml";
            } else if (reportType.equals("Annual")) {
                reportPath = "path/to/annualSalesReport.jrxml";
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(salesDataList);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "path/to/outputSalesReport.pdf");
        } catch (JRException e) {
            e.printStackTrace();
        }
    }**/
}
