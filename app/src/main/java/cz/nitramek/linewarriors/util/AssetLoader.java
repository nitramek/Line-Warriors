package cz.nitramek.linewarriors.util;


import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AssetLoader {

    public static String loadText(AssetManager assets, String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(assets.open(fileName)))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }
    }
}
