/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package search.Controller;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import search.search;
/**
 *
 * @author rylle
 */
public class DBController {
    private static String user;
    private static String pass;
    private static String host;
    private static String db;
    private static Connection connection;
    public DBController(String user,String pass,String host,String db) throws SQLException {
        this.user = user;
        this.pass = pass;
        this.host = host;
        this.db = db;
        connection = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+db, user, pass);
    }
    
    public  void db_connect() throws SQLException{
        /*TEST CONNECTION*/
        Connection test_connect = connection;
        try {
            String test_query = "SELECT * FROM `tbl_stock`";
            Statement test_stmnt = test_connect.createStatement();
            test_stmnt.execute(test_query);
            test_stmnt.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    
    public ArrayList getAllModel() throws SQLException{
        /*GET ALL CAR MODEL*/
        Connection model_con = connection;
        try {
            ArrayList<String> car_model=new ArrayList<>();
            String model_query = "SELECT * FROM `tbl_car`";
            Statement model_stmnt = model_con.createStatement();
            ResultSet model_rs = model_stmnt.executeQuery(model_query);
            while (model_rs.next()) {                
                car_model.add(model_rs.getString("car_model"));
            }
            model_rs.close();
            model_stmnt.close();
            return car_model;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
    }
    
    public ArrayList getAllCategory() throws SQLException{
        Connection category_con = connection;
        try {
            ArrayList<String> category = new ArrayList<>();
            String category_query = "SELECT * FROM `tbl_category`";
            Statement category_stmnt = category_con.createStatement();
            ResultSet category_rs = category_stmnt.executeQuery(category_query);
            while(category_rs.next()){
                category.add(category_rs.getString("cat_name"));
            }
            category_rs.close();
            category_stmnt.close();
            return category;
        } catch (SQLException e) {
            return null;
        }
    }
    
    public ArrayList searchFilter(String carModel,int carYear,String category) throws SQLException{
        Connection searchFilter_con = connection;
        if(carModel.equals("ALL")){
            carModel = "%";
        }
        if (category.equals("ALL")) {
            category = "%";
        }
        try {
            ArrayList<Object[]> searchFilter = new ArrayList<>();
            String searchFilter_query = """
                                        SELECT * FROM `tbl_stock`
                                        INNER JOIN tbl_car ON stock_model = tbl_car.car_id
                                        INNER JOIN tbl_category ON stock_category = tbl_category.cat_id
                                        INNER JOIN tbl_supplier ON stock_supplier = tbl_supplier.supp_id
                                        INNER JOIN tbl_brand ON stock_brand = tbl_brand.brand_id
                                        WHERE tbl_car.car_model LIKE "%s"
                                        AND tbl_category.cat_name LIKe "%s"
                                        AND tbl_stock.stock_year LIKE %d;""";
            Statement searchFilter_stmnt = searchFilter_con.createStatement();
            String formatted = searchFilter_query.formatted(carModel,category,carYear);
            ResultSet searchFilter_rs = searchFilter_stmnt.executeQuery(formatted);
            if(searchFilter_rs.next()){
                searchFilter.add(new Object[]{
                    searchFilter_rs.getInt("stock_id"),
                    searchFilter_rs.getString("supp_name"),
                    searchFilter_rs.getInt("stock_partno"),
                    searchFilter_rs.getString("brand_name"),
                    searchFilter_rs.getString("cat_name"),
                    searchFilter_rs.getInt("stock_updateStock"),
                    searchFilter_rs.getString("car_model"),
                    searchFilter_rs.getInt("stock_year"),
                    searchFilter_rs.getString("stock_status"),});
                while (searchFilter_rs.next()) {
                    searchFilter.add(new Object[]{
                    searchFilter_rs.getInt("stock_id"),
                    searchFilter_rs.getString("supp_name"),
                    searchFilter_rs.getInt("stock_partno"),
                    searchFilter_rs.getString("brand_name"),
                    searchFilter_rs.getString("cat_name"),
                    searchFilter_rs.getInt("stock_updateStock"),
                    searchFilter_rs.getString("car_model"),
                    searchFilter_rs.getInt("stock_year"),
                    searchFilter_rs.getString("stock_status"),});
                }
                searchFilter_rs.close();
                searchFilter_stmnt.close();
                return searchFilter;
            }else{
                JOptionPane.showMessageDialog(new search(), "No Data Found!");
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
