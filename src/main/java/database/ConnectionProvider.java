package database;

import org.dizitart.no2.Nitrite;

public class ConnectionProvider {
  private static Nitrite db;



    public ConnectionProvider() {
    }

    public static Nitrite getConnection(){
        if(db==null || db.isClosed()){
            db = Nitrite.builder()
                    .compressed()
                    .filePath("trans.dat")
                    .openOrCreate("tuski", "test12");
            System.out.println("Successfully created");
        }

        return db;
    }


}
