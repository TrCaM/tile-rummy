package project.rummy.entities;

import project.rummy.control.Controller;

import java.io.Serializable;

public class PlayerData implements Serializable {
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
