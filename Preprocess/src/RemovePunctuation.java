/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author workstation-pc
 */
public class RemovePunctuation {
    public static void main(String[] args) {
        String input = "Testing, testing, 1, one, 2, two, 3, three. wow! you have 2000 rupees ";
        String alpha = input.replaceAll("[^a-zA-Z\\s]", "").replaceAll("\\s+", " ");
        System.out.println(alpha);
    }
}
