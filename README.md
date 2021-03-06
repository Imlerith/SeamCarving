# Seam Carving
The seam carving algorithm allows to resize images without distortion so that they can be displayed without losses in content on cell phones and web browsers. Seam carving removes, vertically or horizontally, streaks of pixels or *seams* which have the smallest *energy*. The energy of a pixel can be computed via e.g. the *dual-gradient energy function*.

The search of the seams with the smallest energy is then done via shortest-path search in an edge-weighted pixel DAG. In the current script, [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) is applied.

Below two pictures, before and after resizing, are displayed:

<table width="500" border="1" cellpadding="5">
    <tr>
        <td align="center" valign="center">
            Before resizing:
            <br />
            <img src="https://github.com/Imlerith/SeamCarving/blob/master/images/pic.jpg" alt="before resizing" />
        </td>
        <td align="center" valign="center">
            After resizing:
            <br />
            <img src="https://github.com/Imlerith/SeamCarving/blob/master/images/pic_resized.jpg" alt="after resizing" />
        </td>
    </tr>
</table>

Before resizing, the size of the picture is 320x217 pixels, after resizing 220x217 pixels. As can be seen, seam carving preserves the most interesting features (aspect ratio, set of objects present, etc.) of the image.
