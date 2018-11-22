package project.rummy.behaviors;

import project.rummy.ai.CombinationSeeker;
import project.rummy.ai.HandMeldSeeker;
import project.rummy.ai.SmartStateAnalyzer;
import project.rummy.ai.TableMeldSeeker;
import project.rummy.commands.Command;
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
     *add tile to a meld directly
     */
    public List<Command> tryAddDirectly(Tile tile, GameState state) {
        List<Command> commands = new ArrayList<>();
        List<Meld> tableMelds = state.getTableData().melds;
        //List<Tile> handTiles = state.getHandsData()[state.getCurrentPlayer()].tiles;

        int meldid = TableMeldSeeker.findDirectMeld(tile.value(), tile.color(), tableMelds);
        if(meldid != 0){
            Meld m = Meld.getMeldFromId(meldid, tableMelds);
            System.out.println(state.getPlayerData()[state.getCurrentPlayer()].name + " adds {"  + tile.toString() +"} to " + m.tiles().toString());
            commands.add(handler ->{
                handler.takeTableMeld(tableMelds.indexOf(m));
                handler.takeHandTile(handler.getHand().getTiles().indexOf(tile));
                handler.getManipulationTable().combineMelds(0,1);
                handler.submit();
            });
        }
        return commands;
    }


    /**
     *try to form a run with that tile
     */

    public List<Command> tryFormRun(Tile tile, GameState state) {
        List<Command> commands = new ArrayList<>();
        List<Meld> tableMelds = new ArrayList<>(state.getTableData().melds);
        List<Tile> handTiles = state.getHandsData()[state.getCurrentPlayer()].tiles;

        //try to form new meld by split other meld and take the right split tiles
        Map<Meld, Integer> map = CombinationSeeker.formRunBySplitRight(tile.value(), tile.color(), tableMelds);
        if(!map.isEmpty()){
            System.out.print(state.getPlayerData()[state.getCurrentPlayer()].name + " use {"  + tile.toString() +"} to form run with ");
            for(Meld k: map.keySet()){
                System.out.print(k.tiles().toString());
            }
            System.out.println();
            commands.add(handler -> {
                ManipulationTable manip = handler.getManipulationTable();
                handler.takeHandTile(handTiles.indexOf(tile));
                Meld m = map.keySet().stream().findFirst().get();
                handler.takeTableMeld(tableMelds.indexOf(m));
                manip.split(manip.getMelds().size()-1, map.get(m)+1);
                manip.combineMelds(0,2);
                handler.submit();
            });
            return commands;
        }

        //
        Map<Meld, Integer> map3 = CombinationSeeker.formRunByDetaching(tile.value(), tile.color(), tableMelds);

        if(map3.size() >= 2){
            System.out.print(state.getPlayerData()[state.getCurrentPlayer()].name + " use {"  + tile.toString() +"} to form run with ");
            for(Meld k: map3.keySet()){
                System.out.print(k.tiles().toString());
            }
            System.out.println();
            commands.add(handler ->{
                handler.takeHandTile(handTiles.indexOf(tile));
                ManipulationTable manip = handler.getManipulationTable();
                List<Integer> meldids = new ArrayList<>();
                meldids.add(manip.getMelds().get(0).getId());
                for (Meld m : map3.keySet()){
                    handler.takeTableMeld(tableMelds.indexOf(m));
                    int index = manip.getMelds().indexOf(m);
                    if(m.type() == MeldType.SET) {
                        manip.detach(index, map3.get(m));
                        meldids.add(manip.getMelds().get(index+1).getId());
                    }else{
                        if(map3.get(m) == 0){
                            manip.split(index, 1);
                            meldids.add(manip.getMelds().get(index).getId());
                        }else{
                            manip.split(index, manip.getMelds().get(index).tiles().size()-1);
                            meldids.add(manip.getMelds().get(index+1).getId());
                        }
                    }

                    tableMelds.remove(tableMelds.indexOf(m));
                }
                manip.combineMelds(meldids);
                handler.submit();
            });
        }

        return commands;
    }


    /**
     *
     */
    public List<Command> tryFormSet(Tile tile, GameState state){
        List<Command> commands = new ArrayList<>();
        List<Meld> tableMelds = new ArrayList<>(state.getTableData().melds);
        List<Tile> unformedTiles = HandMeldSeeker.findRemainingTiles(state.getHandsData()[state.getCurrentPlayer()].tiles);

        List<Tile> goodTiles = new ArrayList<>();
        goodTiles.add(tile);


        for(Tile t: unformedTiles){
            if(t.value()==tile.value() && t.color()!=tile.color()){
                goodTiles.add(t);
            }
        }

        Map<Meld,Integer> map = CombinationSeeker.formSet(goodTiles, tableMelds);

        if(goodTiles.size() + map.size() >= 3){
            //console
            System.out.print(state.getPlayerData()[state.getCurrentPlayer()].name + " uses ");
            for(Tile t1: goodTiles){ System.out.print( "[" + t1.toString() + "] "); }
            System.out.print("to form set with ");
            for(Meld k: map.keySet()){ System.out.print(k.tiles().toString()); }
            System.out.println();

            commands.add(handler ->{
                ManipulationTable manip = handler.getManipulationTable();
                List<Integer> meldids = new ArrayList<>();

                handler.takeHandTile(goodTiles.get(0));
                meldids.add(manip.getMelds().get(0).getId());

                if(goodTiles.size()>1){
                    handler.takeHandTile(goodTiles.get(1));
                    meldids.add(manip.getMelds().get(1).getId());
                }

                for (Meld m : map.keySet()){
                    handler.takeTableMeld(m);
                    if(m.type() == MeldType.SET) {
                        manip.detach(manip.getMelds().indexOf(m), map.get(m));
                        meldids.add(manip.getMelds().get(manip.getMelds().size()-1).getId());
                    }else{
                        if(map.get(m) == 0){
                            manip.split(manip.getMelds().indexOf(m),1);
                            meldids.add(manip.getMelds().get(manip.getMelds().size()-2).getId());
                        }else{
                            manip.split(manip.getMelds().indexOf(m),map.get(m));
                            meldids.add(manip.getMelds().get(manip.getMelds().size()-1).getId());
                        }
                    }
                    tableMelds.remove(tableMelds.indexOf(m));
                }
                manip.combineMelds(meldids);
                handler.submit();
            });
        }

        return commands;
    }



    /**
     *play a first playable tile from hand
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

        for(Tile t: handTiles){
            if (analyzer.shouldPlay(t)) {
                if(!(commands = tryAddDirectly(t,state)).isEmpty()){ break; }
                if(!(commands = tryFormSet(t,state)).isEmpty()){ break; }
                if(!(commands = tryFormRun(t,state)).isEmpty()){ break; }
            }
        }
        return commands;
    }
}
