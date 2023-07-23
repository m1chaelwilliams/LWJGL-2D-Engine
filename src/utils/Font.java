package utils;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import graphics.Surface;

public class Font {
    private Surface glyph;
    private char[] characters;
    private int fontSize;

    public Font(Surface glyph, String characters, int fontSize) {
        this.glyph = glyph;
        this.characters = characters.toCharArray();
        this.fontSize = fontSize;

        glyph.resize(new Vector2f((float)fontSize * this.characters.length, (float)fontSize));
    }
    public Surface render(String text) {
        char[] sentence = text.toCharArray();

        List<Float> data = new ArrayList<>();


        int characterOffset = 0;
        for (char character : sentence) {
            int index = findIndex(character);
            int xCoord = index*fontSize;
            float normalizedCoord = xCoord / glyph.getWidth();
            float normalizedFontSize = fontSize / glyph.getWidth();
            
            if (character == ' ') {
                characterOffset++;
            } else {
                if (index != -1) {
                    float characterLeft = characterOffset * normalizedFontSize;
                    float characterRight = characterLeft + normalizedFontSize;

                    float[] charData = {
                        characterLeft, 1f, normalizedCoord, 1f,
                        characterRight, 0f, normalizedCoord + normalizedFontSize, 0f,
                        characterLeft, 0f, normalizedCoord, 0f,
            
                        characterLeft, 1f, normalizedCoord, 1f,
                        characterRight, 1f, normalizedCoord + normalizedFontSize, 1f, 
                        characterRight, 0f, normalizedCoord + normalizedFontSize, 0f
                    };

                    for (float num : charData) {
                        data.add(num);
                    }
                    characterOffset++;
                }
            }

        }

        float[] dataFormatted = new float[data.size()];


        for (int i = 0; i < dataFormatted.length; i++) {
            dataFormatted[i] = data.get(i);
        }

        Surface textSurface = glyph;
        textSurface.setData(dataFormatted);
        
        return textSurface;
    }
    public int findIndex(char character) {
        for (int i = 0; i < characters.length; i++) {
            if(characters[i] == character) {
                return i;
            }
        }
        return -1;
    }
}
