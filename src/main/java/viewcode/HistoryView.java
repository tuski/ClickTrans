package viewcode;

import database.ConnectionProvider;
import org.dizitart.no2.*;

import javax.swing.text.TabableView;

public class HistoryView {

    TabableView tabableView;

    public HistoryView(TabableView tabableView) {
        this.tabableView = tabableView;
    }


    private void populateTable(){
        Nitrite db= ConnectionProvider.getConnection();
        NitriteCollection collection = db.getCollection("test");

        Cursor cursor = collection.find(FindOptions.sort("id",SortOrder.Descending));
        for (Document document : cursor) {
            System.out.println(document.get("from"));
            System.out.println(document.get("to"));
        }


    }



}
