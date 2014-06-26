package me.jezzadabomb.es2.common.core;

import java.util.HashSet;

import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;

public class PylonSphere {

    private CoordSet coordSet;
    private HashSet<CoordSet> positionArray;

    public PylonSphere(CoordSet coordSet, int radius) {
        this.coordSet = coordSet;

        for (int i = -radius; i <= radius; i++)
            for (int j = -radius; j <= radius; j++)
                for (int k = -radius; k <= radius; k++) {
                    CoordSet tempSet = coordSet.copy().addX(i).addY(j).addZ(k);
                    if (tempSet.getDistanceSq(tempSet) < (radius * radius))
                        positionArray.add(tempSet);
                }
    }

    public boolean isPowering(CoordSet coordSet) {
        return positionArray.contains(coordSet);
    }
}
