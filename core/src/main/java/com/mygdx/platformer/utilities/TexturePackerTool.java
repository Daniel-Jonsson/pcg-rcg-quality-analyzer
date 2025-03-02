package com.mygdx.platformer.utilities;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {
    public static void main(String[] args) {
        TexturePacker.process(
            "assets/player_sprites",  // Input folder (contains images)
            "assets/atlas",           // Output folder (where atlas will be saved)
            "player_sprites"          // Atlas filename (player_sprites.atlas & .png)
        );
        System.out.println("Texture packing complete!");
    }
}
