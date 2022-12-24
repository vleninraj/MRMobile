package com.atlanta.mr;

import com.atlanta.mr.Models.MRDTL;


import java.util.Comparator;

public class PosDtlComparator implements Comparator<MRDTL> {

    @Override
    public int compare(MRDTL o1, MRDTL o2) {
        return o2.get_slno() - o1.get_slno();
    }
}
