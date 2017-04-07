package com.rpsnet.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.rpsnet.network.Packets;

public class ServerListener extends Listener
{
    GameServer gameServer;

    public ServerListener(GameServer g)
    {
        super();
        gameServer = g;
    }

    public void connected(Connection connection)
    {
        gameServer.addClient(connection);
        Log.info("Client connected with ID: " + connection.getID());
    }

    public void disconnected(Connection connection)
    {
        gameServer.removeClient(connection);
        Log.info("Removed client with ID: " + connection.getID());
    }

    public void received(Connection connection, Object o)
    {
        //System.out.println("Got packet: " + o);

        if(o instanceof Packets.RegisterName)
        {
            gameServer.registerName(connection, ((Packets.RegisterName)o).name);
        }
        else if(o instanceof Packets.RequestPlayerCount)
        {
            Packets.PlayerCount playerCount = new Packets.PlayerCount();
            playerCount.count = gameServer.playerCount();
            connection.sendTCP(playerCount);
        }
    }
}
