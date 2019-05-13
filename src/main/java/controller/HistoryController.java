package controller;

import database.DBConnectionProvider;
import org.dizitart.no2.*;

import javax.swing.text.TabableView;

public class HistoryController {

    TabableView tabableView;

    public HistoryController(TabableView tabableView) {
        this.tabableView = tabableView;
    }


    private void populateTable() {
        Nitrite db = DBConnectionProvider.getConnection();
        NitriteCollection collection = db.getCollection("test");

        Cursor cursor = collection.find(FindOptions.sort("id", SortOrder.Descending));
        for (Document document : cursor) {
            System.out.println(document.get("from"));
            System.out.println(document.get("to"));
        }


    }


}
