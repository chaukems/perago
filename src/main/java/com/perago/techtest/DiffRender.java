/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.perago.techtest;

import com.cedarsoftware.util.GraphComparator;
import com.google.gson.Gson;
import java.util.Set;

/**
 *
 * @author F4829689
 */
public class DiffRender implements DiffRenderer {

    private Gson gson = new Gson();

    @Override
    public String render(Diff<?> diff) throws DiffException {

        String result = gson.toJson(diff.getDiffObject());

        return result;
    }

}
