![ParagonBanner1](https://user-images.githubusercontent.com/85251388/154859895-65627d8d-0753-43ea-91c4-d8c092e478c3.png)

# Paragon
A 1.12.2 Anarchy Client

Created by Wolfsurge and TeleTofu

# Build instructions
Windows:

`gradlew setupDecompWorkspace`

`gradlew build`

Linux/Mac:

`chmod +x gradlew`

`./gradlew setupDecompWorkspace`

`./gradlew build`

# Credits
Most if not all files should have an @author tag at the top so you can see who wrote the file. (It's mostly Wolfsurge)

# Using the Custom Font
A directory is created in the Paragon config folder (/.minecraft/paragon), called "font". In here, upon first opening the client, two files will be present.
"font.ttf" is the font that will be used when the Font module is enabled. The second file is "font_config.json". Here you can find a couple of font rendering
settings. "size" is an integer (whole number) value that determines the font's size. "y_offset" is a float (decimal) value to determin how much to add to the Y value when text is rendered. (this is so you can center it so it looks good (*cough* wp3 *cough*).

You can change the custom font by simply deleting "font.ttf" in the font folder, and replacing it with a different TTF font file, which is also called "font.ttf". It is
imperative that it is named this as otherwise the client will not detect the font, and will therefore default to Java's default font. Support for fonts with different file names might be added in the future.
