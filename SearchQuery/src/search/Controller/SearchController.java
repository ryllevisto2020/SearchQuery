/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package search.Controller;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author rylle
 */
public class SearchController {

    public SearchController() {
    }
    public ArrayList searchFilter(String carModel,int carYear,String category,DBController db) throws SQLException{
        ArrayList<Object[]> searchFilter = db.searchFilter(carModel, carYear, category);
        return searchFilter;
    }
}
