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
    private List<Tile> handTiles;
    private  List<Meld> tableMelds;
    private String playerName;
    private  CombinationSeeker seeker;


    public PlayOneTileMoveMaker() {
        this.shouldAnalyzeTable = false;
        this.analyzer = new SmartStateAnalyzer();
        this.handTiles = new ArrayList<>();
        this.tableMelds = new ArrayList<>();
    }

    public PlayOneTileMoveMaker(boolean shouldAnalyzeTable) {
        this.shouldAnalyzeTable = shouldAnalyzeTable;
        this.analyzer = new SmartStateAnalyzer();
        this.handTiles = new ArrayList<>();
        this.tableMelds = new ArrayList<>();
    }

    /**
     * add tile to a meld directly
     */
    private List<Command> tryAddDirectly(Tile tile) {
        List<Command> commands = new ArrayList<>();

        int meldid = TableMeldSeeker.findDirectMeld(tile.value(), tile.color(), tableMelds);
        if (meldid != 0) {
            Meld m = Meld.getMeldFromId(meldid, tableMelds);
            System.out.println(playerName + " adds {" + tile.toString() + "} to " + m.tiles().toString());
            commands.add(handler -> {
                handler.takeTableMeld(tableMelds.indexOf(m));
                handler.takeHandTile(tile);
                handler.getManipulationTable().combineMelds(0, 1);
                handler.submit();
            });
        }
        return commands;
    }


    private List<Integer> findMeldIdToCombine(Map<Meld, Integer> map, ActionHandler handler) {
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


    private List<Command> tryFormRun_splitMiddle(Tile tile) {
        List<Command> commands = new ArrayList<>();

        //try to form new meld by split other meld and take the right split tiles
        Map<Meld, Integer> map = seeker.formRunBySplitRight(tile.value(), tile.color());
        if (!map.isEmpty()) {
            System.out.print(playerName + " use {" + tile.toString() + "} to form run with ");
            map.keySet().stream().forEach(meld -> System.out.print(meld.tiles().toString()));
            System.out.println();

            commands.add(handler -> {
                ManipulationTable manip = handler.getManipulationTable();
                handler.takeHandTile(tile);
                Meld m = map.keySet().stream().findFirst().get();
                handler.takeTableMeld(m);
                manip.split(manip.getMelds().size() - 1, map.get(m) + 1);
                manip.combineMelds(0, 2);
                handler.submit();
            });
        }
        return commands;

    }

    private List<Command> tryFormRun_detachMelds(Tile tile) {
        List<Command> commands = new ArrayList<>();
        List<Tile> goodTiles = new ArrayList<>();
        goodTiles.add(tile);


        Map<Meld, Integer> map = seeker.formRunByDetaching(goodTiles);

        if (map.size() + goodTiles.size() < 3) {
            return commands;
        }

        //console
        System.out.print(playerName + " uses ");
        goodTiles.stream().forEach(tile1 -> System.out.print("[" + tile1.toString() + "] "));
        System.out.print("to form run with ");
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
     * try to form a run with that tile
     */

    private List<Command> tryFormRun(Tile tile) {

        List<Command> commands = new ArrayList<>();
        commands.addAll(tryFormRun_splitMiddle(tile));
        return !commands.isEmpty() ? commands : tryFormRun_detachMelds(tile);

    }


    /**
     *
     */
    private List<Command> tryFormSet(Tile tile) {
        List<Command> commands = new ArrayList<>();

        List<Tile> goodTiles = new ArrayList<>();
        goodTiles.add(tile);

        Map<Meld, Integer> map = seeker.formSet(goodTiles);

        if (goodTiles.size() + map.size() < 3) {
            return commands;
        }
        //console
        System.out.print(playerName + " uses ");
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
        handTiles = state.getHandsData()[state.getCurrentPlayer()].tiles;
        tableMelds = state.getTableData().melds;
        playerName = state.getPlayerData()[state.getCurrentPlayer()].name;
        seeker = new CombinationSeeker(handTiles,tableMelds);

        List<Tile> remainingTiles = HandMeldSeeker.findRemainingTiles(handTiles);

        if (shouldAnalyzeTable) {
            analyzer.setState(state);
        }

        for (Tile t : remainingTiles) {
            if (analyzer.shouldPlay(t)) {
                if (!(commands = tryAddDirectly(t)).isEmpty()) {
                    break;
                }
                if (!(commands = tryFormSet(t)).isEmpty()) {
                    break;
                }
                if (!(commands = tryFormRun(t)).isEmpty()) {
                    break;
                }
            }
        }
        return commands;
    }
}
