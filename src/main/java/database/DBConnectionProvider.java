package database;

import org.dizitart.no2.Nitrite;

public class DBConnectionProvider {
    private static Nitrite db;

    public DBConnectionProvider() {
    }

    public static Nitrite getConnection() {
        if (db == null || db.isClosed()) {
            db = Nitrite.builder()
                    .compressed()
                    .filePath("trans.dat")
                    .openOrCreate("tuski", "test12");
        }

        return db;
    }


}
