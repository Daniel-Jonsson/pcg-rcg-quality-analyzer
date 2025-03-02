package com.mygdx.platformer.utilities;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {
    public static void main(String[] args) {
        TexturePacker.process(
            "assets/goblin_sprites",  // Input folder (contains images)
            "assets/atlas",           // Output folder (where atlas will be saved)
            "goblin_sprites"          // Atlas filename
        );
        System.out.println("Texture packing complete!");
    }
}
