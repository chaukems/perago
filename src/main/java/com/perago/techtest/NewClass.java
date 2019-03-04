/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.perago.techtest;

import com.cedarsoftware.util.GraphComparator;
import com.perago.techtest.test.Person;
import java.util.List;

/**
 *
 * @author F4829689
 */
public class NewClass {

    public static void main(String[] args) throws DiffException {

        Person friend1 = new Person();
        friend1.setFirstName("Jabulani");
        friend1.setSurname("Mabale");

        Person person1 = new Person();
        person1.setFirstName("Matimba");
        person1.setSurname("Chauke");
        person1.setFriend(friend1);

        Person friend2 = new Person();
        friend1.setFirstName("Kulani");
        friend1.setSurname("Baloyi");

        Person person2 = new Person();
        person2.setFirstName("Sydney");
        person2.setSurname("Maluleke");
        person2.setFriend(friend2);

        DiffRender render = new DiffRender();

        Diff<?> calculatedResults = new Diff(null).calculate(person1, person2);

        System.out.println(render.render(calculatedResults));

        //Object applyResults = calculatedResults.apply(person1, calculatedResults);

       // Diff<?> diffResults = new Diff(applyResults) {        };

       // System.out.println(render.render(diffResults));

        List<GraphComparator.Delta> deltas = GraphComparator.compare(person1, person2, new GraphComparator.ID() {
            @Override
            public Object getId(Object o) {
                return "id";
            }
        });

        System.out.println(deltas.size() + " diff(s) found:");
        for (GraphComparator.Delta d : deltas) {
            System.out.println(d.getFieldName() + " " + d.getTargetValue());
        }
    }

}
