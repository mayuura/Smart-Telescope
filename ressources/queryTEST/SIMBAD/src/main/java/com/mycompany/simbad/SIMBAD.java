/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.simbad;

/**
 *
 * @author embouddi
 */
public class SIMBAD {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Double ra=3.9084429453;
        Double dec=-1.442668530641793;
        int radius=2;
        String query=SIMBADQuery.querySIMBAD(ra,dec,radius);
        System.out.println(query);
        
    }
}
