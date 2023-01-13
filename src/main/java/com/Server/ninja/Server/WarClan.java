package com.Server.ninja.Server;

import com.Server.ninja.template.MapTemplate;

import java.util.ArrayList;

public class WarClan {
    protected Map[] maps;
    protected byte warClanID;
    protected int coinTotal;
    protected ArrayList<Char> blacks;
    protected ArrayList<Char> white;
    protected final ArrayList<Char> AChar;

    protected Clan clanBlack;
    protected Clan clanWhite;
    protected String clanBlacks;
    protected String clanWhites;
    protected byte typeWin;
    protected int timeLength;
    protected boolean isFinght;
    protected boolean isWait;
    protected static final String[] TITLE;
    private static final short[] arrMapId;
    protected static int setTimeChien;
    public int pointBlack;
    public int pointWhite;
    protected static byte baseId;
    protected static final Object LOCK;
    protected static ArrayList<WarClan> arrWarClan;

    public WarClan() {
        this.white = new ArrayList<>();
        this.blacks = new ArrayList<>();
        this.AChar = new ArrayList<>();
        this.clanBlacks = "";
        this.clanWhites = "";
        this.pointBlack = 0;
        this.pointWhite = 0;
        this.timeLength = 0;
        this.typeWin = -1;
    }

    protected static WarClan setWarClan() {
        synchronized (WarClan.LOCK) {
            final WarClan warClan;
            final WarClan war = warClan = new WarClan();
            final byte baseId = WarClan.baseId;
            WarClan.baseId = (byte)(baseId + 1);
            warClan.warClanID = baseId;
            war.maps = new Map[WarClan.arrMapId.length];
            for (byte i = 0; i < WarClan.arrMapId.length; ++i) {
                final MapTemplate template = GameScr.mapTemplates[WarClan.arrMapId[i]];
                war.maps[i] = new Map(template.mapID, (byte)20, template.numZone);
                war.maps[i].warClan = war;
            }
            for (byte i = 0; i < war.maps.length; ++i) {
                final Map map = war.maps[i];
                if (map != null) {
                    for (byte j = 0; j < map.tileMaps.length; ++j) {
                        final TileMap tileMap = map.tileMaps[j];
                        if (tileMap != null) {
                            tileMap.initWaypoint(6);
                        }
                    }
                }
            }
            for (byte i = 0; i < war.maps.length; ++i) {
                final Map map = war.maps[i];
                if (map != null) {
                    map.start();
                }
            }
            war.timeLength = (int)(System.currentTimeMillis() / 1000L + 60);
            WarClan.arrWarClan.add(war);
            return war;
        }
    }

    protected static Map getMap(final WarClan war, final short mapId) {
        for (byte i = 0; i < war.maps.length; ++i) {
            if (war.maps[i].template.mapID == mapId) {
                return war.maps[i];
            }
        }
        return null;
    }

    protected void addChar(final Char _char) {
        synchronized (this.AChar) {
            this.AChar.add(_char);
        }
    }

    protected void removeChar(final Char _char) {
        synchronized (this.AChar) {
            for (byte i = 0; i < this.AChar.size(); ++i) {
                final Char player = this.AChar.get(i);
                if (player != null && player.charID == _char.charID) {
                    this.AChar.remove(i);
                }
            }
        }
    }

    protected static void close() {
        synchronized (WarClan.LOCK) {
            for (int i = WarClan.arrWarClan.size() - 1; i >= 0; --i) {
                final WarClan war = WarClan.arrWarClan.get(i);
                WarClan.arrWarClan.remove(i);
                war.CLOSE();
            }
        }
    }

    protected void finish(final byte type) {
        this.isFinght = false;
        final TileMap tileMap = this.maps[1].tileMaps[0];
        final long coin = this.coinTotal * 2L * 9L / 10L;
        if (type == -1) {
            for (short i = 0; i < tileMap.numPlayer; ++i) {
                final Char player = tileMap.aCharInMap.get(i);
                if (player != null && player.user != null && player.user.session != null) {
                    if (clanWhite.name.equals(clanWhites)) {
                        clanWhite.updateCoin(coin/2L);
                    }
                    if (clanBlack.name.equals(clanBlacks)) {
                        clanBlack.updateCoin(coin/2L);
                    }
                    Service.ServerMessage(player, "Hai gia tộc hoà nhau và nhận lại " + coin + " gia tộc.");
                }
            }
        }
        else if (type == 0) {
            for (short i = 0; i < tileMap.numPlayer; ++i) {
                final Char player = tileMap.aCharInMap.get(i);
                if (player != null && player.user != null && player.user.session != null) {
                    if (clanWhite.name.equals(clanWhites)) {
                        clanWhite.updateCoin(coin);
                    }
                    Service.ServerMessage(player, "Gia tộc " + clanWhites + " giành chiến thắng và nhận được " + coin + " gia tộc.");
                }
            }
        }
        else if (type == 1) {
            for (short i = 0; i < tileMap.numPlayer; ++i) {
                final Char player = tileMap.aCharInMap.get(i);
                if (player != null && player.user != null && player.user.session != null) {
                    if (clanBlack.name.equals(clanBlacks)) {
                        clanBlack.updateCoin(coin);
                    }
                    Service.ServerMessage(player, "Gia tộc " + clanBlacks + " giành chiến thắng và nhận được " + coin + " gia tộc.");
                }
            }
        }
    }

    protected void CLOSE() {
        if (isWait) {
            final TileMap tileMap = this.maps[0].tileMaps[0];
            for (short i = 0; i < tileMap.numPlayer; ++i) {
                final Char player = tileMap.aCharInMap.get(i);
                if (player != null && player.user != null && player.user.session != null) {
                    Service.ServerMessage(player, "Trận đấu bị huỷ bỏ.");
                }
            }
        } else if (this.isFinght) {
            this.finish((byte)(-1));
        }
        for (byte i = 0; i < this.maps.length; ++i) {
            final Map map = this.maps[i];
            if (map != null) {
                for (byte j = 0; j < map.tileMaps.length; ++j) {
                    final TileMap tileMap = map.tileMaps[j];
                    if (tileMap != null) {
                        try {
                            tileMap.lock.lock("Close war clan");
                            try {
                                for (short k = (short)(tileMap.aCharInMap.size() - 1); k >= 0; --k) {
                                    final Char player = tileMap.aCharInMap.get(k);
                                    if (player != null && player.isHuman) {
                                        final Map ltd = MapServer.getMapServer(player.mapLTDId);
                                        if (ltd != null) {
                                            final TileMap tile = ltd.getSlotZone(player);
                                            if (tile == null) {
                                                GameCanvas.startOKDlg(player.user.session, Text.get(0, 9));
                                            }
                                            else {
                                                player.testDunPhe = -1;
                                                player.tileMap.EXIT(player);
                                                player.cx = tile.map.template.goX;
                                                player.cy = tile.map.template.goY;
                                                player.cHP = player.cMaxHP();
                                                player.cMP = player.cMaxMP();
                                                player.statusMe = 1;
                                                Service.MeLive(player);
                                                tile.Join(player);
                                            }
                                        }
                                    }
                                }
                            }
                            finally {
                                tileMap.lock.unlock();
                            }
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        for (byte i = 0; i < this.maps.length; ++i) {
            final Map map = this.maps[i];
            if (map != null) {
                map.close();
            }
        }
    }

    protected void WarClanMessage(final String str) {
        for (byte i = 0; i < this.maps.length; ++i) {
            final Map map = this.maps[i];
            if (map != null) {
                for (byte j = 0; j < map.tileMaps.length; ++j) {
                    final TileMap tileMap = map.tileMaps[j];
                    if (tileMap != null) {
                        try {
                            tileMap.lock.lock("Chat gtc");
                            try {
                                for (short k = (short)(tileMap.aCharInMap.size() - 1); k >= 0; --k) {
                                    final Char player = tileMap.aCharInMap.get(k);
                                    if (player != null && player.user != null && player.user.session != null) {
                                        Service.ServerMessage(player, str);
                                    }
                                }
                            }
                            finally {
                                tileMap.lock.unlock();
                            }
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    protected void update() {
        Char char1 = Client.getPlayer(clanBlack.main_name);
        Char char2 = Client.getPlayer(clanWhite.main_name);
        if ((char1 == null || char2 == null) && isWait) {
            WarClanMessage("Trận đấu bị huỷ bỏ.");
            CLOSE();
        }
        if (char1 != null && char2 != null && isWait) {
            Char c = char1.findCharInMap(char2.charID);
            if (c == null) {
                this.CLOSE();
            }
        }
    }

    static {
        arrMapId = new short[] {117,118,119,120,121,122,123,124};
        LOCK = new Object();
        WarClan.setTimeChien = 3600;
        TITLE = new String[] { "Học Giả", "Hạ Nhẫn", "Trung Nhẫn", "Thượng nhẫn", "Nhẫn Giả" };
        WarClan.arrWarClan = new ArrayList<>();
    }
}
