package com.example.andreea.finalchatandsend;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.annotation.BusInterface;
import org.alljoyn.bus.annotation.BusSignal;

/**
 * Created by Andreea on 4/19/2016.
 */
@BusInterface(name = "com.example.andreea.finalchatandsend")
public interface ChatInterface {

    @BusSignal
    public void Chat(String name) throws BusException;
}
