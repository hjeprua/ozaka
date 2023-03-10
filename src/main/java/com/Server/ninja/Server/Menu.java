 package com.Server.ninja.Server;

import java.util.Random;

import com.Server.ninja.template.MapTemplate;
import com.Server.ninja.template.MobTemplate;
import lombok.val;

public class Menu {
    private static final int[] arrLevelGift;

    protected static void Menu(final Char _char, final byte npcId, byte menuId, final byte optionId) {
        Util.log("npcId:" + npcId + " menuId:" + menuId + " optionId:" + optionId);
        final Npc npc = TileMap.NPCNear(_char, npcId);
        if (Task.isTaskNPC(_char, npcId) && npc != null) {
            if (_char.ctaskIndex == -1) {
                --menuId;
                if (menuId == -1) {
                    Task.Task(_char, npcId);
                }
            } else if (Task.isFinishTask(_char)) {
                --menuId;
                if (menuId == -1) {
                    Task.FinishTask(_char, npcId);
                }
            } else if (_char.ctaskId == 1) {
                --menuId;
                if (menuId == -1) {
                    Task.doTask(_char, npcId, menuId, optionId);
                }
            } else if (_char.ctaskId == 7) {
                --menuId;
                if (menuId == -1) {
                    Task.doTask(_char, npcId, menuId, optionId);
                }
            } else if (_char.ctaskId == 13) {
                --menuId;
                if (menuId == -1) {
                    if (_char.ctaskIndex == 1) {
                        // OOka
                        _char.uptaskMaint();
                    } else if (_char.ctaskIndex == 2) {
                        // Haru
                        _char.uptaskMaint();
                    } else if (_char.ctaskIndex == 3) {
                        // Hiro
                        _char.uptaskMaint();
                    }
                }
            }
        }
        if (npc != null) {
            switch (npcId) {
                case 0: {
                    if (_char.menuType == 1 && _char.tileMap.map.isTestDunMap()) {
                        if (menuId == 0) {
                            synchronized (TestDun.LOCK) {
                                for (int i = TestDun.arrTestDun.size() - 1; i >= 0; --i) {
                                    final TestDun test = TestDun.arrTestDun.get(i);
                                    if (test != null && test.testdunID == _char.tileMap.map.testDun.testdunID) {
                                        TestDun.arrTestDun.remove(i);
                                        test.CLOSE();
                                    }
                                }
                            }
                            break;
                        }
                        if (menuId == 1) {
                            Service.openTextBoxUI(_char, Text.get(0, 298), (short) 498);
                            break;
                        }
                        if (menuId == 2) {
                            break;
                        }
                        break;
                    } else {
                        if (menuId == 0) {
                            Service.openUI(_char, (byte) 2, null, null);
                            break;
                        }
                        if (menuId == 1) {
                            if (!_char.isHuman) {
                                Service.ServerMessage(_char, Text.get(0, 310));
                                break;
                            }
                            if (optionId == 0) {
                                if (!_char.cClanName.isEmpty() || _char.clan != null) {
                                    Service.openUISay(_char, npcId, Text.get(0, 187));
                                    break;
                                }
                                if (_char.user.luong < 30000) {
                                    Service.openUISay(_char, npcId, String.format(Text.get(0, 188), 30000));
                                    break;
                                }
                                if (_char.cLevel < 40) {
                                    Service.openUISay(_char, npcId, String.format(Text.get(0, 189), 40));
                                    break;
                                }
                                Service.openTextBoxUI(_char, Text.get(0, 190), (short) 500);
                                break;
                            } else {
                                if (optionId != 1) {
                                    break;
                                }
                                if (_char.cLevel < 40) {
                                    Service.openUISay(_char, npcId, Text.get(0, 33));
                                    break;
                                }
                                if (_char.clan == null) {
                                    Service.openUISay(_char, npcId, "Ho???t ?????ng n??y ch??? d??nh cho ng?????i ???? gia nh???p gia t???c.");
                                    break;
                                }
                                if (_char.clan.clanManor == null && !_char.clan.main_name.equals(_char.cName)) {
                                    Service.openUISay(_char, npcId, Text.get(0, 249));
                                    break;
                                }
                                if (_char.clan.clanManor == null && _char.clan.openDun <= 0) {
                                    Service.openUISay(_char, npcId, Text.get(0, 250));
                                    break;
                                }
                                final Clan clan = _char.clan;
                                synchronized (clan.LOCK_CLAN) {
                                    if (clan.clanManor == null && clan.openDun > 0) {
                                        ClanManor.setClanManor(clan);
                                        final Map map = ClanManor.getMap(clan.clanManor, (short) 80);
                                        final TileMap tile = map.getSlotZone(_char);
                                        _char.tileMap.Exit(_char);
                                        _char.cx = tile.map.template.goX;
                                        _char.cy = tile.map.template.goY;
                                        tile.Join(_char);
                                        _char.user.player.ItemBagAdd(new Item(null, (short) 260, 1, 5400, true, (byte) 0, 0));
                                        Service.ServerMessage(_char, "B???n nh???n ???????c ch??a kho?? c?? quan.");
                                    } else if (clan.clanManor != null) {
                                        if (_char.clan.clanManor.memberAcceptManor.contains(_char.cName) || _char.clan.assist_name.equals(_char.cName) || _char.clan.main_name.equals(_char.cName)) {
                                            final Map map = ClanManor.getMap(clan.clanManor, (short) 80);
                                            final TileMap tile = map.getSlotZone(_char);
                                            if (tile == null) {
                                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 24));
                                            } else {
                                                _char.tileMap.Exit(_char);
                                                _char.cx = tile.map.template.goX;
                                                _char.cy = tile.map.template.goY;
                                                tile.Join(_char);
                                            }
                                        } else {
                                            Service.openUISay(_char, npcId, "B???n ch??a ???????c m???i tham gia L??nh ?????a Gia T???c.");
                                        }
                                    }
                                }
                                break;
                            }
                        } else if (menuId == 2) {
                            if (!_char.isHuman) {
                                Service.ServerMessage(_char, Text.get(0, 310));
                                break;
                            }
                            if (optionId == 0) {
                                Service.reviewPB(_char);
                                break;
                            }
                            if (optionId == 1) {
                                BackCave.JoinCave(_char, (byte) 0, npcId);
                                break;
                            }
                            if (optionId == 2) {
                                BackCave.JoinCave(_char, (byte) 1, npcId);
                                break;
                            }
                            if (optionId == 3) {
                                BackCave.JoinCave(_char, (byte) 2, npcId);
                                break;
                            }
                            if (optionId == 4) {
                                BackCave.JoinCave(_char, (byte) 3, npcId);
                                break;
                            }
                            if (optionId == 5) {
                                BackCave.JoinCave(_char, (byte) 4, npcId);
                                break;
                            }
                            if (optionId == 6) {
                                BackCave.JoinCave(_char, (byte) 5, npcId);
                                break;
                            }
                            break;
                        } else {
                            if (menuId != 3) {
                                break;
                            }
                            if (!_char.isHuman) {
                                Service.ServerMessage(_char, Text.get(0, 310));
                                break;
                            }
                            if (optionId == 0) {
                                Service.openTextBoxUI(_char, Text.get(0, 293), (short) 499);
                                break;
                            }
                            if (optionId == 1) {
                                Service.testDunList(_char, TestDun.arrTestDun);
                                break;
                            }
                            if (optionId == 3) {
                                final NJTalent nj = NJTalent.ninja_talent;
                                if (nj == null) {
                                    Service.openUISay(_char, npcId, "Ninja t??i n??ng m??? v??o 19h50-21h h??ng ng??y");
                                    return;
                                }
                                final Map map3 = NJTalent.getMap(nj, NJTalent.mapChuanBi);
                                if (map3 != null) {
                                    final TileMap tileMap2 = map3.getSlotZone(_char);
                                    if (tileMap2 != null) {
                                        _char.tileMap.Exit(_char);
                                        _char.cx = map3.template.goX;
                                        _char.cy = map3.template.goY;
                                        tileMap2.Join(_char);
                                    } else {
                                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                    }
                                }
                                break;
                            }
                            if (optionId == 4) {
                                Service.AlertMessage(_char, "Ninja T??i N??ng", Top.getStringBXH(_char, 6));
                                break;
                            }
                            break;
                        }
                    }
                    //break;
                }
                case 1: {
                    if (menuId != 0) {
                        break;
                    }
                    if (optionId == 0) {
                        Service.openUI(_char, (byte) (21 - _char.cgender), null, null);
                        break;
                    }
                    if (optionId == 1) {
                        Service.openUI(_char, (byte) (23 - _char.cgender), null, null);
                        break;
                    }
                    if (optionId == 2) {
                        Service.openUI(_char, (byte) (25 - _char.cgender), null, null);
                        break;
                    }
                    if (optionId == 3) {
                        Service.openUI(_char, (byte) (27 - _char.cgender), null, null);
                        break;
                    }
                    if (optionId == 4) {
                        Service.openUI(_char, (byte) (29 - _char.cgender), null, null);
                        break;
                    }
                    break;
                }
                case 2: {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.openUI(_char, (byte) 16, null, null);
                            break;
                        }
                        if (optionId == 1) {
                            Service.openUI(_char, (byte) 17, null, null);
                            break;
                        }
                        if (optionId == 2) {
                            Service.openUI(_char, (byte) 18, null, null);
                            break;
                        }
                        if (optionId == 3) {
                            Service.openUI(_char, (byte) 19, null, null);
                            break;
                        }
                        break;
                    } else {
                        if (menuId == 1) {
                            DanhVong.doNVDV(_char, npcId, optionId);
                            break;
                        }
                        break;
                    }
                    //break;
                }
                case 3: {
                    if (menuId == 0) {
                        Service.openUI(_char, (byte) 7, null, null);
                        break;
                    }
                    if (menuId == 1) {
                        Service.openUI(_char, (byte) 6, null, null);
                        break;
                    }
                    if (menuId != 2) {
                        break;
                    }
                    if (_char.ctaskId == 0 && _char.ctaskIndex == 0) {
                        _char.uptaskMaint();
                        Service.openUISay(_char, npcId, Talk.getTask(0, 1));
                        break;
                    }
                    Service.openUISay(_char, npcId, Talk.get(0, npcId));
                    break;
                }
                case 4: {
                    if (menuId == 0) {
                        Service.openUI(_char, (byte) 9, null, null);
                        break;
                    }
                    if (menuId == 1) {
                        Service.openUI(_char, (byte) 8, null, null);
                        break;
                    }
                    if (menuId != 2) {
                        break;
                    }
                    if (_char.ctaskId == 0 && _char.ctaskIndex == 1) {
                        _char.uptaskMaint();
                        Service.openUISay(_char, npcId, Talk.getTask(0, 2));
                        break;
                    }
                    Service.openUISay(_char, npcId, Talk.get(0, npcId));
                    break;
                }
                case 5: {

                    if (menuId == 0) {
                        switch (optionId) {
                            case 0: {
                                Service.openUIBox(_char);
                                break;
                            }
                            case 1: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }
                                // b??? s??u t???p Kh???ng Minh Ti???n
                                Service.openMenuBST(_char);
                                break;
                            }
                            case 2: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }
                                // c???i trang Kh???ng Minh Ti???n
                                Service.openMenuCaiTrang(_char);
                                break;
                            }
                            case 3: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }
                                if (_char.typeCaiTrang != -1) {
                                    _char.typeCaiTrang = -1;
                                    Service.updateInfoMe(_char);
                                    Player.updateInfoPlayer(_char);
                                }
                                break;
                            }
                        }
                    }
                    if (menuId == 1) {
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                            break;
                        }
                        _char.mapLTDId = _char.tileMap.mapID;
                        Service.openUISay(_char, npcId, Text.get(0, 39));
                        break;
                    } else if (menuId == 2) {
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                            break;
                        }
                        if (optionId == 0) {
                            if (_char.cLevel < 60) {
                                Service.ServerMessage(_char, Text.get(0, 127));
                                break;
                            }
                            final Map map2 = MapServer.getMapServer(139);
                            if (map2 != null) {
                                final TileMap tileMap = map2.getSlotZone(_char);
                                if (tileMap == null) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                } else {
                                    _char.tileMap.Exit(_char);
                                    _char.cx = tileMap.map.template.goX;
                                    _char.cy = tileMap.map.template.goY;
                                    tileMap.Join(_char);
                                }
                            }
                            break;
                        } else {
                            if (optionId == 1) {
                                Service.openUISay(_char, npcId, Text.get(0, 126));
                                break;
                            }
                            break;
                        }
                    } else {
                        if (menuId != 3) {
                            break;
                        }
                        if (_char.ctaskId == 0 && _char.ctaskIndex == 4) {
                            _char.uptaskMaint();
                            Service.openUISay(_char, npcId, Talk.getTask(0, 5));
                            break;
                        }
                        Service.openUISay(_char, npcId, Talk.get(0, npcId));
                        break;
                    }
                    //break;
                }
                case 6: {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.openUI(_char, (byte) 10, null, null);
                            break;
                        }
                        if (optionId == 1) {
                            Service.openUI(_char, (byte) 31, null, null);
                            break;
                        }
                        if (optionId == 2) {
                            Service.openUISay(_char, npcId, Text.get(0, 76));
                            break;
                        }
                        break;
                    } else if (menuId == 1) {
                        if (optionId == 0) {
                            Service.openUI(_char, (byte) 12, null, null);
                            break;
                        }
                        if (optionId == 1) {
                            Service.openUI(_char, (byte) 11, null, null);
                            break;
                        }
                        break;
                    } else {
                        if (menuId == 2) {
                            Service.openUI(_char, (byte) 13, null, null);
                            break;
                        }
                        if (menuId == 3) {
                            Service.openUI(_char, (byte) 33, null, null);
                            break;
                        }
                        if (menuId == 4) {
                            Service.openUI(_char, (byte) 46, null, null);
                            break;
                        }
                        if (menuId == 5) {
                            Service.openUI(_char, (byte) 47, null, null);
                            break;
                        }
                        if (menuId == 6) {
                            Service.openUI(_char, (byte) 49, null, null);
                            break;
                        }
                        if (menuId == 7) {
                            Service.openUI(_char, (byte) 50, null, null);
                            break;
                        }
                        if (menuId != 8) {
                            break;
                        }
                        if (_char.ctaskId == 0 && _char.ctaskIndex == 2) {
                            _char.uptaskMaint();
                            Service.openUISay(_char, npcId, Talk.getTask(0, 3));
                            break;
                        }
                        Service.openUISay(_char, npcId, Talk.get(0, npcId));
                        break;
                    }
                    //break;
                }
                case 7: {
                    if (menuId == 0) {
                        if (_char.ctaskId == 0 && _char.ctaskIndex == 5) {
                            _char.uptaskMaint();
                            Service.openUISay(_char, npcId, Talk.getTask(0, 6));
                            break;
                        }
                        Service.openUISay(_char, npcId, Talk.get(0, npcId));
                        break;
                    } else {
                        if (menuId > 0 && menuId <= Map.arrLang.length) {
                            final Map map2 = MapServer.getMapServer(Map.arrLang[menuId - 1]);
                            if (map2 != null) {
                                final TileMap tileMap = map2.getSlotZone(_char);
                                if (tileMap == null) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                } else if (Task.isLockChangeMap2(tileMap.mapID, _char.ctaskId)) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 84));
                                } else {
                                    _char.tileMap.Exit(_char);
                                    _char.cx = tileMap.map.template.goX;
                                    _char.cy = tileMap.map.template.goY;
                                    tileMap.Join(_char);
                                }
                            }
                            break;
                        }
                        break;
                    }
                    //break;
                }
                case 8: {
                    if (menuId >= 0 && menuId < Map.arrTruong.length) {
                        final Map map2 = MapServer.getMapServer(Map.arrTruong[menuId]);
                        if (map2 != null) {
                            final TileMap tileMap = map2.getSlotZone(_char);
                            if (tileMap == null) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                            } else if (Task.isLockChangeMap2(tileMap.mapID, _char.ctaskId)) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 84));
                            } else {
                                _char.tileMap.Exit(_char);
                                _char.cx = tileMap.map.template.goX;
                                _char.cy = tileMap.map.template.goY;
                                tileMap.Join(_char);
                            }
                        }
                        break;
                    }
                    if (menuId != 0) {
                        break;
                    }
                    if (_char.ctaskId == 0 && _char.ctaskIndex == 5) {
                        _char.uptaskMaint();
                        Service.openUISay(_char, npcId, Talk.getTask(0, 6));
                        break;
                    }
                    Service.openUISay(_char, npcId, Talk.get(0, npcId));
                    break;
                }
                case 9: {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.AlertMessage(_char, Text.get(0, 160), Top.getStringBXH(_char, 0));
                            break;
                        }
                        if (optionId == 1) {
                            Service.AlertMessage(_char, Text.get(0, 161), Top.getStringBXH(_char, 1));
                            break;
                        }
                        if (optionId == 2) {
                            Service.AlertMessage(_char, Text.get(0, 162), Top.getStringBXH(_char, 2));
                            break;
                        }
                        if (optionId == 3) {
                            Service.AlertMessage(_char, Text.get(0, 163), Top.getStringBXH(_char, 3));
                            break;
                        }
                        break;
                    } else if (menuId == 1) {
                        if (optionId == 0) {
                            Admission.Admission(_char, npcId, (byte) 1);
                            break;
                        }
                        if (optionId == 1) {
                            Admission.Admission(_char, npcId, (byte) 2);
                            break;
                        }
                        break;
                    } else {
                        if (menuId == 2) {
                            Char.clearPoint(_char, optionId, npcId);
                            break;
                        }
                        if (menuId == 3) {
                            if (_char.ctaskId == 8 && _char.ctaskIndex == 1) {
                                _char.uptaskMaint();
                                Service.openUISay(_char, npcId, Talk.getTask(0, 112));
                                break;
                            }
                            Service.openUISay(_char, npcId, Talk.get(0, npcId));
                            break;
                        } else {
                            if (menuId == 5) {
                                Service.AlertMessage(_char, Text.get(0, 185), Top.getStringBXH(_char, 4));
                                break;
                            }
                            break;
                        }
                    }
                    //break;
                }
                case 10: {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.AlertMessage(_char, Text.get(0, 160), Top.getStringBXH(_char, 0));
                            break;
                        }
                        if (optionId == 1) {
                            Service.AlertMessage(_char, Text.get(0, 161), Top.getStringBXH(_char, 1));
                            break;
                        }
                        if (optionId == 2) {
                            Service.AlertMessage(_char, Text.get(0, 162), Top.getStringBXH(_char, 2));
                            break;
                        }
                        if (optionId == 3) {
                            Service.AlertMessage(_char, Text.get(0, 163), Top.getStringBXH(_char, 3));
                            break;
                        }
                        break;
                    } else if (menuId == 1) {
                        if (optionId == 0) {
                            Admission.Admission(_char, npcId, (byte) 3);
                            break;
                        }
                        if (optionId == 1) {
                            Admission.Admission(_char, npcId, (byte) 4);
                            break;
                        }
                        break;
                    } else {
                        if (menuId == 2) {
                            Char.clearPoint(_char, optionId, npcId);
                            break;
                        }
                        if (menuId == 3) {
                            if (_char.ctaskId == 8 && _char.ctaskIndex == 2) {
                                _char.uptaskMaint();
                                Service.openUISay(_char, npcId, Talk.getTask(0, 113));
                                break;
                            }
                            Service.openUISay(_char, npcId, Talk.get(0, npcId));
                            break;
                        } else {
                            if (menuId == 5) {
                                Service.AlertMessage(_char, Text.get(0, 185), Top.getStringBXH(_char, 4));
                                break;
                            }
                            break;
                        }
                    }
                    //break;
                }
                case 11: {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.AlertMessage(_char, Text.get(0, 160), Top.getStringBXH(_char, 0));
                            break;
                        }
                        if (optionId == 1) {
                            Service.AlertMessage(_char, Text.get(0, 161), Top.getStringBXH(_char, 1));
                            break;
                        }
                        if (optionId == 2) {
                            Service.AlertMessage(_char, Text.get(0, 162), Top.getStringBXH(_char, 2));
                            break;
                        }
                        if (optionId == 3) {
                            Service.AlertMessage(_char, Text.get(0, 163), Top.getStringBXH(_char, 3));
                            break;
                        }
                        break;
                    } else if (menuId == 1) {
                        if (optionId == 0) {
                            Admission.Admission(_char, npcId, (byte) 5);
                            break;
                        }
                        if (optionId == 1) {
                            Admission.Admission(_char, npcId, (byte) 6);
                            break;
                        }
                        break;
                    } else {
                        if (menuId == 2) {
                            Char.clearPoint(_char, optionId, npcId);
                            break;
                        }
                        if (menuId == 3) {
                            if (_char.ctaskId == 8 && _char.ctaskIndex == 3) {
                                _char.uptaskMaint();
                                Service.openUISay(_char, npcId, Talk.getTask(0, 114));
                                break;
                            }
                            Service.openUISay(_char, npcId, Talk.get(0, npcId));
                            break;
                        } else {
                            if (menuId == 5) {
                                Service.AlertMessage(_char, Text.get(0, 185), Top.getStringBXH(_char, 4));
                                break;
                            }
                            break;
                        }
                    }
                    //break;
                }
                case 12: {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.AlertMessage(_char, "Tr?????ng l??ng", "D??ng c??c ph??m Q,W,E,A,D: Di chuy???n nh??n v???t\nHo???c c??c ph??m L??n,Tr??i,Ph???i: Di chuy???n nh??n v???t\nPh??m Spacebar ho???c ph??m Enter: T???n c??ng ho???c h??nh ?????ng\nPh??m F1: Menu,Ph??m F2: ?????i m???c ti??u, ph??m 6,7: D??ng b??nh HP,MP\nPh??m 0: Chat,Ph??m P: ?????i k??? n??ng,Ph??m 1,2,3,4,5: S??? d???ng k??? n??ng ???????c g??n tr?????c trong m???c K??? N??ng");
                            break;
                        }
                        if (optionId == 1) {
                            Service.AlertMessage(_char, "Tr?????ng l??ng", "Ki???m, Kunai, ??ao: ??u ti??n t??ng s???c m???nh(s???c ????nh) --> th??? l???c(HP) --> Th??n ph??p(N?? ????n, ch??nh x??c) --> Charka(MP).\n\nTi??u, Cung, Qu???t: ??u ti??n t??ng Charka(S???c ????nh, MP) -->th??? l???c(HP)--> Th??n ph??p(N?? ????n, ch??nh x??c). Kh??ng t??ng SM.");
                            break;
                        }
                        if (optionId == 2) {
                            Service.AlertMessage(_char, "Tr?????ng l??ng", "Pk th?????ng: tr???ng th??i h??a b??nh.\n\nPk phe: kh??ng ????nh ???????c ng?????i c??ng nh??m hay c??ng bang h???i. Gi???t ng?????i kh??ng l??n ??i???m hi???u chi???n.\n\nPk ????? s??t: c?? th??? ????nh t???t c??? ng?????i ch??i. Gi???t 1 ng?????i s??? l??n 1 ??i???m hi???u chi???n.\n\n??i???m hi???u chi???n cao s??? kh??ng s??? d???ng b??nh HP, MP, Th???c ??n.\n\nT??? th??: ch???n ng?????i ch??i, ch???n t??? th??, ch??? ng?????i ???? ?????ng ??.\n\nC???u S??t: Ch???n ng?????i ch??i kh??c, ch???n c???u s??t, ??i???m hi???u chi???n t??ng 2.");
                            break;
                        }
                        if (optionId == 3) {
                            Service.AlertMessage(_char, "Tr?????ng l??ng", "B???n c?? th??? t???o m???t nh??m t???i ??a 6 ng?????i ch??i.\n\nNh???ng ng?????i trong c??ng nh??m s??? ???????c nh???n th??m x% ??i???m kinh nghi???m t??? ng?????i kh??c.\n\nNh???ng ng?????i c??ng nh??m s??? c??ng ???????c v???t ph???m, th??nh t??ch n???u c??ng chung nhi???m v???.\n\n????? m???i ng?????i v??o nh??m, ch???n ng?????i ????, v?? ch???n m???i v??o nh??m. ????? qu???n l?? nh??m, ch???n Menu/T??nh n??ng/Nh??m.");
                            break;
                        }
                        if (optionId == 4) {
                            Service.AlertMessage(_char, "Tr?????ng l??ng", "???? d??ng ????? n??ng c???p trang b???. B???n c?? th??? mua t??? c???a h??ng ho???c nh???t khi ????nh qu??i.N??ng c???p ???? nh???m m???c ????ch n??ng cao t??? l??? th??nh c??ng khi n??ng c???p trang b??? c???p cao.????? luy???n ????, b???n c???n t??m Kenshinto.\n\n????? ?????m b???o th??nh c??ng 100%, 4 vi??n ???? c???p th???p s??? luy???n th??nh 1 vi??n ???? c???p cao h??n.");
                            break;
                        }
                        if (optionId == 5) {
                            Service.AlertMessage(_char, "Tr?????ng l??ng", "N??ng c???p trang b??? nh???m m???c ????ch gia t??ng c??c ch??? s??? c?? b???n c???a trang b???. C?? c??c c???p trang b??? sau +1, +2, +3... t???i ??a +16.????? th???c hi???n, b???n c???n g???p NPC Kenshinto. Sau ????, ti???n h??nh ch???n v???t ph???m v?? s??? l?????ng ???? ????? ????? n??ng c???p. L??u ??, trang b??? c???p ????? 5 tr??? l??n n??ng c???p th???t b???i s??? b??? gi???m c???p ?????.\n\nB???n c?? th??? t??ch m???t v???t ph???m ???? n??ng c???p v?? thu l???i 50% s??? ???? ???? d??ng ????? n??ng c???p trang b??? ????.");
                            break;
                        }
                        if (optionId == 6) {
                            Service.AlertMessage(_char, "Tr?????ng l??ng", "Khi tham gia c??c ho???t ?????ng trong game b???n s??? nh???n ???????c ??i???m ho???t ?????ng. Qua m???t ng??y ??i???m ho???t ?????ng s??? b??? tr??? d???n (n???u t??? 1-49 tr??? 1, 50-99 tr??? 2, 100-149 tr??? 3...). M???i tu???n b???n s??? c?? c?? h???i ?????i Y??n sang Xu n???u c?? ????? ??i???m ho???t ?????ng theo v??u c???u c???a NPC Okanechan.\n\nM???t tu???n m???t l???n duy nh???t ???????c ?????i t???i ??a 70.000 Y??n = 70.000 xu.");
                            break;
                        }
                    }
                    if (menuId == 1) {
                        Random generator = new Random();
                        int value = generator.nextInt(3);
                        if (value == 0) {
                            Service.openUISay(_char, npcId, "L??ng Tone l?? ng??i l??ng c??? x??a, ???? c?? t??? r???t l??u.");
                        }
                        if (value == 1) {
                            Service.openUISay(_char, npcId, "??i th??a, v??? tr??nh, nh?? c??c con");
                        }
                        if (value == 2) {
                            Service.openUISay(_char, npcId, "Ta l?? Tajima, m???i vi???c ??? ????y ?????u do ta qu???n l??.");
                        }
                        break;
                    }
                    if (menuId == 2) {
                        if (_char.ctaskIndex == -1) {
                            Service.openUISay(_char, npcId, "Con ch??a nh???n nhi???m v??? n??o c???.");
                            return;
                        }
                        Service.openUISay(_char, npcId, "Ta ???? h???y v???t ph???m v?? nhi???m v??? l???n n??y cho con.");
                        _char.clearTask();
                        Service.getTask(_char);
                        break;
                    }
                    if (menuId == 3) {
                        try {
                            _char.tileMap.lock.lock("Doi phan than");
                            try {
                                Player.toNhanban(_char);
                            } finally {
                                _char.tileMap.lock.unlock();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    if (menuId == 4) {
                        Player player = null;
                        if (!_char.isHuman && _char.user != null) {
                            player = _char.user.player;
                        }
                        try {
                            _char.tileMap.lock.lock("Doi chu than");
                            try {
                                Player.toChar(_char);
                            } finally {
                                _char.tileMap.lock.unlock();
                            }
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        if (player != null) {
                            Player.CallNhanban(player.tileMap, player);
                        }
                        break;
                    }
                    break;
                }
                case 14:
                case 15:
                case 16:
                    if (_char.ctaskId == 15) {
                        if (menuId == 0) {
                            _char.uptaskMaint();
                            _char.user.player.ItemBagUses((short) 214, 1);
                        }
                    } else if (_char.ctaskId == 20 && npcId == 15) {
                        if (menuId == 0) {
                            final Map task = MapServer.getMapServer(74);
                            if (task != null) {
                                final TileMap tile = task.getFreeArea();
                                if (tile == null) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                } else {
                                    _char.tileMap.Exit(_char);
                                    _char.cx = tile.map.template.goX;
                                    _char.cy = tile.map.template.goY;
                                    _char.timeTask = System.currentTimeMillis() + 180000L;
                                    Service.mapTime(_char, 180);
                                    tile.Join(_char);
                                }
                            }
                        }
                    }
                    break;
                case 17:
                    if (_char.ctaskId == 17) {
                        if (menuId == 0) {
                            _char.uptaskMaint();
                        }
                    }
                    break;
                case 18:
                case 23:
                    if (menuId == -1) {
                        if (optionId == 0) {
                            if ((_char.ctaskId == 19 || _char.ctaskId == 23) && _char.ctaskIndex == 0) {
                                Task.getItemTask(_char);
                            }
                        }
                    }
                    break;
                case 24: {
                    if (_char.menuType == 1) {
                        if (menuId != 0) {
                            break;
                        }
                        if (_char.ctaskId == 0 && _char.ctaskIndex == 3) {
                            _char.uptaskMaint();
                            Service.openUISay(_char, npcId, Talk.getTask(0, 4));
                            break;
                        }
                        Service.openUISay(_char, npcId, Talk.get(0, npcId));
                        break;
                    } else if (menuId == 0) {
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                            break;
                        }
//                        if (_char.user.isTester == 0) {
//                            Service.openUISay(_char, npcId, "H??y ki???m 6789 l?????ng v?? ?????n NPC Vua H??ng x??c th???c.");
//                            break;
//                        }
                        if (_char.user.luong < 500) {
                            Service.openUISay(_char, npcId, Text.get(0, 154));
                            break;
                        }
                        _char.user.player.upGold(-500L, (byte) 1);
                        if (optionId == 0) {
                            _char.user.player.upCoin(5000000L, (byte) 1);
                            break;
                        }
                        if (optionId == 1) {
                            _char.user.player.upCoinLock(5500000L, (byte) 2);
                            break;
                        }
                        break;
                    } else {
                        if (menuId == 2) {
                            try {
                                final int level = Menu.arrLevelGift[optionId];
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                } else if (_char.user.player.LevelGift.contains(level)) {
                                    Service.openUISay(_char, npcId, Text.get(0, 164));
                                } else if (level > _char.user.player.cLevel) {
                                    Service.openUISay(_char, npcId, Text.get(0, 165));
                                } else {
                                    switch (level) {
                                        case 10: {
                                            _char.user.player.upCoinLock(10000L, (byte) 2);
                                            _char.user.player.upCoinLock(10000000L, (byte) 2);
                                            break;
                                        }
                                        case 20: {
                                            if (_char.user.player.ItemBagSlotNull() < 1) {
                                                Service.openUISay(_char, npcId, String.format(Text.get(0, 151), 1));
                                                return;
                                            }
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 240, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.upGold(10L, (byte) 1);
                                            _char.user.player.upCoinLock(20000000L, (byte) 2);
                                            break;
                                        }
                                        case 30: {
                                            if (_char.user.player.ItemBagSlotNull() < 8) {
                                                Service.openUISay(_char, npcId, String.format(Text.get(0, 151), 2));
                                                return;
                                            }
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 241, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 5, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 7, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 7, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 7, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 15, 500, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 20, 500, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 539, 1, -1, true, (byte) 0, 0));

                                            _char.user.player.upCoinLock(30000000L, (byte) 2);
                                            break;
                                        }
                                        case 40: {
                                            if (_char.user.player.ItemBagSlotNull() < 2) {
                                                Service.openUISay(_char, npcId, String.format(Text.get(0, 151), 2));
                                                return;
                                            }
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 242, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 6, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.upCoinLock(40000000L, (byte) 2);
                                            break;
                                        }
                                        case 50: {
                                            if (_char.user.player.ItemBagSlotNull() < 2) {
                                                Service.openUISay(_char, npcId, String.format(Text.get(0, 151), 2));
                                                return;
                                            }
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 269, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 7, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.upCoinLock(50000000L, (byte) 2);
                                            break;
                                        }
                                    }
                                    _char.user.player.LevelGift.add(level);
                                }
                            } catch (Exception e3) {
                            }
                            break;
                        }
                        if (menuId == 3) {
                            if (_char.ctaskId == 0 && _char.ctaskIndex == 3) {
                                _char.uptaskMaint();
                                Service.openUISay(_char, npcId, Talk.getTask(0, 4));
                                break;
                            }
                            Service.openUISay(_char, npcId, Talk.get(0, npcId));
                            break;
                        } else if (menuId == 4) {
                            if (!_char.isHuman) {
                                Service.ServerMessage(_char, Text.get(0, 310));
                                break;
                            }
                            if (optionId == 0) {
                                Service.openTextBoxUI(_char, Text.get(0, 255), (short) 501);
                                break;
                            }
                            if (optionId == 1) {
                                Service.openTextBoxUI(_char, Text.get(0, 255), (short) 502);
                                break;
                            }
                            if (optionId == 2) {
                                Service.openTextBoxUI(_char, Text.get(0, 255), (short) 503);
                                break;
                            }
                            if (optionId == 3) {
                                DiamondSwap.viewDiamond(_char);
                                break;
                            }
                            if (optionId == 4) {
                                DiamondSwap.BangGia(_char);
                                break;
                            }
                            break;
                        } else if (menuId == 5) {
                            if (!_char.isHuman) {
                                Service.ServerMessage(_char, Text.get(0, 310));
                                break;
                            }
                            if (optionId == 0) {
                                Service.openTextBoxUI(_char, "Nh???p t??n ng?????i nh???n :", (short) 998);
                                break;
                            }
                            if (optionId == 1) {
                                DiamondSwap.viewGiftCard(_char, npcId);
                                break;
                            }
                            break;
                        }
                    }
                    break;
                }
                case 25: {
                    MobTemplate mob;
                    MapTemplate map;
                    if (_char.menuType == 1 && _char.tileMap.map.isChienTruong()) {
                        final ChienTruong ct = ChienTruong.chien_truong;
                        if (menuId == 0) {
                            synchronized (ct.CHIENTRUONG_LOCK) {
                                TileMap.getMapLTD(_char);
                            }
                        } else if (menuId == 1) {
                            Service.reviewChienTruong(_char, ct);
                        }
                        break;
                    }
                    if (menuId == 0) {
                        break;
                    }
                    if (menuId == 1) {
                        switch (optionId) {
                            case 0:
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }

                                if (_char.cLevel < 20) {
                                    Service.openUISay(_char, npcId, "Ph???i ?????t c???p ????? 20 m???i c?? th??? nh???n nhi???m v???.");
                                    break;
                                }

                                if (_char.taskHangNgay[5] != 0) {
                                    Service.openUISay(_char, npcId, "Nhi???m v??? tr?????c ???? ch??a ho??n th??nh, h??y mau ch??ng ??i ho??n th??nh v?? nh???n l???y ph???n th?????ng.");
                                    break;
                                }

                                if (_char.countTaskHangNgay >= 20) {
                                    Service.openUISay(_char, npcId, "Con ???? ho??n th??nh h???t nhi???m v??? c???a ng??y h??m nay, h??y quay l???i v??o ng??y mai.");
                                    break;
                                }

                                mob = Service.getMobIdByLevel(_char.cLevel);
                                if (mob != null) {
                                    map = Service.getMobMapId(mob.mobTemplateId);
                                    if (map != null) {
                                        _char.taskHangNgay[0] = 0;
                                        _char.taskHangNgay[1] = 0;
                                        _char.taskHangNgay[2] = Util.nextInt(10, 25);
                                        _char.taskHangNgay[3] = mob.mobTemplateId;
                                        _char.taskHangNgay[4] = map.mapID;
                                        _char.taskHangNgay[5] = 1;
                                        _char.countTaskHangNgay++;
                                        Service.getTaskOrder(_char, (byte) 0);
                                        Service.openUISay(_char, (short) npcId, "????y l?? nhi???m v??? th??? " + _char.taskHangNgay[6] + "/20 trong ng??y c???a con.");
                                    }
                                }
                                break;
                            case 1: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }
                                if (_char.taskHangNgay[5] == 0) {
                                    Service.openUISay(_char, (short) npcId, "Con ch??a nh???n nhi???m v??? n??o c???!");
                                    return;
                                }

                                _char.taskHangNgay[5] = 0;
                                _char.taskHangNgay = new int[]{-1, -1, -1, -1, -1, 0, _char.countTaskHangNgay};
                                Service.clearTaskOrder(_char, (byte) 0);
                                Service.openUISay(_char, (short) npcId, "Con ???? hu??? nhi???m v??? l???n n??y, h??y c??? g???ng l???n sau.");
                                break;
                            }
                            case 2: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }

                                if (_char.taskHangNgay[5] == 0) {
                                    Service.openUISay(_char, (short) npcId, "Con ch??a nh???n nhi???m v??? n??o c???!");
                                    return;
                                }

                                if (_char.user.player.ItemBagSlotNull() < 1) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                                    break;
                                }

                                if (_char.taskHangNgay[1] < _char.taskHangNgay[2]) {
                                    Service.openUISay(_char, (short) npcId, "Con ch??a ho??n th??nh nhi???m v??? ta giao!");
                                    return;
                                }
                                if (_char.countTaskHangNgay == 10 || _char.countTaskHangNgay == 20) {
                                    _char.user.player.upGold(Util.nextInt(200, 300), (byte) 2);
                                    _char.pointHoatDong += 3;
                                    Service.ServerMessage(_char, "B???n nh???n ???????c 3 ??i???m ho???t ?????ng.");
                                }
                                if ((_char.ctaskId == 30 && _char.ctaskIndex == 1) || (_char.ctaskId == 39 && _char.ctaskIndex == 3)) {
                                    _char.uptaskMaint();
                                }
                                _char.taskHangNgay[5] = 0;
                                _char.taskHangNgay = new int[]{-1, -1, -1, -1, -1, 0, _char.countTaskHangNgay};
                                Service.clearTaskOrder(_char, (byte) 0);
                                Service.openUISay(_char, (short) npcId, "Con h??y nh???n l???y ph???n th?????ng c???a m??nh.");
                                break;
                            }

                            case 3: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }

                                if (_char.taskHangNgay[4] != -1) {
                                    final Map map2 = MapServer.getMapServer(_char.taskHangNgay[4]);
                                    if (map2 != null) {
                                        final TileMap tileMap = map2.getSlotZone(_char);
                                        if (tileMap == null) {
                                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                        } else {
                                            _char.tileMap.Exit(_char);
                                            _char.cx = tileMap.map.template.goX;
                                            _char.cy = tileMap.map.template.goY;
                                            tileMap.Join(_char);
                                        }
                                    }
                                }
                                Service.openUISay(_char, (short) npcId, "Con ch??a nh???n nhi???m v??? n??o c???!");
                                break;
                            }
                            default:
                                break;
                        }
                        break;
                    }
                    if (menuId == 2) {
                        switch (optionId) {
                            case 0: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }

                                if (_char.cLevel < 30) {
                                    Service.openUISay(_char, npcId, "Ph???i ?????t c???p ????? 30 m???i c?? th??? nh???n nhi???m v???.");
                                    break;
                                }

                                if (_char.taskTaThu[5] != 0) {
                                    Service.openUISay(_char, npcId, "Nhi???m v??? tr?????c ???? ch??a ho??n th??nh, h??y mau ch??ng ??i ho??n th??nh v?? nh???n l???y ph???n th?????ng.");
                                    break;
                                }

                                if (_char.countTaskTaThu == 0) {
                                    Service.openUISay(_char, npcId, "Con ???? ho??n th??nh h???t nhi???m v??? c???a ng??y h??m nay, h??y quay l???i v??o ng??y mai.");
                                    break;
                                }

                                mob = Service.getMobIdTaThu(_char.cLevel);
                                if (mob != null) {
                                    map = Service.getMobMapIdTaThu(mob.mobTemplateId);
                                    if (map != null) {
                                        _char.taskTaThu[0] = 1;
                                        _char.taskTaThu[1] = 0;
                                        _char.taskTaThu[2] = 1;
                                        _char.taskTaThu[3] = mob.mobTemplateId;
                                        _char.taskTaThu[4] = map.mapID;
                                        _char.taskTaThu[5] = 1;
                                        _char.countTaskTaThu--;
                                        Service.getTaskOrder(_char, (byte) 1);
                                        Service.openUISay(_char, (short) npcId, "H??y ho??n th??nh nhi???m v??? v?? tr??? v??? ????y nh???n th?????ng.");
                                    }
                                }
                                break;
                            }
                            case 1: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }

                                if (_char.taskTaThu[5] == 0) {
                                    Service.openUISay(_char, (short) npcId, "Con ch??a nh???n nhi???m v??? n??o c???!");
                                    return;
                                }

                                Service.clearTaskOrder(_char, (byte) 1);
                                _char.taskTaThu[5] = 0;
                                _char.taskTaThu = new int[]{-1, -1, -1, -1, -1, 0, _char.countTaskTaThu};
                                Service.openUISay(_char, (short) npcId, "Con ???? hu??? nhi???m v??? l???n n??y.");
                                break;
                            }

                            case 2: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }

                                if (_char.taskTaThu[5] == 0) {
                                    Service.openUISay(_char, (short) npcId, "Con ch??a nh???n nhi???m v??? n??o c???!");
                                    return;
                                }

                                if (_char.taskTaThu[1] < _char.taskTaThu[2]) {
                                    Service.openUISay(_char, (short) npcId, "Con ch??a ho??n th??nh nhi???m v??? ta giao!");
                                    return;
                                }

                                if (_char.user.player.ItemBagSlotNull() < 1) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                                    break;
                                }

                                _char.taskTaThu[5] = 0;
                                _char.taskTaThu = new int[]{-1, -1, -1, -1, -1, 0, _char.countTaskTaThu};
                                Service.clearTaskOrder(_char, (byte) 1);
                                int quantity = 0;
                                if (_char.cLevel < 60) {
                                    quantity = 2;
                                } else if (_char.cLevel >= 60 && _char.cLevel < 80) {
                                    quantity = 3;
                                } else if (_char.cLevel >= 80) {
                                    quantity = 5;
                                }
                                if ((_char.ctaskId == 30 && _char.ctaskIndex == 2) || (_char.ctaskId == 39 && _char.ctaskIndex == 1)) {
                                    _char.uptaskMaint();
                                }
                                _char.user.player.ItemBagAdd(new Item(null, (short) 251, quantity, -1, false, (byte) 0, 0));
                                _char.user.player.upGold(50, (byte) 2);
                                _char.user.player.upCoin(50000, (byte) 2);
                                _char.pointHoatDong += 3;
                                Service.ServerMessage(_char, "B???n nh???n ???????c 3 ??i???m ho???t ?????ng.");
                                Service.openUISay(_char, (short) npcId, "Con h??y nh???n l???y ph???n th?????ng c???a m??nh.");
                                break;
                            }
                            default:
                                break;
                        }
                        break;
                    }
//                    if (menuId != 2) {
//                        break;
//                    }
                    if (menuId == 3) {
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                            break;
                        }
                        if (optionId == 0 || optionId == 1) {
                            final ChienTruong ct = ChienTruong.chien_truong;
                            if (ct == null) {
                                Service.openUISay(_char, npcId, Text.get(0, 330));
                            } else {
                                synchronized (ct.CHIENTRUONG_LOCK) {
                                    if (!ct.isBaoDanh && !ct.aCharBlack.contains(_char.user.player.playerId) && !ct.aCharWhite.contains(_char.user.player.playerId)) {
                                        Service.openUISay(_char, npcId, Text.get(0, 350));
                                    } else if (optionId == 0 && ct.aCharBlack.contains(_char.user.player.playerId)) {
                                        Service.openUISay(_char, npcId, Text.get(0, 351));
                                    } else if (optionId == 1 && ct.aCharWhite.contains(_char.user.player.playerId)) {
                                        Service.openUISay(_char, npcId, Text.get(0, 352));
                                    } else {
                                        short mapGo = -1;
                                        if (optionId == 0) {
                                            mapGo = 98;
                                            if (!ct.aCharWhite.contains(_char.user.player.playerId)) {
                                                _char.user.player.pointChienTruong = 0;
                                                ct.aCharWhite.add(_char.user.player.playerId);
                                            }
                                        } else if (optionId == 1) {
                                            mapGo = 104;
                                            if (!ct.aCharBlack.contains(_char.user.player.playerId)) {
                                                _char.user.player.pointChienTruong = 0;
                                                ct.aCharBlack.add(_char.user.player.playerId);
                                            }
                                        }
                                        if (mapGo != -1) {
                                            final Map map3 = ChienTruong.getMap(ct, mapGo);
                                            if (map3 != null) {
                                                final TileMap tileMap2 = map3.getSlotZone(_char);
                                                if (tileMap2 != null) {
                                                    _char.tileMap.Exit(_char);
                                                    _char.cx = map3.template.goX;
                                                    _char.cy = map3.template.goY;
                                                    tileMap2.Join(_char);
                                                    Top.sortTop(5, _char.cName, ChienTruong.TITLE_CT[_char.getCT()], _char.pointChienTruong, new int[]{(_char.cTypePk == 4) ? 0 : ((_char.cTypePk == 5) ? 1 : -1)});
                                                } else {
                                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        if (optionId == 2) {
                            final ChienTruong ct = ChienTruong.chien_truong;
                            if (ct == null) {
                                Service.openUISay(_char, npcId, Text.get(0, 330));
                            } else {
                                Service.reviewChienTruong(_char, ct);
                            }
                            break;
                        }
                        break;
                    }
                    if (menuId == 4) {
                        if (SevenBeasts.isRun) {
                            if (SevenBeasts.isStart) {
                                if (SevenBeasts.checkBaoDanh(_char)) {
                                    SevenBeasts sb = SevenBeasts.getSevenBeasts(_char);
                                    if (sb != null) {
                                        SevenBeasts.joinMap(sb, _char);
                                    } else {
                                        GameCanvas.startOKDlg(_char.user.session, "C?? l???i g?? ???? x???y ra!");
                                    }
                                } else {
                                    Service.openUISay(_char, npcId, "???? h???t th???i gian b??o danh.");
                                }
                            } else if (SevenBeasts.isBaoDanh) {
                                if (SevenBeasts.checkBaoDanh(_char)) {
                                    SevenBeasts sb = SevenBeasts.getSevenBeasts(_char);
                                    if (sb != null) {
                                        SevenBeasts.joinMap(sb, _char);
                                    } else {
                                        GameCanvas.startOKDlg(_char.user.session, "C?? l???i g?? ???? x???y ra!");
                                    }
                                } else if (_char.cLevel < 50) {
                                    Service.openUISay(_char, npcId, "Y??u c???u tr??nh ????? c???p 50.");
                                } else if (_char.party == null) {
                                    Service.openUISay(_char, npcId, "B???n ch??a c?? nh??m, h??y ??i tri???u t???p th??nh vi??n ????? tham gia.");
                                } else if (!_char.party.aChar.get(0).cName.equals(_char.cName)) {
                                    Service.openUISay(_char, npcId, "Ch??? c?? nh??m tr?????ng m???i c?? th??? m??? c???a Th???t th?? ???i.");
                                } else if (_char.party.numPlayer < 1) {
                                    Service.openUISay(_char, npcId, "Nh??m ph???i c?? ????? 6 th??nh vi??n m???i c?? th??? tham gia.");
                                } else if (_char.party.checkLevelParty(_char)) {
                                    Service.openUISay(_char, npcId, "C?? th??nh vi??n trong nh??m kh??ng ????? c???p ????? y??u c???u.");
                                } else if (SevenBeasts.checkBaoDanh(_char.party)) {
                                    Service.openUISay(_char, npcId, "C?? th??nh vi??n trong nh??m ???? b??o danh r???i.");
                                } else {
                                    SevenBeasts sBeasts = new SevenBeasts();
                                    SevenBeasts.arrChar.addAll(_char.party.aChar);
                                    sBeasts.setSevenBeasts(_char);
                                    sBeasts.sendTB(_char, null, null, 3);
                                    Service.openUISay(_char, npcId, "B??o danh th??nh c??ng.");
                                }
                            }
                        } else {
                            Service.openUISay(_char, npcId, Text.get(0, 408));
                        }
                        break;
                    }
                    break;
                }
                case 26: {
                    if (menuId == 0) {
                        Service.openUI(_char, (byte) 14, null, null);
                        break;
                    }
                    if (menuId == 1) {
                        Service.openUI(_char, (byte) 15, null, null);
                        break;
                    }
                    if (menuId == 2) {
                        Service.openUI(_char, (byte) 32, null, null);
                        break;
                    }
                    if (menuId == 3) {
                        Service.openUI(_char, (byte) 34, null, null);
                        break;
                    }
                    break;
                }
                case 27: {
                    if (menuId == 0) {
                        TileMap tile = _char.tileMap;
                        _char.clan.clanManor.openTru(_char, tile.mapID, tile.map);
                    }
                    break;
                }
                case 28: {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[0]);
                            break;
                        }
                        if (optionId == 1) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[1]);
                            break;
                        }
                        if (optionId == 2) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[2]);
                            break;
                        }
                        if (optionId == 3) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[3]);
                            break;
                        }
                        if (optionId == 4) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[4]);
                            break;
                        }
                        if (optionId == 5) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[5]);
                            break;
                        }
                        if (optionId == 6) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[6]);
                            break;
                        }
                        if (optionId == 7) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[7]);
                            break;
                        }
                        if (optionId == 8) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[8]);
                            break;
                        }
                        if (optionId == 9) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[9]);
                            break;
                        }
                        if (optionId == 10) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[10]);
                            break;
                        }
                        if (optionId == 11) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[11]);
                            break;
                        }
                        break;
                    } else {
                        if (menuId != 1) {
                            break;
                        }
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                            break;
                        }
                        Service.openUI(_char, (byte) 36, null, null);
                        break;
                    }
                    //break;
                }
                case 29: {
                    if (menuId != 0) {
                        break;
                    }
                    if (optionId == 0) {
                        BiKip.LuyenBiKip(_char);
                        break;
                    }
                    if (optionId == 1) {
                        final Item it = _char.ItemBody[15];
                        if (it == null) {
                            Service.ServerMessage(_char, Text.get(0, 389));
                        } else if (it.upgrade >= 10) {
                            Service.ServerMessage(_char, Text.get(0, 390));
                        } else {
                            Service.openUIConfirmID(_char, String.format(Text.get(0, 388), it.template.name, BiKip.goldUps[it.upgrade], BiKip.SL_NL[it.upgrade], GameScr.itemTemplates[457].name, BiKip.percents[it.upgrade], "%"), (byte) (-125));
                        }
                        break;
                    }
                    if (optionId == 2) {
                        Service.AlertMessage(_char, Text.get(0, 130), Text.get(0, 385));
                        break;
                    }
                    break;
                }
                case 30: {
                    if (menuId == 0) {
                        Service.openUI(_char, (byte) 38, null, null);
                        break;
                    }
                    if (menuId == 1) {
                        Service.openTextBoxUI(_char, Text.get(0, 51), (short) 504);
                        break;
                    }
                    if (menuId == 2) {
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                            break;
                        }
                        if (optionId == 0) {
                            final Lucky lucky = Lucky.arrLucky[0];
                            Service.AlertLuck(_char.user.player, lucky);
                        } else if (optionId == 1) {
                            Service.openTextBoxUI(_char, Text.get(0, 66), (short) 100);
                        } else if (optionId == 2) {
                            Service.AlertMessage(_char, Text.get(0, 66), Text.get(0, 69));
                        }
                        _char.menuType = 0;
                        break;
                    } else {
                        if (menuId != 3) {
                            break;
                        }
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                            break;
                        }
                        if (optionId == 0) {
                            final Lucky lucky = Lucky.arrLucky[1];
                            Service.AlertLuck(_char.user.player, lucky);
                        } else if (optionId == 1) {
                            Service.openTextBoxUI(_char, Text.get(0, 67), (short) 100);
                        } else if (optionId == 2) {
                            Service.AlertMessage(_char, Text.get(0, 66), Text.get(0, 70));
                        }
                        _char.menuType = 1;
                        break;
                    }
                    //break;
                }
                case 31: {
                    if (menuId != 0) {
                        break;
                    }
                    if (!_char.isHuman) {
                        Service.ServerMessage(_char, Text.get(0, 310));
                        break;
                    }
                    if (GameScr.vEvent != 1 || npc.type == 15) {
                        break;
                    }
                    if (_char.user.player.ItemBagQuantity((short) 310) < 1) {
                        Service.ServerMessage(_char, Text.get(0, 174));
                        break;
                    }
                    Event.Lighting(_char, npc);
                    break;
                }
                case 32: {
                    if (_char.tileMap != null && _char.tileMap.mapID == 117) {
                        if (menuId == 0) {
                            Service.openTextBoxUI(_char, "Nh???p xu c?????c", (short) 515);
                        } else if (menuId == 1) {
                            TileMap.getMapLTD(_char);
                        }
                        break;
                    } else if (_char.tileMap != null && (_char.tileMap.mapID == 118 || _char.tileMap.mapID == 119)) {
                        if (menuId == 0) {
                            TileMap.getMapLTD(_char);
                        } else if (menuId == 1) {
                            Service.AlertMessage(_char,"T???ng k???t", String.format("%s : T??ch lu??? %s ??i???m\n" +
                                    "%s : T??ch lu??? %s ??i???m", _char.clan.warClan.clanBlack.name, _char.clan.warClan.pointBlack,
                                    _char.clan.warClan.clanWhite.name, _char.clan.warClan.pointWhite));
                        }
                        break;
                    }
                    if (menuId == 0) {
                        break;
                    }
                    if (menuId == 1) {
                        Clan clan = Clan.get(_char.cClanName);
                        if (clan.warClan != null) {
                            short mapId = 22;
                            if (_char.clan.typeWar == 4) {
                                mapId = 118;
                            }
                            if (_char.clan.typeWar == 5) {
                                mapId = 119;
                            }
                            final Map map = WarClan.getMap(clan.warClan, mapId);
                            if (map != null) {
                                final TileMap tile = map.getSlotZone(_char);
                                if (tile == null) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                } else {
                                    _char.tileMap.Exit(_char);
                                    _char.cx = tile.map.template.goX;
                                    _char.cy = tile.map.template.goY;
                                    tile.Join(_char);
                                }
                            }
                        } else if (clan != null) {
                            if (clan.main_name.equals(_char.cName)) {
                                Service.openTextBoxUI(_char, "Nh???p t??n gia t???c ?????i th???", (short) 514);
                            } else {
                                Service.openUISay(_char, npcId, "Ch???c n??ng ch??? d??nh cho t???c tr?????ng.");
                            }
                        } else {
                            Service.openUISay(_char, npcId, "Ch???c n??ng ch??? d??nh cho ng?????i c?? gia t???c.");
                        }
                        break;
                    }
                    if (menuId == 4) {
                        if (optionId == 0) {
                            Service.openUI(_char, (byte) 43, null, null);
                            break;
                        }
                        if (optionId == 1) {
                            Service.openUI(_char, (byte) 44, null, null);
                            break;
                        }
                        if (optionId == 2) {
                            Service.openUI(_char, (byte) 45, null, null);
                            break;
                        }
                        if (optionId == 3) {
                            Service.AlertMessage(_char, Text.get(0, 130), Text.get(0, 131));
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 33: {
                    if (!_char.isHuman) {
                        Service.ServerMessage(_char, Text.get(0, 310));
                        break;
                    }
                    Event.doEventMenu(_char, menuId, optionId, npcId);
                    break;
                }
                case 34: {
                    if (GameScr.vEvent == 3) {
                        Event.cayThong(_char, menuId, optionId, npcId);
                    }
                    if (GameScr.vEvent == 4) {
                        Event.hoaDao(_char, menuId, optionId, npcId);
                        break;
                    }
                    break;
                }
                case 36: {
                    switch (menuId) {
                        case 0: {
                            String str = "";
                            if (GameScr.qua_top == 0) {
                                Service.openUISay(_char, npcId, "Ch??a t???i th???i gian nh???n qu??.");
                                return;
                            }
                            RewardTop.Reward(_char, npcId);
                            break;
                        }
                        case 1: {
                            Service.openTextBoxUI(_char, Text.get(0, 51), (short) 504);
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                }
                case 37: {
                    switch (menuId) {
                        case 0: {
                            Service.AlertMessage(_char, "Th??nh t??ch", "- B???n c?? " + _char.pointTalent + " ??i???m t??i n??ng");
                            break;
                        }
                        case 1: {
                            final NJTalent nj = NJTalent.ninja_talent;
                            synchronized (nj.NJ_LOCK) {
                                final Map ltd = MapServer.getMapServer(_char.mapLTDId);
                                if (ltd != null) {
                                    final TileMap tile = ltd.getSlotZone(_char);
                                    if (tile == null) {
                                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                    } else {
                                        _char.tileMap.Exit(_char);
                                        _char.cx = tile.map.template.goX;
                                        _char.cy = tile.map.template.goY;
                                        tile.Join(_char);
                                    }
                                }
                            }
                            break;
                        }
                        case 2: {
                            Service.AlertMessage(_char, "H?????ng d???n", "");
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    protected static void MenuNew(final Char _char, final byte npcId, final byte menuId, final byte optionId) {
        switch (_char.menuType + 128) {
            case 0: {
                if (menuId == 0) {
                    _char.menuType = -127;
                    Service.openUIMenuNew(_char, new String[]{Text.get(0, 71), Text.get(0, 72)});
                    break;
                }
                if (menuId == 1) {
                    _char.menuType = -126;
                    Service.openUIMenuNew(_char, new String[]{Text.get(0, 71), Text.get(0, 72)});
                    break;
                }
                break;
            }
            case 1: {
                if (menuId == 0) {
                    final Lucky lucky = Lucky.arrLucky[0];
                    Service.AlertLuck(_char.user.player, lucky);
                    break;
                }
                if (menuId == 1) {
                    Service.AlertMessage(_char, Text.get(0, 66), Text.get(0, 69));
                    break;
                }
                break;
            }
            case 2: {
                if (menuId == 0) {
                    final Lucky lucky = Lucky.arrLucky[1];
                    Service.AlertLuck(_char.user.player, lucky);
                    break;
                }
                if (menuId == 1) {
                    Service.AlertMessage(_char, Text.get(0, 66), Text.get(0, 70));
                    break;
                }
                break;
            }
            case 3: {
                Admin.controller(_char, menuId);
                break;
            }
            case 4: {
                if (menuId == 0) {
                    if (_char.user.player.ItemBagQuantity((short) 251) < 300) {
                        Service.ServerMessage(_char, "Kh??ng ????? m???nh gi???y v???n");
                    } else {
                        _char.user.player.ItemBagUses((short) 251, 300);
                        Item it = new Item(null, (short) 253, 1, -1, false, (byte) 0, 0);
                        _char.user.player.ItemBagAddQuantity(it);
                    }
                    break;
                }
                if (menuId == 1) {
                    if (_char.user.player.ItemBagQuantity((short) 251) < 250) {
                        Service.ServerMessage(_char, "Kh??ng ????? m???nh gi???y v???n");
                    } else {
                        _char.user.player.ItemBagUses((short) 251, 250);
                        Item it = new Item(null, (short) 252, 1, -1, false, (byte) 0, 0);
                        _char.user.player.ItemBagAddQuantity(it);
                    }
                    break;
                }
                break;
            }
        }
    }

    protected static void openMenu(final Char _char, final short npcTemplateId) {
        String[] Arrcaption = null;
        _char.menuType = 0;
        if (Task.isTaskNPC(_char, npcTemplateId)) {
            Arrcaption = new String[]{null};
            if (_char.ctaskIndex == -1) {
                Arrcaption[0] = GameScr.taskTemplates[_char.ctaskId].name;
            } else if (Task.isFinishTask(_char)) {
                Arrcaption[0] = Text.get(0, 12);
            } else if (_char.ctaskIndex >= 0 && _char.ctaskIndex <= 4 && _char.ctaskId == 1) {
                Arrcaption[0] = GameScr.taskTemplates[_char.ctaskId].name;
            } else if (_char.ctaskIndex >= 1 && _char.ctaskIndex <= 15 && _char.ctaskId == 7) {
                Arrcaption[0] = GameScr.taskTemplates[_char.ctaskId].name;
            } else if (_char.ctaskIndex >= 1 && _char.ctaskIndex <= 3 && _char.ctaskId == 15) {
                Arrcaption[0] = "Giao th??";
            } else if (_char.ctaskIndex == 1 && _char.ctaskId == 20) {
                Arrcaption[0] = "V??o trong hang";
            } else if (_char.ctaskIndex == 1 && _char.ctaskId == 17) {
                Arrcaption[0] = "V??? ??i cu ??i";
            }
            // menu npc nv
            else if (_char.ctaskIndex >= 1 && _char.ctaskIndex <= 3 && _char.ctaskId == 13) {
                Arrcaption[0] = GameScr.taskTemplates[_char.ctaskId].name;
            }
        }
        if (_char.tileMap != null && _char.tileMap.map.isTestDunMap() && npcTemplateId == 0 && !_char.tileMap.map.testDun.isFinght) {
            _char.menuType = 1;
            Service.openUIMenuNew(_char, new String[]{Text.get(0, 295), Text.get(0, 296), Text.get(0, 297)});
        } else if (_char.tileMap != null && _char.tileMap.map.isChienTruong() && npcTemplateId == 25) {
            _char.menuType = 1;
            Service.openUIMenuNew(_char, new String[]{Text.get(0, 295), Text.get(0, 335)});
        } else if (_char.tileMap != null && _char.tileMap.mapID == 22 && npcTemplateId == 24) {
            _char.menuType = 1;
            Service.openUIMenuNew(_char, new String[]{Text.get(0, 155)});
        } else if (_char.tileMap != null && _char.tileMap.map.isWarClanMap() && npcTemplateId == 32) {
            _char.menuType = 1;
            if (_char.tileMap.mapID == 117 ) {
                Service.openUIMenuNew(_char, new String[]{"?????t c?????c", "R???i kh???i n??i n??y"});
            }
            if (_char.tileMap.mapID == 118 || _char.tileMap.mapID == 119) {
                Service.openUIMenuNew(_char, new String[]{"R???i kh???i n??i n??y", "T???ng k???t"});
            }
        } else {
            Service.openUIMenu(_char, Arrcaption);
        }
    }

    static {
        arrLevelGift = new int[]{10, 20, 30, 40, 50};
    }
}
