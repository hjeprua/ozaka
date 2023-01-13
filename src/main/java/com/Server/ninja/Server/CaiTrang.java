 package com.Server.ninja.Server;

public class CaiTrang {
    protected static short Head(Char c, final short part) {
        if (c.typeCaiTrang != -1) {
            return c.getTypeCaiTrang();
        }
        if (part > 50) {
            return part;
        } else {
            return -1;
        }
    }

    protected static short Weapon(final short part) {
        return -1;
    }

    protected static short Body(Char c, final short part) {
        if (c.typeCaiTrang != -1) {
            return (short) (c.getTypeCaiTrang() + 1);
        }
        if (part > 50) {
            return (short) (part + 1);
        } else {
            return -1;
        }
    }

    protected static short Leg(Char c, final short part) {
        if (c.typeCaiTrang != -1) {
            return (short) (c.getTypeCaiTrang() + 2);
        }
        if (part > 50) {
            return (short) (part + 2);
        } else {
            return -1;
        }
    }

    protected static int checkUpgradeCT(Char c, int index) {
        return c.user.player.ItemCaiTrang[index].upgrade;
    }
}
