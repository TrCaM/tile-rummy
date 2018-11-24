package project.rummy.behaviors;

import project.rummy.ai.CombinationSeeker;
import project.rummy.ai.HandMeldSeeker;
import project.rummy.ai.SmartStateAnalyzer;
import project.rummy.ai.TableMeldSeeker;
import project.rummy.commands.Command;
import project.rummy.control.ActionHandler;
import project.rummy.entities.ManipulationTable;
import project.rummy.entities.Meld;
import project.rummy.entities.MeldType;
import project.rummy.entities.Tile;
import project.rummy.game.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayOneTileMoveMaker implements ComputerMoveMaker {
    private boolean shouldAnalyzeTable;
    private SmartStateAnalyzer analyzer;

    public PlayOneTileMoveMaker() {
        this.shouldAnalyzeTable = false;
        this.analyzer = new SmartStateAnalyzer();
    }

    public PlayOneTileMoveMaker(boolean shouldAnalyzeTable) {
        this.shouldAnalyzeTable = shouldAnalyzeTable;
        this.analyzer = new SmartStateAnalyzer();
    }

    /**
     * add tile to a meld directly
     */
    public List<Command> tryAddDirectly(Tile tile, GameState state) {
        List<Command> commands = new ArrayList<>();
        List<Meld> tableMelds = state.getTableData().melds;
        //List<Tile> handTiles = state.getHandsData()[state.getCurrentPlayer()].tiles;

        int meldid = TableMeldSeeker.findDirectMeld(tile.value(), tile.color(), tableMelds);
        if (meldid != 0) {
            Meld m = Meld.getMeldFromId(meldid, tableMelds);
            System.out.println(state.getPlayerData()[state.getCurrentPlayer()].name + " adds {"
                    + tile.toString() + "} to " + m.tiles().toString());
            commands.add(handler -> {
                handler.takeTableMeld(tableMelds.indexOf(m));
                handler.takeHandTile(handler.getHand().getTiles().indexOf(tile));
                handler.getManipulationTable().combineMelds(0, 1);
                handler.submit();
            });
        }
        return commands;
    }


    public List<Integer> findMeldIdToCombine(Map<Meld, Integer> map, ActionHandler handler) {
        ManipulationTable manip = handler.getManipulationTable();
        List<Integer> id = new ArrayList<>();

        for (Meld m : map.keySet()) {
            handler.takeTableMeld(m);
            if (m.type() == MeldType.SET) {
                manip.detach(manip.getMelds().indexOf(m), map.get(m));
                id.add(manip.getMelds().get(manip.getMelds().size() - 1).getId());
            } else {
                if (map.get(m) == 0) {
                    manip.split(manip.getMelds().indexOf(m), 1);
                    id.add(manip.getMelds().get(manip.getMelds().size() - 2).getId());
                } else {
                    manip.split(manip.getMelds().indexOf(m), map.get(m));
                    id.add(manip.getMelds().get(manip.getMelds().size() - 1).getId());
                }
            }
        }
        return id;
    }


    public List<Command> tryFormRun_splitMiddle(Tile tile, GameState state) {
        List<Command> commands = new ArrayList<>();

        //try to form new meld by split other meld and take the right split tiles
        Map<Meld, Integer> map = CombinationSeeker.formRunBySplitRight(tile.value(), tile.color(), state.getTableData().melds);
        if (map.isEmpty()) {
            return commands;
        }

        System.out.print(state.getPlayerData()[state.getCurrentPlayer()].name + " use {" + tile.toString() + "} to form run with ");
        map.keySet().stream().forEach(meld -> System.out.print(meld.tiles().toString()));
        System.out.println();

        commands.add(handler -> {
            ManipulationTable manip = handler.getManipulationTable();
            handler.takeHandTile(state.getHandsData()[state.getCurrentPlayer()].tiles.indexOf(tile));
            Meld m = map.keySet().stream().findFirst().get();
            handler.takeTableMeld(m);
            manip.split(manip.getMelds().size() - 1, map.get(m) + 1);
            manip.combineMelds(0, 2);
            handler.submit();
        });
        return commands;

    }


    public List<Command> tryFormRun_detachMelds(Tile tile, GameState state) {
        List<Command> commands = new ArrayList<>();


        Map<Meld, Integer> map = CombinationSeeker.formRunByDetaching(tile.value(), tile.color(), state.getTableData().melds);

        if (map.size() < 2) {
            return commands;
        }

        System.out.print(state.getPlayerData()[state.getCurrentPlayer()].name + " use {" + tile.toString() + "} to form run with ");
        map.keySet().stream().forEach(meld -> System.out.print(meld.tiles().toString()));
        System.out.println();

        commands.add(handler -> {
            handler.takeHandTile(state.getHandsData()[state.getCurrentPlayer()].tiles.indexOf(tile));
            ManipulationTable manip = handler.getManipulationTable();
            List<Integer> id = new ArrayList<>();

            id.add(manip.getMelds().get(0).getId());
            id.addAll(findMeldIdToCombine(map, handler));
            manip.combineMelds(id);
            handler.submit();
        });


        return commands;
    }


    /**
     * try to form a run with that tile
     */

    public List<Command> tryFormRun(Tile tile, GameState state) {

        List<Command> commands = new ArrayList<>();
        commands.addAll(tryFormRun_splitMiddle(tile, state));
        return !commands.isEmpty() ? commands : tryFormRun_detachMelds(tile, state);
    }


    /**
     *
     */
    public List<Command> tryFormSet(Tile tile, GameState state) {
        List<Command> commands = new ArrayList<>();

        List<Tile> goodTiles = new ArrayList<>();
        goodTiles.add(tile);

        List<Tile> unformedTiles = HandMeldSeeker.findRemainingTiles(state.getHandsData()[state.getCurrentPlayer()].tiles);
        for(Tile t: unformedTiles){
            if(t.value()==tile.value() && t.color()!=tile.color()){
                goodTiles.add(t);
                break;
            }
        }

        Map<Meld, Integer> map = CombinationSeeker.formSet(goodTiles, state.getTableData().melds);

        if (goodTiles.size() + map.size() < 3) {
            return commands;
        }
        //console
        System.out.print(state.getPlayerData()[state.getCurrentPlayer()].name + " uses ");
        goodTiles.stream().forEach(tile1 -> System.out.print("[" + tile1.toString() + "] "));
        System.out.print("to form set with ");
        map.keySet().stream().forEach(meld -> System.out.print(meld.tiles().toString()));
        System.out.println();

        commands.add(handler -> {
            ManipulationTable manip = handler.getManipulationTable();
            List<Integer> id = new ArrayList<>();

            goodTiles.stream().forEach(tile1 -> {
                handler.takeHandTile(tile1);
                id.add(manip.getMelds().get(manip.getMelds().size() - 1).getId());
            });

            id.addAll(findMeldIdToCombine(map, handler));
            manip.combineMelds(id);
            handler.submit();
        });


        return commands;
    }


    /**
     * play a first playable tile from hand
     */
    @Override
    public List<Command> calculateMove(GameState state) {
        List<Command> commands = new ArrayList<>();
        List<Tile> handTiles = state.getHandsData()[state.getCurrentPlayer()].tiles;

        List<Meld> allMelds = HandMeldSeeker.findBestMelds(handTiles);

        allMelds.forEach(meld -> meld.tiles().forEach(handTiles::remove));

        if (shouldAnalyzeTable) {
            analyzer.setState(state);
        }

        for (Tile t : handTiles) {
            if (analyzer.shouldPlay(t)) {
                if (!(commands = tryAddDirectly(t, state)).isEmpty()) {
                    break;
                }
                if (!(commands = tryFormSet(t, state)).isEmpty()) {
                    break;
                }
                if (!(commands = tryFormRun(t, state)).isEmpty()) {
                    break;
                }
            }
        }
        return commands;
    }
}
