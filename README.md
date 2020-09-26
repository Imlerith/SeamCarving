# Seam Carving
The seam carving algorithm allows to resize images without distortion so that they can be displayed without losses in content on cell phones and web browsers. Seam carving removes, vertically or horizontally, streaks of pixels or *seams* which have the smallest *energy*. The energy of a pixel can be computed via e.g. the *dual-gradient energy function*.

The search of the seams with the smallest energy is then done via shortest-path search in an edge-weighted pixel DAG. In the current script, [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) is applied.

Below two pictures, before and after resizing, are displayed:

<div class="img-with-text">
     <p>Before resizing:</p>
    <img src="https://github.com/Imlerith/SeamCarving/blob/master/images/pic.jpg" alt="sometext" />
</div>

<p align="left">
  Before resizing:
  <img src="https://github.com/Imlerith/SeamCarving/blob/master/images/pic.jpg">
</p>

<p align="center">
  After resizing:
  <img src="https://github.com/Imlerith/SeamCarving/blob/master/images/pic_resized.jpg">
</p>
