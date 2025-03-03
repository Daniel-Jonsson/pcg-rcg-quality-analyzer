package com.mygdx.platformer.utilities;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Utility class for packing textures into a texture atlas using LibGDX's
 * {@link TexturePacker} This tool processes individual sprite images and
 * generates a texture atlas, which is used for optimized rendering in the
 * game.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public class TexturePackerTool {
    public static void main(String[] args) {
        TexturePacker.process(
            "assets/necromancer_sprites",  // Input folder (contains images)
            "assets/atlas",           // Output folder (where atlas will be saved)
            "necromancer_sprites"          // Atlas filename
        );
        System.out.println("Texture packing complete!");
    }
}
