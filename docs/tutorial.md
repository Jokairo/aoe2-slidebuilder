# AoE2:DE Slide Builder Instructions

This guide provides a step-by-step walkthrough on how to use Slide Builder to create custom cutscenes and campaign menus for your campaigns in Age of Empires II: Definitive Edition.

## Getting Started

### Requirements

- Age of Empires II: Definitive Edition installed
- (Optional) Audiokinetic WWise installed if your cutscenes use audio. Slide Builder will use WWise in the background to convert audio into the correct format.

### Creating Campaign

Before working with Slide Builder, you must first create a custom campaign in Age of Empires II:
1. Open the **Editor** in Aoe2:DE
2. Create and save your scenarios
3. Go to the **Campaign Editor** tab
4. Add your scenarios and click **Save Campaign**

Your campaign will be saved as a `.aoe2campaign` file in:

`C:\Users\<Your Name>\Games\Age of Empires 2 DE\<Your User ID>\resources\_common\campaign`

## Specifying Campaign in Slide Builder

In Slide Builder:
- Go to **Add Campaign File** > **Browse...**
- Select your `.aoe2campaign` file (it should automatically have the campaign folder open)
- Enter the number of scenarios in your campaign in **Number of Scenarios**

## Editing Campaign Menu Screen

This is the screen players see when selecting a scenario in your campaign. To edit it, click **Edit Campaign Menu** in the main view.

To preview it, go to **View** > **Campaign Menu Preview** on the topbar.

### Campaign Menu Options

| Option | Description |
| ------ | ----------- |
| **Title Text** | The title displayed at the top of the screen. |
| **Background Image** | The background image for the menu. See [Adding Custom Assets](#adding-custom-assets) for how to include your own background images. |
| **Disable Custom Menu** | If checked, uses the default AoE2 custom campaign menu instead. |

To edit each individual scenario button, click **Edit Scenario Buttons**. Use the bottom tabs to choose the button you want to modify.

### Scenario Button Options

| Option | Description |
| ------ | ----------- |
| **Button Label Text** | The displayed scenario name. *Tip:* Use numbering like `1. The Battle Begins`. |
| **Button Coordinates** | Position of the button image on screen (X, Y). |
| **Label Coordinates** | Position of the scenario label relative to the button (X, Y). |
| **Scenario Difficulty** | An icon to indicate difficulty level. |
| **Button Image** | The image for the scenario button. See [Adding Custom Assets](#adding-custom-assets) for how to add your own button images. |
| **Image Size** | Width and height of the image. |
| **Keep Aspect Ratio** | Maintain the current aspect ratio when resizing image. |
| **Reset Size** | Restores image's original size. |
| **Button Hover Help Text** | The text displayed at the bottom right of the screen when the player hovers over a scenario button. Use this to give players information about the scenario, such as objectives, historical context, or difficulty. You can style the text using tags like `<blue>`, `<i>`, etc. Example: `<blue><i>Your mission is to defeat all enemy castles.`. You can add tags by clicking the colored rectangles. You can preview help text behavior in **Campaign Menu Preview**. |
| **Help Text Style** | Adds the AoE2 expansion icon and label above the help text. |

You can also move and resize buttons visually in **Campaign Menu Preview**:

| Action | Instructions |
| ------ | ----------- |
| Move Button | Click the button > Drag to move |
| Resize Button | Click the button > Drag edges (disable **Keep Aspect Ratio** for free scaling) |
| Move Label | Click the label > Drag to move |
| Preview Help Text | Hover over a button |

## Editing Slideshows

Each scenario can have an **Intro** (before mission) and **Outro** (after victory) cutscene. To edit them, click **Edit Slideshows** on the main view.

Use the bottom tabs like `1I`, `1O`, `2I`, `2O` to select which scenario's intro/outro to edit.

To preview the slideshow, go to **View** > **Slideshow Preview** on the topbar.

### Slideshow Options

| Option | Description |
| ------ | ----------- |
| **Number of Slides** | How many slides the slideshow will include. |
| **Slide Background** | Background image shown behind all slides. See [Adding Custom Assets](#adding-custom-assets) for how to include your own background images. |
| **Add Slideshow Audio (Optional)** | To add narration or background music, select a `.wav` file. See [Syncing Audio with Slides](#syncing-audio-with-slides) for syncing audio to slide changes. |
| **Don't Show Slideshow** | If checked, skips the intro/outro cutscene entirely for the scenario. |

Click **Edit Slides** to edit each individual slide. Use the bottom tabs to choose the slide you want to modify. The preview will display the slide that you have selected for editing.

### Slide Options

| Option | Description |
| ------ | ----------- |
| **Slide Text** | Text shown on the slide. |
| **Text Coordinates** | Position of the text on screen (X, Y). |
| **Text Dimensions** | Width and height of the text area (text will wrap if width is too small). *Note:* While the height property is available to edit, it doesn't do anything. If the text extends the height, the text will just cut off. It's recommended to not  lower the height and increase the height if needed. |
| **Image** | The image shown on the slide. By default, no images are included so you must add your own. See [Adding Custom Assets](#adding-custom-assets) for help. |
| **Image Coordinates** | Position of the image on screen (X, Y). |
| **Image Size** | Width and height of the image. |
| **Keep Aspect Ratio** | Maintain the current aspect ratio when resizing image. |
| **Reset Size** | Restores image's original size. |
| **Slide Duration** | Time in seconds the slide is shown before transitioning to another slide. If using audio, it's recommended to use [Slide Duration Editor](#syncing-audio-with-slides) instead. |

You can also move and resize elements visually in **Slideshow Preview**:

| Action | Instructions |
| ------ | ----------- |
| Move Text Area | Click the text > Drag to move |
| Resize Text Area | Click the text > Drag edges |
| Move Image | Click the image > Drag to move |
| Resize Image | Click the image > Drag edges (disable **Keep Aspect Ratio** for free scaling) |

## Adding Custom Assets

To use your own images for buttons, backgrounds, or slides:

1. Go to **Edit** > choose one of:
   - Slideshow Image
   - Slideshow Background
   - Campaign Menu Button Image
   - Campaign Menu Background
2. Click **Add Images...** and select one or more `.png` files
3. Click **Save and Close**

Your images are now available in the dropdowns.

## Syncing Audio with Slides

Traditionally, slideshows in AoE2 include a narrator that reads the text displayed on the screen. If you have multiple slides, you must calculate the duration for each slide so that the slides stay in sync with the narrator. Slide Builder includes a tool which will automatically sync the slides with the audio.

To make your slideshow match narration:

1. Add a `.wav` audio file in **Add Slideshow Audio**
2. Open **Slide Duration Editor**
3. In the editor, use **Add Marker** to place slide change markers while the audio plays
4. Markers can be:
   - Added while playing
   - Dragged to adjust timing
   - Deleted by right clicking
5. Once all markers have been added, click **Apply Changes**. Each slide will automatically get the correct duration.

If you don't have any available markers, it is likely because the slideshow only has 1 slide (meaning no slides to change into).

After syncing the audio, you can preview that it looks correct by clicking **Play Slideshow**. In the preview, click **Play** to see how the slideshow would look in-game. The preview will play the audio and change slide in the positions that you determined in Slide Duration Editor.

## Saving and Loading Projects

- **File** > **Save As...** to name and save your project
- **File** > **Save** to overwrite the last saved file
- **File** > **Open** to load project file

## Exporting

When you are ready to test your campaign in-game:

1. Go to **File** > **Export**
2. Requirements:
   - A valid `.aoe2campaign` file added
   - WWise installed if you're using audio
3. Choose your **AoE2 local mod folder** (this is auto-suggested, using it ensures your campaign appears immediately in-game)
4. Name the mod folder and click **Save**

To test the exported campaign:
1. Launch Age of Empires II: DE
2. Go to **Single Player > Campaigns > Custom Campaigns**
3. Select the campaign (the name will be the same that as in the **.aoe2campaign** file)

Your exported campaign will be fully functional and identical to the one you created in Slide Builder!