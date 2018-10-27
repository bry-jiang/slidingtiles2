package accounts;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Account {
    // replace ArrayList<String> with ArrayList<Specific Board Manager>
    protected HashMap<String, ArrayList<String>> gameSaves = new HashMap<>();

    protected Account() {
        gameSaves.put("sliding_tiles", new ArrayList<>());
    }

    public void saveGame(String gameSave, String game) {
        // replace String gameSave --> GenericBoardManager gameSave
        gameSaves.get(game).add(gameSave);
    };

    // replace ArrayList<String> with ArrayList<Generic Board Manager>
    public ArrayList<String> getGame(String game) {
        return gameSaves.get(game);
    };
}
