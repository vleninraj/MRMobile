package com.atlanta.mr;

import com.atlanta.mr.Models.POSDTL;

import java.util.Comparator;

public class PosDtlComparator implements Comparator<POSDTL> {

    @Override
    public int compare(POSDTL o1, POSDTL o2) {
        return o2.get_slno() - o1.get_slno();
    }
}
