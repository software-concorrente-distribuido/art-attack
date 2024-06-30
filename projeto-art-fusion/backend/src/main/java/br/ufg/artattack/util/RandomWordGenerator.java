package br.ufg.artattack.util;

import java.util.Random;

public class RandomWordGenerator {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public static String generateRandomWord(int length) {
        Random random = new Random();
        StringBuilder word = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHABET.length());
            word.append(ALPHABET.charAt(index));
        }

        return word.toString();
    }
}

