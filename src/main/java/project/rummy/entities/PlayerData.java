package project.rummy.entities;

import project.rummy.control.Controller;

public class PlayerData {
    public String controllerType;
    public String name;

    /**
     *
     * @param name
     * @param controller: human, strategy1, strategy2, strategy3
     */
    public PlayerData(String name, String controller){
        this.name = name;
        this.controllerType = controller;
    }

}
